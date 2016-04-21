package cl.niclabs.adkintunmobile.services.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.Report;
import cl.niclabs.adkintunmobile.data.persistent.ApplicationTraffic;
import cl.niclabs.adkintunmobile.data.persistent.TrafficObservationWrapper;
import cl.niclabs.adkintunmobile.utils.volley.HttpMultipartRequest;
import cl.niclabs.adkintunmobile.utils.volley.VolleySingleton;

public class Synchronization extends Service {

    private final String TAG = "AdkM:Synchronization";
    private Context context;

    public Synchronization() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        Log.d(this.TAG, "Creado El servicio de sincronización");

        // 0.- Build a report
        Report report = new Report(getApplicationContext());
        // 1.- Prepare data
        byte[] data = collectStoredData(report);
        // 2.- Prepare request
        sendData(data);
        // 2,5.- Backup data
        backupData();
        // 3.- Clean DB
        report.cleanDBRecords();
        // 4.- StopService
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(this.TAG, "Detenido El servicio de sincronización");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /*
     *  Utility Methods
     */

    private void backupData(){
        Iterator<TrafficObservationWrapper> iterator = TrafficObservationWrapper.findAsIterator(TrafficObservationWrapper.class, "uid > 0");

        while(iterator.hasNext()){
            ApplicationTraffic value = new ApplicationTraffic(iterator.next());
            value.save();
        }
    }

    private byte[] collectStoredData(Report report) {
        // The output object
        byte[] data = null;

        // Store to String
        Gson gson = new Gson();
        String reportData = gson.toJson(report);

        // Store in cache
        File outputDir = this.getCacheDir();
        try {
            File outputFile = File.createTempFile("report", ".adkdb", outputDir);
            FileOutputStream outStream = new FileOutputStream(outputFile);
            outStream.write(reportData.getBytes());
            outStream.flush();
            outStream.close();
            data = readContentIntoByteArray(outputFile);

            // Delete cache file
            if (!outputFile.delete())
                Log.d(this.TAG, "Error al borrar el archivo temporal");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private static byte[] readContentIntoByteArray(File file) {
        FileInputStream fileInputStream;
        byte[] bFile = new byte[(int) file.length()];
        try {
            // Convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bFile;
    }

    private void sendData(byte[] data) {
        byte[] multipartBody = null;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            // Primer archivo adjuntado al DataOutputStream
            HttpMultipartRequest.buildPart(dos, data, "report.JSON");
            // Agregar "multipart form data" después de los archivos
            HttpMultipartRequest.writeBytes(dos);
            // Crear multipart body
            multipartBody = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creación multipart request
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        HttpMultipartRequest multipartRequest =
                new HttpMultipartRequest(
                        sharedPreferences.getString(this.context.getString(R.string.settings_sampling_hostname_key), ""),
                        null,
                        multipartBody,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                Toast.makeText(getApplicationContext(), "Upload successfully!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Upload successfully!");

                                // Registro de la última sincronización exitosa
                                // TODO: crear un sistema persistente de BD para almacenar este tipo de eventos
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", java.util.Locale.getDefault());
                                Date date = new Date();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(context.getString(R.string.settings_sampling_lastsync_key), dateFormat.format(date));
                                editor.apply();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Upload failed!" + error.toString(), Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "Upload failed! " + error.toString());
                            }
                        }) {
                    @Override
                    public void deliverError(VolleyError error) {
                        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(this);
                        VolleySingleton.getInstance(getApplicationContext()).getRequestQueue().stop();
                        Toast.makeText(getApplicationContext(), "Deliver Error, Queued!" + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Deliver Error, Queued! " + error.toString());
                        // mErrorListener.onErrorResponse(error);

                    }
                };

        // Agregar multipartrequest a la cola de peticiones
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(multipartRequest);
    }

}

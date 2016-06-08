package cl.niclabs.adkintunmobile.services.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

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
import java.util.HashMap;
import java.util.Map;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.Report;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NewsNotification;
import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;
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

        // 1.- Build a report
        Report report = new Report(getApplicationContext());
        if (report.recordsToSend()){
            // 2.- Save report
            report.saveFile(context);
            // 3.- Backup visualization data
            report.saveVisualSamples();
            // 4.- Clean DB
            report.cleanDBRecords();
        }
        // 5.- StopService
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
}

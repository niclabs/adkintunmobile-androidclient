package cl.niclabs.adkintunmobile.views.dashboard;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;
import cl.niclabs.adkintunmobile.BuildConfig;
import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.monitors.TelephonyMonitor;
import cl.niclabs.adkintunmobile.views.status.FileSizeDialog;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private final String TAG = "AdkM:MainActivity";
    private final int REQUEST_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG_MODE)
            Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        this.context = this;

        int permissionCheck = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Exception", "No permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
        } else {
            telephonyWorkRequest();
        }
        showFileSizeDialog();

    }

    private void showFileSizeDialog() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value = sharedPreferences.getString(this.getString(R.string.speedtest_file_size), "");
        if (value.equals("")) {
            FragmentManager fm = getSupportFragmentManager();
            FileSizeDialog alertDialog = FileSizeDialog.newInstance("File Size Picker");
            alertDialog.show(fm, "fragment_alert");
        } else {
            String[] fileSizeOptions = getResources().getStringArray(R.array.speed_test_file_size);
            TextView fileSize = findViewById(R.id.file_size);
            fileSize.append(fileSizeOptions[Integer.parseInt(value)]);
        }
    }


    public void telephonyWorkRequest() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .build();

        final PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(TelephonyMonitor.class, 15, TimeUnit.MINUTES)
                .addTag("telephony_check")
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance().enqueueUniquePeriodicWork("telephony",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest);

        WorkManager.getInstance().getStatusesByTag(TelephonyMonitor.SLAVE_WORKER)
                .observe(this, new Observer<List<WorkStatus>>() {
                    @Override
                    public void onChanged(@Nullable List<WorkStatus> workStatuses) {
                        if (workStatuses != null) {
                            if (!workStatuses.isEmpty()) {
                                for (WorkStatus workStatus : workStatuses) {
                                    if (workStatus != null && workStatus.getState().isFinished()) {
                                        Log.i("STATUS CHANGED", workStatus.getState().name());
                                        String lac = workStatus.getOutputData().getString(TelephonyMonitor.LAC,"LAC");
                                        String type = workStatus.getOutputData().getString(TelephonyMonitor.TYPE,"LAC");
                                        String strength = workStatus.getOutputData().getString(TelephonyMonitor.DBM,"LAC");
                                        String cid = workStatus.getOutputData().getString(TelephonyMonitor.CID,"LAC");
                                        Log.i("STATUS CHANGED", lac);
                                        TextView lacText = findViewById(R.id.lac);
                                        lacText.append(lac);

                                        TextView typeText = findViewById(R.id.type);
                                        typeText.append(type);

                                        TextView strengthText = findViewById(R.id.intensity);
                                        strengthText.append(strength);

                                        TextView cidText = findViewById(R.id.cid);
                                        cidText.append(cid);
                                    }
                                }
                            }
                        }
                    }
                });
        /*
        WorkManager.getInstance().getStatusById(workRequest.getId())
                .observe(this, new Observer<WorkStatus>() {
                    @Override
                    public void onChanged(@Nullable WorkStatus workStatus) {
                        if (workStatus != null) {
                            Log.i("STATUS SIZE", workStatus.getState().name());
                            if (!workStatus.getOutputData().getString(TelephonyMonitor.LAC, "").equals("")) {
                                Log.i("STATUS CHANGED", "NEW STAT");
                                String lac = workStatus.getOutputData().getString(TelephonyMonitor.LAC,"LAC");
                                String type = workStatus.getOutputData().getString(TelephonyMonitor.TYPE,"LAC");
                                String strength = workStatus.getOutputData().getString(TelephonyMonitor.DBM,"LAC");
                                String cid = workStatus.getOutputData().getString(TelephonyMonitor.CID,"LAC");
                                Log.i("STATUS CHANGED", lac);
                                TextView lacText = findViewById(R.id.lac);
                                lacText.append(lac);

                                TextView typeText = findViewById(R.id.type);
                                typeText.append(type);

                                TextView strengthText = findViewById(R.id.intensity);
                                strengthText.append(strength);

                                TextView cidText = findViewById(R.id.cid);
                                cidText.append(cid);
                            }
                        }
                    }
                });*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    telephonyWorkRequest();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ACCESS_COARSE_LOCATION);
                }
                break;

            }
        }
    }



    private void showText(String message){
        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void sendToServer(String accessToken,String qrString) {
        String url = getString(R.string.web_auth_url);

        JSONObject params = new JSONObject();
        try {
            params.put("uuid", qrString);
            params.put("access_token", accessToken);
            StringEntity entity = new StringEntity(params.toString());

            AsyncHttpClient client = new AsyncHttpClient();
            client.post(this, url, entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Log.d(TAG, statusCode + " OK");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d(TAG, statusCode + " NOT OK");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


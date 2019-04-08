package cl.niclabs.adkintunmobile.views.dashboard;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
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

        WorkManager.getInstance().getWorkInfosByTagLiveData(TelephonyMonitor.SLAVE_WORKER)
                .observe(this, new Observer<List<WorkInfo>>() {

                    @Override
                    public void onChanged(@Nullable List<WorkInfo> workInfos) {
                        if (workInfos != null) {
                            if (!workInfos.isEmpty()) {
                                for (WorkInfo workInfo : workInfos) {
                                    if (workInfo != null && workInfo.getState().isFinished()) {
                                        createDynamicTextView();
                                        setTextViews(workInfo.getOutputData());
                                    }
                                }
                            }
                        }
                    }
                });

    }

    public void createDynamicTextView() {
        TableLayout layout = this.findViewById(R.id.drawer_layout);

        TableLayout.LayoutParams lparams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(this);
        tv.setLayoutParams(lparams);
        tv.setText("test");
        layout.addView(tv);
    }
    public void setTextViews(Data data) {
        Map<String, Object> map = data.getKeyValueMap();
        TextView view;
        Log.i("values", map.values().toString());
        for (String key : map.keySet()) {
            view = findViewById(getResources().getIdentifier(key, "id", getPackageName()));
            view.setText((String)map.get(key));
        }
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


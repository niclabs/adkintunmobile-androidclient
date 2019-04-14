package cl.niclabs.adkintunmobile.views.dashboard;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
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
import cl.niclabs.adkintunmobile.workers.PeriodicMeasurementsWorker;
import cl.niclabs.adkintunmobile.views.status.FileSizeDialog;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private final String TAG = "AdkM:MainActivity";
    public static final int MULTIPLE_PERMISSIONS = 10;
    private final String[] permissions = new String[]{
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!BuildConfig.DEBUG_MODE)
            Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        this.context = this;

        showFileSizeDialog();
        if(checkPermissions()) {
            startWork();
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            disableOptimizations(getApplicationContext());
        }
    }

    public void startWork() {
        telephonyWorkRequest();
    }

    /*
    Starts activity which opens the battery optimization settings window so the user can add the app
    to the whitelist.
    This is needed to disable Doze and Standby Mode.
     */
    private void openPowerSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        context.startActivity(intent);
    }

    public void disableOptimizations(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        String packageName = context.getPackageName();
        boolean ignoringOptimizations = powerManager.isIgnoringBatteryOptimizations(packageName);
        if(!ignoringOptimizations) {
            openPowerSettings(context);
        }
    }

    /*
    Shows the number picker so the user can select the file size with which the speed test will run.
     */
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


    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ActivityCompat.checkSelfPermission(getApplicationContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    /*
     TODO: Change the way the last measured values are put on the View. Observing a periodicwork doesnt work as expected
     */

    public void telephonyWorkRequest() {
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(PeriodicMeasurementsWorker.class, 15, TimeUnit.MINUTES)
                .addTag(PeriodicMeasurementsWorker.SLAVE_WORKER)
                .setConstraints(constraints)
                .build();

        final WorkManager workManager = WorkManager.getInstance();
        workManager.enqueueUniquePeriodicWork(PeriodicMeasurementsWorker.SLAVE_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest);

        workManager.getWorkInfosByTagLiveData(PeriodicMeasurementsWorker.SLAVE_WORKER)
                .observe(this, new Observer<List<WorkInfo>>() {

                    @Override
                    public void onChanged(@Nullable List<WorkInfo> workInfos) {
                        if (workInfos != null) {
                            if (!workInfos.isEmpty()) {
                                for (WorkInfo workInfo : workInfos) {
                                    if (workInfo.getState().equals(WorkInfo.State.ENQUEUED)) {
                                        setTextViews();
                                    }
                                }
                            }
                        }
                    }
                });
    }

    /*
    Method for visualizing the data obtained from the periodic work. Just a temporary method.
     */
    public void setTextViews() {
        String packageName = this.getPackageName();
        String[] tags = new String[] {"TYPE", "DBM", "MCC", "MNC", "CID", "LAC", "TIMESTAMP"};
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String value;
        TextView view;
        int resId;
        for (String tag : tags) {
            resId = this.getResources().getIdentifier(tag, "string", packageName);
            if (tag.equals("TIMESTAMP")) {
                value = sharedPreferences.getString(this.getString(resId), "0");
                if (value.contains(" ")) {
                    value = value.split(" ")[1];
                }
                value = tag + ": " + value;
            } else {
                value = tag + ": " + sharedPreferences.getString(this.getString(resId), "0");
            }
            view = findViewById(getResources().getIdentifier(tag, "id", getPackageName()));
            view.setText(value);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    List<String> permissionsDenied = new ArrayList<>();
                    for (int i = 0; i < permissionsList.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            permissionsDenied.add(permissionsList[i]);
                        }
                    }
                    if (!permissionsDenied.isEmpty()) {
                        ActivityCompat.requestPermissions(this, permissionsDenied.toArray(new String[0]), MULTIPLE_PERMISSIONS);
                    } else {
                        startWork();
                    }
                }  else {
                    ActivityCompat.requestPermissions(this, permissions, MULTIPLE_PERMISSIONS);
                }
            }
        }
    }
}


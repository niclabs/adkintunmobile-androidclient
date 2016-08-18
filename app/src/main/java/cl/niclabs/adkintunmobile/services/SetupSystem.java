package cl.niclabs.adkintunmobile.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.GsmObservationWrapper;
import cl.niclabs.adkintunmobile.services.monitors.ConnectivityMonitor;
import cl.niclabs.adkintunmobile.services.monitors.TelephonyMonitor;
import cl.niclabs.adkintunmobile.services.monitors.TrafficMonitor;
import cl.niclabs.adkintunmobile.services.sync.SynchronizationBroadcastReceiver;
import cl.niclabs.adkintunmobile.utils.files.FileManager;
import cl.niclabs.adkmobile.monitor.Device;
import cl.niclabs.adkmobile.monitor.data.constants.ConnectionType;
import cl.niclabs.adkmobile.monitor.data.constants.NetworkType;
import cl.niclabs.android.utils.Time;

public class SetupSystem extends Device {

    static private final String TAG = "AdkM:SetupSystem";

    @Override
    public void onBootCompleted(Context context) {
        Log.d(TAG, "onBootCompleted");
        super.onBootCompleted(context);
        startUpSystem(context);
    }

    @Override
    public void onShutdown(Context context) {
        Log.d(TAG, "onShutdown");
        super.onShutdown(context);
        bootOffSystem(context);
    }

    /*
     * Static methods for service settings and timers
     */

    static public void startUpSystem(Context context) {
        // Start Monitoring Services
        SetupSystem.startMonitoringServices(context);

        // Start Broadcast Receivers
        SetupSystem.schedulleBroadcastReceivers(context);

        // Register preferences
        SetupSystem.setAppVersionCode(context);
    }

    static public void startMonitoringServices(Context context) {
        if (!ConnectivityMonitor.isRunning())
            context.startService(new Intent(context, ConnectivityMonitor.class));
        if (!TelephonyMonitor.isRunning())
            context.startService(new Intent(context, TelephonyMonitor.class));
        if (!TrafficMonitor.isRunning())
            context.startService(new Intent(context, TrafficMonitor.class));
        Log.d(TAG, "Monitores Iniciados");
    }

    private void bootOffSystem(Context context) {
        if (ConnectivityMonitor.isRunning())
            context.stopService(new Intent(context, ConnectivityMonitor.class));
        if (TelephonyMonitor.isRunning())
            context.stopService(new Intent(context, TelephonyMonitor.class));
        if (TrafficMonitor.isRunning())
            context.stopService(new Intent(context, TrafficMonitor.class));
        Log.d(TAG, "Monitores Detenidos");

        ConnectivityObservationWrapper shutDownConnectivityObservation = new ConnectivityObservationWrapper();
        shutDownConnectivityObservation.timestamp = Time.currentTimeMillis();
        shutDownConnectivityObservation.connectionType = ConnectionType.NONE.value();
        shutDownConnectivityObservation.save();

        GsmObservationWrapper shutDownGsmObservation = new GsmObservationWrapper();
        shutDownGsmObservation.timestamp = Time.currentTimeMillis();
        shutDownGsmObservation.networkType = NetworkType.UNKNOWN.value();
        shutDownGsmObservation.save();
    }

    static public void schedulleBroadcastReceivers(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long sampleRepeatTime = Long.parseLong(sharedPreferences.getString(context.getString(R.string.settings_sampling_frequency_key), "1"));

        SynchronizationBroadcastReceiver.setSchedule(context, sampleRepeatTime);
    }

    static public void setAppVersionCode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String currentAppVersion = sharedPreferences.getString(context.getString(R.string.settings_app_version_key), packageInfo.versionName);

        Log.d(TAG,"current saved: " + currentAppVersion + ", current build: " + packageInfo.versionName);
        if (!currentAppVersion.equals(packageInfo.versionName)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(R.string.settings_app_version_key), packageInfo.versionName);
            editor.apply();
            Log.d(TAG,"app version updated");
            /* Al cambio de version, borrar reportes aun no mandados */
            FileManager.deleteStoredReports(context);
        }else{
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(context.getString(R.string.settings_app_version_key), packageInfo.versionName);
            editor.apply();
        }

    }


}

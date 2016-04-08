package cl.niclabs.adkintunmobile.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.services.monitors.ConnectivityMonitor;
import cl.niclabs.adkintunmobile.services.monitors.TelephonyMonitor;
import cl.niclabs.adkintunmobile.services.monitors.TrafficMonitor;
import cl.niclabs.adkintunmobile.services.sync.SynchronizationBroadcastReceiver;
import cl.niclabs.adkmobile.monitor.Device;

public class SetupSystem extends Device {

    static private final String TAG = "AdkM:SetupSystem";

    @Override
    public void onBootCompleted(Context context) {
        super.onBootCompleted(context);
        startUpSystem(context);
    }

    @Override
    public void onShutdown(Context context) {
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

        Log.d(TAG, "Sistemas reconfigurados");
    }

    static public void startMonitoringServices(Context context) {
        if (!ConnectivityMonitor.isRunning())
            context.startService(new Intent(context, ConnectivityMonitor.class));
        if (!TelephonyMonitor.isRunning())
            context.startService(new Intent(context, TelephonyMonitor.class));
        if (!TrafficMonitor.isRunning())
            context.startService(new Intent(context, TrafficMonitor.class));
    }

    private void bootOffSystem(Context context) {
        if (ConnectivityMonitor.isRunning())
            context.stopService(new Intent(context, ConnectivityMonitor.class));
        if (TelephonyMonitor.isRunning())
            context.stopService(new Intent(context, TelephonyMonitor.class));
        if (TrafficMonitor.isRunning())
            context.stopService(new Intent(context, TrafficMonitor.class));
    }

    static public void schedulleBroadcastReceivers(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long sampleRepeatTime = Long.parseLong(sharedPreferences.getString(context.getString(R.string.settings_sampling_frequency_key), "1"));

        SynchronizationBroadcastReceiver sync = new SynchronizationBroadcastReceiver();
        sync.setSchedule(context, sampleRepeatTime);
    }

    static public void setAppVersionCode(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.settings_app_version_key), context.getString(R.string.settings_app_version_default));
        editor.apply();
    }


}

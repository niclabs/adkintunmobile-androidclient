package cl.niclabs.adkintunmobile.services;

import android.content.Context;
import android.content.Intent;

import cl.niclabs.adkintunmobile.services.monitors.ConnectivityMonitor;
import cl.niclabs.adkintunmobile.services.monitors.TelephonyMonitor;
import cl.niclabs.adkintunmobile.services.monitors.TrafficMonitor;
import cl.niclabs.adkintunmobile.services.sync.SynchronizationBroadcastReceiver;
import cl.niclabs.adkmobile.monitor.Device;

public class StartUp extends Device{
    @Override
    public void onBootCompleted(Context context) {
        super.onBootCompleted(context);
        bootOnSystem(context);
    }

    @Override
    public void onShutdown(Context context) {
        super.onShutdown(context);
        bootOffSystem(context);
    }

    /*
     * Static methods for service settings and timers
     */

    static public void bootOnSystem(Context context){

        // Start Monitoring Services
        StartUp.startMonitoringServices(context);

        // Start Broadcast Receivers
        StartUp.schedulleBroadcastReceivers(context);
    }

    static public void startMonitoringServices(Context context){
        if(!ConnectivityMonitor.isRunning())
            context.startService(new Intent(context, ConnectivityMonitor.class));
        if(!TelephonyMonitor.isRunning())
            context.startService(new Intent(context, TelephonyMonitor.class));
        if(!TrafficMonitor.isRunning())
            context.startService(new Intent(context, TrafficMonitor.class));
    }

    private void bootOffSystem(Context context) {
        if(ConnectivityMonitor.isRunning())
            context.stopService(new Intent(context, ConnectivityMonitor.class));
        if(TelephonyMonitor.isRunning())
            context.stopService(new Intent(context, TelephonyMonitor.class));
        if(TrafficMonitor.isRunning())
            context.stopService(new Intent(context, TrafficMonitor.class));
    }

    static public void schedulleBroadcastReceivers(Context context) {
        SynchronizationBroadcastReceiver sync = new SynchronizationBroadcastReceiver();
        sync.setSchedulle(context);
    }



}

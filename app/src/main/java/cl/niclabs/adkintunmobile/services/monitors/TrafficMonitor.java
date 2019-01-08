package cl.niclabs.adkintunmobile.services.monitors;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cl.niclabs.adkintunmobile.data.persistent.TrafficObservationWrapper;
import cl.niclabs.adkintunmobile.adkmobile.monitor.Monitor;
import cl.niclabs.adkintunmobile.adkmobile.monitor.Traffic;
import cl.niclabs.adkintunmobile.adkmobile.monitor.data.TrafficObservation;
import cl.niclabs.adkintunmobile.adkmobile.monitor.listeners.TrafficListener;

public class TrafficMonitor extends Service implements TrafficListener {

    private final String TAG = "AdkM:TrafficMonitor";

    private static boolean running = false;
    private Monitor.Controller<TrafficListener> trafficController;
    private Gson gson;

    public TrafficMonitor() {
    }

    public static boolean isRunning() {
        return running;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.gson = new GsonBuilder().create();
    }

    @Override
    public void onDestroy() {
        stopMonitor();
        super.onDestroy();
        this.running = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startMonitor();
        this.running = true;
        return START_STICKY;
    }

    public void startMonitor() {
        this.trafficController = Traffic.bind(Traffic.class, this);
        this.trafficController.listen(this, true);
        // set sample frequency
        Bundle bundle = new Bundle();
        bundle.putInt(Traffic.TRAFFIC_UPDATE_INTERVAL_EXTRA, 20);
        this.trafficController.activate(Monitor.TRAFFIC_APPLICATION, bundle);
        //this.trafficController.activate(Monitor.TRAFFIC_WIFI | Monitor.TRAFFIC_MOBILE | Monitor.TRAFFIC_APPLICATION, bundle);
        //this.trafficController.activate(Monitor.TRAFFIC_WIFI | Monitor.TRAFFIC_MOBILE | Monitor.TRAFFIC_APPLICATION);
    }

    public void stopMonitor() {
        this.trafficController.unbind();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     *  Implementation of event reporting methods provided by the monitoring library
     */
    @Override
    public void onMobileTrafficChange(TrafficObservation trafficState) {
        TrafficObservationWrapper sample = this.gson.fromJson(trafficState.toString(), TrafficObservationWrapper.class);
        sample.save();
    }

    @Override
    public void onWiFiTrafficChange(TrafficObservation trafficState) {
        TrafficObservationWrapper sample = this.gson.fromJson(trafficState.toString(), TrafficObservationWrapper.class);
        sample.save();
    }

    @Override
    public void onApplicationTrafficChange(TrafficObservation trafficState) {
        TrafficObservationWrapper sample = this.gson.fromJson(trafficState.toString(), TrafficObservationWrapper.class);
        sample.packageName = getPackageManager().getNameForUid(trafficState.getUid());
        sample.save();
    }
}

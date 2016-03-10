package cl.niclabs.adkintunmobile.services.monitors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkmobile.monitor.Connectivity;
import cl.niclabs.adkmobile.monitor.Monitor;
import cl.niclabs.adkmobile.monitor.data.ConnectivityObservation;
import cl.niclabs.adkmobile.monitor.listeners.ConnectivityListener;

public class ConnectivityMonitor extends Service implements ConnectivityListener{

    private static boolean running = false;
    private Monitor.Controller<ConnectivityListener> connController;
    private Gson gson;

    public ConnectivityMonitor() {
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

    private void startMonitor(){
        this.connController = Connectivity.bind(Connectivity.class, this);
        connController.listen(this, true);
        connController.activate(Monitor.CONNECTIVITY);
    }

    private void stopMonitor(){
        connController.unbind();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     *  Implementation of event reporting methods provided by the monitoring library
     */
    @Override
    public void onConnectivityChange(ConnectivityObservation connectivityState) {
        ConnectivityObservationWrapper sample = this.gson.fromJson(connectivityState.toString(), ConnectivityObservationWrapper.class);
        sample.save();
    }
}

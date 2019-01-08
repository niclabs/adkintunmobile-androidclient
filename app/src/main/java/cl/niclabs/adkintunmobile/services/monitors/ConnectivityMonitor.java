package cl.niclabs.adkintunmobile.services.monitors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkintunmobile.adkmobile.monitor.Connectivity;
import cl.niclabs.adkintunmobile.adkmobile.monitor.Monitor;
import cl.niclabs.adkintunmobile.adkmobile.monitor.data.ConnectivityObservation;
import cl.niclabs.adkintunmobile.adkmobile.monitor.listeners.ConnectivityListener;

public class ConnectivityMonitor extends Service implements ConnectivityListener {

    private final String TAG = "AdkM:ConnectivityMonitor";

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

    private void startMonitor() {
        this.connController = Connectivity.bind(Connectivity.class, this);
        this.connController.listen(this, true);
        this.connController.activate(Monitor.CONNECTIVITY);
    }

    private void stopMonitor() {
        this.connController.unbind();
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

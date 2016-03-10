package cl.niclabs.adkintunmobile.services.monitors;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cl.niclabs.adkintunmobile.data.persistent.GsmObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.StateChangeWrapper;
import cl.niclabs.adkintunmobile.data.persistent.TelephonyObservationWrapper;
import cl.niclabs.adkmobile.monitor.Monitor;
import cl.niclabs.adkmobile.monitor.Telephony;
import cl.niclabs.adkmobile.monitor.data.StateChange;
import cl.niclabs.adkmobile.monitor.data.TelephonyObservation;
import cl.niclabs.adkmobile.monitor.listeners.TelephonyListener;

public class TelephonyMonitor extends Service implements TelephonyListener {

    private static boolean running = false;
    private Monitor.Controller<TelephonyListener> phoneController;
    private Gson gson;

    public TelephonyMonitor() {
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
        this.phoneController = Telephony.bind(Telephony.class, this);
        this.phoneController.listen(this, true);
        this.phoneController.activate(Monitor.TELEPHONY);
    }

    private void stopMonitor(){
        this.phoneController.unbind();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     *  Implementation of event reporting methods provided by the monitoring library
     */
    // TODO: El evento recibido podría no ser un GSM, sino un CDMA. Se debe verificar
    @Override
    public void onMobileTelephonyChange(TelephonyObservation<?> telephonyState) {
        GsmObservationWrapper sample = this.gson.fromJson(telephonyState.toString(), GsmObservationWrapper.class);
        sample.save();
    }

    @Override
    public void onSimStateChange(StateChange stateChange) {
        StateChangeWrapper sample = this.gson.fromJson(stateChange.toString(), StateChangeWrapper.class);
        sample.save();
    }

    @Override
    public void onServiceStateChange(StateChange stateChange) {
        StateChangeWrapper sample = this.gson.fromJson(stateChange.toString(), StateChangeWrapper.class);
        sample.save();
    }

    @Override
    public void onDataConnectionStateChange(StateChange stateChange) {
        StateChangeWrapper sample = this.gson.fromJson(stateChange.toString(), StateChangeWrapper.class);
        sample.save();
    }

    @Override
    public void onAirplaneModeChange(StateChange stateChange) {
        StateChangeWrapper sample = this.gson.fromJson(stateChange.toString(), StateChangeWrapper.class);
        sample.save();
    }
}

package cl.niclabs.adkintunmobile.services.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import cl.niclabs.adkintunmobile.data.Report;
import cl.niclabs.adkintunmobile.utils.compression.CompressionUtils;

public class Synchronization extends Service {

    private final String TAG = "AdkM:Synchronization";
    private Context context;

    private final String SYNC_INTENT = "cl.niclabs.adkintunmobile.intent.DISPATCHER_DATA";

    public Synchronization() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        Log.d(this.TAG, "Creado El servicio de sincronización");

        // 1.- Build a report
        Report report = new Report(getApplicationContext());

        if (report.recordsToSend()){
            // 2.- Save report
            report.saveFile(context, CompressionUtils.CompressionType.GZIP);
            // 3.- Backup visualization data
            report.saveVisualSamples();
            // 4.- Clean DB
            report.cleanDBRecords();
        }
        // 5.- Intent send data
        runIntentSendData();
        // 6.- StopService
        stopSelf();
    }

    private void runIntentSendData() {
        Intent intent = new Intent();
        intent.setAction(SYNC_INTENT);
        context.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(this.TAG, "Detenido El servicio de sincronización");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

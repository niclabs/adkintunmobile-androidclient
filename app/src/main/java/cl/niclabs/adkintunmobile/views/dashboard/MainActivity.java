package cl.niclabs.adkintunmobile.views.dashboard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.CdmaObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.GsmObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.StateChangeWrapper;
import cl.niclabs.adkintunmobile.data.persistent.TelephonyObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.TrafficObservationWrapper;
import cl.niclabs.adkintunmobile.services.StartUp;
import cl.niclabs.adkintunmobile.services.sync.SynchronizationBroadcastReceiver;
import cl.niclabs.adkintunmobile.services.monitors.ConnectivityMonitor;
import cl.niclabs.adkintunmobile.services.monitors.TelephonyMonitor;
import cl.niclabs.adkintunmobile.services.monitors.TrafficMonitor;

public class MainActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        // Start System
        StartUp.bootOnSystem(this.context);
    }
}
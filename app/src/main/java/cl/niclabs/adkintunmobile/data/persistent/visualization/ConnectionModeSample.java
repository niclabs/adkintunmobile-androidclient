package cl.niclabs.adkintunmobile.data.persistent.visualization;

import android.util.Log;

import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkmobile.monitor.data.constants.ConnectionType;

public class ConnectionModeSample extends ConnectionTypeSample {
    public final static int NONE = 0;
    public final static int MOBILE = 1;
    public final static int WIFI = 2;

    public ConnectionModeSample(ConnectivityObservationWrapper observation) {
        ConnectionType connectionType = ConnectionType.getInstance(observation.connectionType);
        Log.d("Connection", observation.connectionType+"");
        if (observation.connected){
            if (connectionType.isMobile()) {
                type = MOBILE;
                Log.d("Color", "mobile");
            }
            else {
                type = WIFI;
                Log.d("Color", "wifi");
            }
        }
        else {
            type = NONE;
            Log.d("Color", "none");

        }
        initialTime = observation.timestamp;

        date = DailyConnectionModeSummary.getSummary(initialTime);
    }

    public ConnectionModeSample(){}
}


package cl.niclabs.adkintunmobile.data.persistent.visualization;

import android.util.Log;

import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkmobile.monitor.data.constants.ConnectionType;
import cl.niclabs.android.data.Persistent;

public class ConnectionTypeSample extends Persistent<ConnectionTypeSample> {
    public final static int NONE = 0;
    public final static int MOBILE = 1;
    public final static int WIFI = 2;

    private int type;
    private long initialTime;
    private DailyConnectionTypeSummary date;

    public ConnectionTypeSample(ConnectivityObservationWrapper observation) {
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

        date = DailyConnectionTypeSummary.getSummary(initialTime);
    }

    public ConnectionTypeSample(){}

    public long getInitialTime(){
        return initialTime;
    }

    public int getType(){
        return type;
    }
}


package cl.niclabs.adkintunmobile.data.persistent.visualization;

import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkmobile.monitor.data.constants.ConnectionType;

public class ConnectionModeSample extends ConnectionTypeSample {

    public final static int NONE = 0;
    public final static int MOBILE = 1;
    public final static int WIFI = 2;

    public ConnectionModeSample(ConnectivityObservationWrapper observation) {
        ConnectionType connectionType = ConnectionType.getInstance(observation.connectionType);
        if (observation.connected){
            if (connectionType.isMobile()) {
                type = MOBILE;
            }
            else {
                type = WIFI;
            }
        }
        else {
            type = NONE;
        }
        initialTime = observation.timestamp;
        date = DailyConnectionModeSummary.getSummary(initialTime);
    }

    public ConnectionModeSample(){}
}


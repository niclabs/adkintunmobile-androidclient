package cl.niclabs.adkintunmobile.data.persistent.visualization;

import cl.niclabs.adkintunmobile.data.persistent.GsmObservationWrapper;
import cl.niclabs.adkmobile.monitor.data.constants.NetworkType;
import cl.niclabs.android.data.Persistent;

/**
 * Created by diego on 27-04-16.
 */
public class NetworkTypeSample extends ConnectionTypeSample{

    public final static int UNKNOWN = 0;
    public final static int TYPE_G = 1;
    public final static int TYPE_E = 2;
    public final static int TYPE_3G = 3;
    public final static int TYPE_H = 4;
    public final static int TYPE_Hp = 5;
    public final static int TYPE_4G = 6;

    public NetworkTypeSample(GsmObservationWrapper observation) {
        NetworkType networkType = NetworkType.getInstance(observation.networkType);
        switch (networkType.value()) {

            case 1: // 1xRTT 2.5
            case 2: // CDMA 2.5
            case 8: // GPRS 2
            case 13: // IDEN 2
                type = TYPE_G;
                break;

            case 3: // EDGE 2.5
                type = TYPE_E;
                break;

            case 5: // EVDO_0 3
            case 6: // EVDO_A 3
            case 7: // EVDO_B 3
            case 4: // EHRPD ??
            case 15: // UMTS 3
                type = TYPE_3G;
                break;

            case 9: // HSDPA 3.5
            case 10: // HSPA 3
            case 12: // HSUPA 3
                type = TYPE_H;
                break;

            case 11: // HSPAP 4 ?
                type = TYPE_Hp;
                break;

            case 14: // LTE 4
                type = TYPE_4G;
                break;

            case 0: // OTHER
            case 16: // UNKNOWN
                type = UNKNOWN;
        }

        initialTime = observation.timestamp;
        date = DailyNetworkTypeSummary.getSummary(initialTime);
    }

    public NetworkTypeSample(){}
}


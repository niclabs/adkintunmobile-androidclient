package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.android.data.Persistent;

public class ActiveMeasurement extends Persistent<ActiveMeasurement> {

    @SerializedName("network_interface")
    public NetworkInterface networkInterface;
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("sent")
    public boolean sent;

    public ActiveMeasurement(Context context) {
        super();
        this.sent = false;
        this.timestamp = System.currentTimeMillis();

        if(Network.getActiveNetwork(context) == ConnectionModeSample.MOBILE){
            this.networkInterface = new MobileInterface(context);
        }else if(Network.getActiveNetwork(context) == ConnectionModeSample.WIFI){
            this.networkInterface = new WifiInterface(context);
        }else {

        }
    }
}

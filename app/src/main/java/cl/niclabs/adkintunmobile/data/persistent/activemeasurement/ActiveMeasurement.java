package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public class ActiveMeasurement extends Persistent<ActiveMeasurement> {

    @SerializedName("network_interface")
    public NetworkInterface networkInterface;
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("sent")
    public boolean sent;

    public ActiveMeasurement() {
        super();
        this.sent = false;
        this.timestamp = System.currentTimeMillis();
    }

    public void setUpReport(Context context){
        this.networkInterface = new NetworkInterface();
        this.networkInterface.setUpInterface(context);
    }

    @Override
    public void save() {
        this.networkInterface.save();
        super.save();
    }
}

package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public class ActiveMeasurement extends Persistent<ActiveMeasurement> {

    @SerializedName("network_interface")
    public NetworkInterface networkInterface;
    @SerializedName("timestamp")
    public long timestamp;
}

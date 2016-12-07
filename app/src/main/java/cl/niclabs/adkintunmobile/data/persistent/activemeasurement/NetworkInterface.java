package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public abstract class NetworkInterface extends Persistent<NetworkInterface> {

    @SerializedName("active_interface")
    public int activeInterface;

    public NetworkInterface() {
    }
}

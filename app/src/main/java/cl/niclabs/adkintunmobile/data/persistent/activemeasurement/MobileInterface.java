package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.net.ConnectivityManager;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;

public class MobileInterface extends NetworkInterface {

    @SerializedName("gsm_cid")
    public int gsmCid;
    @SerializedName("gsm_lac")
    public int gsmLac;
    @SerializedName("network_type")
    public int networkType;

    public MobileInterface() {
        this.activeInterface = ApplicationTraffic.MOBILE;
    }
}

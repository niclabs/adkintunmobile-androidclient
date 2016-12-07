package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.content.Context;
import android.net.ConnectivityManager;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class MobileInterface extends NetworkInterface {

    @SerializedName("gsm_cid")
    public int gsmCid;
    @SerializedName("gsm_lac")
    public int gsmLac;
    @SerializedName("network_type")
    public int networkType;

    public MobileInterface(Context context) {
        super();
        this.activeInterface = ApplicationTraffic.MOBILE;
        this.networkType = Network.getINTNetworkType(context);
        //TODO: LAC y CID
        int laccid[] = Network.getLacCid(context);
        this.gsmLac = laccid[0];
        this.gsmCid = laccid[1];
    }
}

package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;
import cl.niclabs.adkintunmobile.utils.information.Network;
import cl.niclabs.adkintunmobile.commons.data.Persistent;

public class NetworkInterface extends Persistent<NetworkInterface> {

    @SerializedName("active_interface")
    public int activeInterface;

    @SerializedName("ssid")
    public String ssid;
    @SerializedName("bssid")
    public String bssid;

    @SerializedName("gsm_cid")
    public int gsmCid;
    @SerializedName("gsm_lac")
    public int gsmLac;
    @SerializedName("network_type")
    public int networkType;

    public NetworkInterface() {
    }

    public void setUpInterface(Context context){
        this.activeInterface = Network.getActiveNetwork(context);

        if(this.activeInterface == ConnectionModeSample.MOBILE){
            setUpMobileInterface(context);
        }else if(this.activeInterface == ConnectionModeSample.WIFI){
            setUpWifiInterface(context);
        }else {
            //No se guardaría nada
        }
    }

    public void setUpWifiInterface(Context context) {
        this.ssid = Network.getWifiSSID(context);
        this.bssid = Network.getWifiBSSID(context);
    }

    public void setUpMobileInterface(Context context) {
        this.networkType = Network.getINTNetworkType(context);
        this.gsmLac = Network.getLacCid(context)[0];
        this.gsmCid = Network.getLacCid(context)[1];
    }
}

package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.utils.information.Network;

public class WifiInterface extends NetworkInterface {

    @SerializedName("ssid")
    public String ssid;
    @SerializedName("bssid")
    public String bssid;
    /**
     * TODO: Get MAC Adress of access point
    @SerializedName("mac")
    public String mac;
    **/

    public WifiInterface(Context context) {
        super();
        this.activeInterface = ApplicationTraffic.WIFI;
        this.ssid = Network.getWifiSSID(context);
        this.bssid = Network.getWifiBSSID(context);
    }
}

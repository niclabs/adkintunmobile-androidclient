package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;

public class WifiInterface extends NetworkInterface {

    @SerializedName("ssid")
    public String ssid;
    @SerializedName("mac")
    public String mac;

    public WifiInterface() {
        this.activeInterface = ApplicationTraffic.WIFI;
    }
}

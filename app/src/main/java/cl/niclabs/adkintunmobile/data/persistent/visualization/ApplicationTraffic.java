package cl.niclabs.adkintunmobile.data.persistent.visualization;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.data.persistent.TrafficObservationWrapper;
import cl.niclabs.android.data.Persistent;

public class ApplicationTraffic extends Persistent<ApplicationTraffic>{

    public final static int MOBILE = 1;
    public final static int WIFI = 6;

    @SerializedName("uid")
    public Integer uid;
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("network_type")
    public int networkType;
    @SerializedName("rx_bytes")
    public long rxBytes;
    @SerializedName("tx_bytes")
    public long txBytes;
    @SerializedName("tcp_rx_bytes")
    public Long tcpRxBytes;
    @SerializedName("tcp_tx_bytes")
    public Long tcpTxBytes;

    public ApplicationTraffic() {

    }

    public ApplicationTraffic(TrafficObservationWrapper trafficObservationWrapper){
        this.uid = trafficObservationWrapper.uid;
        this.timestamp = trafficObservationWrapper.timestamp;
        this.networkType = trafficObservationWrapper.networkType;
        this.rxBytes = trafficObservationWrapper.rxBytes;
        this.txBytes = trafficObservationWrapper.txBytes;
        this.tcpRxBytes = trafficObservationWrapper.tcpRxBytes;
        this.tcpTxBytes = trafficObservationWrapper.tcpTxBytes;
    }
}

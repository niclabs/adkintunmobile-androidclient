package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.commons.data.Persistent;

public class TrafficObservationWrapper extends Persistent<TrafficObservationWrapper>{

    @SerializedName("network_type")
    public int networkType;
    @SerializedName("rx_bytes")
    public long rxBytes;
    @SerializedName("rx_packets")
    public Long rxPackets;

    @SerializedName("tcp_rx_bytes")
    public Long tcpRxBytes;
    @SerializedName("tcp_tx_bytes")
    public Long tcpTxBytes;

    @SerializedName("tx_bytes")
    public long txBytes;
    @SerializedName("tx_packets")
    public Long txPackets;

    @SerializedName("uid")
    public Integer uid;

    @SerializedName("event_type")
    public int eventType;
    @SerializedName("timestamp")
    public long timestamp;


    // Our attribute for save the package name
    @SerializedName("package_name")
    public String packageName;

    public TrafficObservationWrapper() {
    }
}

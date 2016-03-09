package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public class TelephonyObservationWrapper extends Persistent<TelephonyObservationWrapper>{

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

    public TelephonyObservationWrapper() {
    }
}

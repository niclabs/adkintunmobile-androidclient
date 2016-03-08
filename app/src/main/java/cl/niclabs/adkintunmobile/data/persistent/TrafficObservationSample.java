package cl.niclabs.adkintunmobile.data.persistent;

import cl.niclabs.adkmobile.monitor.data.TrafficObservation;
import cl.niclabs.android.data.Persistent;

public class TrafficObservationSample extends Persistent<TrafficObservation>{

    private int networkType;
    private long rxBytes;
    private Long rxPackets;

    private Long tcpRxBytes;
    private Long tcpRxSegments;
    private Long tcpTxBytes;
    private Long tcpTxSegments;

    private long txBytes;
    private Long txPackets;

    private Integer uid;

    public TrafficObservationSample() {
    }

    public TrafficObservationSample(TrafficObservation trafficObservation) {
        this.setNetworkType(trafficObservation.getNetworkType());
        this.setRxBytes(trafficObservation.getRxBytes());
        this.setRxPackets(trafficObservation.getRxPackets());

        this.setTcpRxBytes(trafficObservation.getTcpRxBytes());
        this.setTcpRxSegments(trafficObservation.getTcpRxSegments());
        this.setTcpTxBytes(trafficObservation.getTcpTxBytes());
        this.setTcpTxSegments(trafficObservation.getTcpTxSegments());

        this.setTxBytes(trafficObservation.getTxBytes());
        this.setTxPackets(trafficObservation.getTxPackets());
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public long getRxBytes() {
        return rxBytes;
    }

    public void setRxBytes(long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public Long getRxPackets() {
        return rxPackets;
    }

    public void setRxPackets(Long rxPackets) {
        this.rxPackets = rxPackets;
    }

    public Long getTcpRxBytes() {
        return tcpRxBytes;
    }

    public void setTcpRxBytes(Long tcpRxBytes) {
        this.tcpRxBytes = tcpRxBytes;
    }

    public Long getTcpRxSegments() {
        return tcpRxSegments;
    }

    public void setTcpRxSegments(Long tcpRxSegments) {
        this.tcpRxSegments = tcpRxSegments;
    }

    public Long getTcpTxBytes() {
        return tcpTxBytes;
    }

    public void setTcpTxBytes(Long tcpTxBytes) {
        this.tcpTxBytes = tcpTxBytes;
    }

    public Long getTcpTxSegments() {
        return tcpTxSegments;
    }

    public void setTcpTxSegments(Long tcpTxSegments) {
        this.tcpTxSegments = tcpTxSegments;
    }

    public long getTxBytes() {
        return txBytes;
    }

    public void setTxBytes(long txBytes) {
        this.txBytes = txBytes;
    }

    public Long getTxPackets() {
        return txPackets;
    }

    public void setTxPackets(Long txPackets) {
        this.txPackets = txPackets;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}

package cl.niclabs.adkintunmobile.data.persistent;

import cl.niclabs.adkmobile.monitor.data.ConnectivityObservation;
import cl.niclabs.android.data.Persistent;

public class ConnectivityObservationSample extends Persistent<ConnectivityObservationSample>{

    private int detailedState;
    private boolean available;
    private boolean connected;
    private boolean roaming;
    private int connectionType;
    private Integer connectionTypeOther;

    public ConnectivityObservationSample() {
    }

    public ConnectivityObservationSample(ConnectivityObservation connectivityObservation) {
        this.setDetailedState(connectivityObservation.getDetailedState().value());
        this.setAvailable(connectivityObservation.isAvailable());
        this.setConnected(connectivityObservation.isConnected());
        this.setRoaming(connectivityObservation.isRoaming());
        this.setConnectionType(connectivityObservation.getConnectionType().value());
        this.setConnectionTypeOther(connectivityObservation.getConnectionTypeOther());
    }

    public int getDetailedState() {
        return detailedState;
    }

    public void setDetailedState(int detailedState) {
        this.detailedState = detailedState;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }

    public int getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }

    public Integer getConnectionTypeOther() {
        return connectionTypeOther;
    }

    public void setConnectionTypeOther(Integer connectionTypeOther) {
        this.connectionTypeOther = connectionTypeOther;
    }
}

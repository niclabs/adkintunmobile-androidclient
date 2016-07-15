package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public class ConnectivityObservationWrapper extends Persistent<ConnectivityObservationWrapper>{

    @SerializedName("detailed_state")
    public int detailedState;
    @SerializedName("available")
    public boolean available;
    @SerializedName("connected")
    public boolean connected;
    @SerializedName("roaming")
    public boolean roaming;
    @SerializedName("connection_type")
    public int connectionType;
    @SerializedName("connection_type_other")
    public Integer connectionTypeOther;

    @SerializedName("event_type")
    public int eventType;
    @SerializedName("timestamp")
    public long timestamp;

    public ConnectivityObservationWrapper() {
    }
}

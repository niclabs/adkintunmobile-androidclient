package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public class StateChangeWrapper extends Persistent<StateChangeWrapper>{

    @SerializedName("state")
    public int state;
    @SerializedName("state_type")
    public Integer stateType;

    @SerializedName("event_type")
    public int eventType;
    @SerializedName("timestamp")
    public long timestamp;

    public StateChangeWrapper() {
    }
}

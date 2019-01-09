package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.commons.data.Persistent;

public class TelephonyObservationWrapper extends Persistent<TelephonyObservationWrapper>{

    @SerializedName("mcc")
    public int mcc;
    @SerializedName("mnc")
    public int mnc;
    @SerializedName("network_type")
    public int networkType;

    @SerializedName("telephony_standard")
    public int telephonyStandard;

    @SerializedName("signal_strength")
    public SampleWrapper signalStrength;

    @SerializedName("event_type")
    public int eventType;
    @SerializedName("timestamp")
    public long timestamp;

    public TelephonyObservationWrapper() {
    }

    @Override
    public void save() {
        if(this.signalStrength != null)
            this.signalStrength.save();
        super.save();
    }
}

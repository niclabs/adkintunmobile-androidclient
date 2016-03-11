package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

public class CdmaObservationWrapper extends TelephonyObservationWrapper {

    @SerializedName("cdma_base_latitude")
    public int cdmaBaseLatitude;
    @SerializedName("cdma_base_longitude")
    public int cdmaBaseLongitude;
    @SerializedName("cdma_base_station_id")
    public int cdmaBaseStationId;

    @SerializedName("network_id")
    public int networkId;
    @SerializedName("system_id")
    public int systemId;


    @SerializedName("cdma_ecio")
    public SampleWrapper cdmaEcio;
    @SerializedName("evdo_dbm")
    public SampleWrapper evdoDbm;
    @SerializedName("evdo_ecio")
    public SampleWrapper evdoEcio;
    @SerializedName("evdo_snr")
    public SampleWrapper evdoSnr;

    public CdmaObservationWrapper() {
    }

    @Override
    public void save() {
        if(this.cdmaEcio != null)
            this.cdmaEcio.save();
        if(this.evdoDbm != null)
            this.evdoDbm.save();
        if(this.evdoEcio != null)
            this.evdoEcio.save();
        if(this.evdoSnr != null)
            this.evdoSnr.save();
        super.save();
    }
}

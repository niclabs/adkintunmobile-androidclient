package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.niclabs.android.data.DoNotSerialize;

public class GsmObservationWrapper extends TelephonyObservationWrapper{

    @SerializedName("gsm_cid")
    public int gsmCid;
    @SerializedName("gsm_lac")
    public int gsmLac;
    @SerializedName("gsm_psc")
    public Integer gsmPsc;

    @SerializedName("neighbor_list")
    public List<NeighborAntennaWrapper> neighborList;

    @SerializedName("signal_ber")
    @DoNotSerialize
    public SampleWrapper signalBer;

    public GsmObservationWrapper() {
    }

    @Override
    public void save() {
        if(this.signalBer != null)
            this.signalBer.save();
        super.save();
    }

    public boolean sameAntenna(GsmObservationWrapper sample){
        return this.gsmCid == sample.gsmCid
                && this.gsmLac == sample.gsmLac
                && this.gsmPsc == sample.gsmPsc;
    }
}

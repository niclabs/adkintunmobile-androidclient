package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    public SampleWrapper signalBer;

    public GsmObservationWrapper() {
    }

    // TODO: Hacer una rutina para guardar la neighbor_list en caso de venir como dato
    @Override
    public void save() {
        if(this.signalBer != null)
            this.signalBer.save();
        super.save();
    }
}

package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.adkintunmobile.commons.data.Persistent;

public class NeighborAntennaWrapper extends Persistent<NeighborAntennaWrapper>{

    @SerializedName("gsm_cid")
    public int gsmCid;
    @SerializedName("gsm_lac")
    public int gsmLac;
    @SerializedName("gsm_psc")
    public int gsmPsc;

    @SerializedName("signal_strength")
    public Integer signalStrength;

    public NeighborAntennaWrapper() {
    }
}

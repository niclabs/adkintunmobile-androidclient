package cl.niclabs.adkintunmobile.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.niclabs.adkintunmobile.data.persistent.CdmaObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.DeviceSingleton;
import cl.niclabs.adkintunmobile.data.persistent.GsmObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.SampleWrapper;
import cl.niclabs.adkintunmobile.data.persistent.SimSingleton;
import cl.niclabs.adkintunmobile.data.persistent.StateChangeWrapper;
import cl.niclabs.adkintunmobile.data.persistent.TelephonyObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.TrafficObservationWrapper;

public class EventsReport {

    // TODO: Agregar Campos para datos del dispositivo
    // TODO: Modificar Nombres de los campos Serializados

    @SerializedName("sim_records")
    public List <SimSingleton> simRecords;
    @SerializedName("device_records")
    public List <DeviceSingleton> deviceRecords;
    @SerializedName("cdma_records")
    public List <CdmaObservationWrapper> cdmaRecords;
    @SerializedName("connectivity_records")
    public List <ConnectivityObservationWrapper> connectivityRecords;
    @SerializedName("gsm_records")
    public List <GsmObservationWrapper> gsmRecords;
    @SerializedName("state_records")
    public List <StateChangeWrapper> stateRecords;
    @SerializedName("telephony_records")
    public List <TelephonyObservationWrapper> telephonyRecords;
    @SerializedName("traffic_records")
    public List <TrafficObservationWrapper> trafficRecords;

    public EventsReport() {
        this.simRecords = SimSingleton.listAll(SimSingleton.class);
        this.deviceRecords = DeviceSingleton.listAll(DeviceSingleton.class);
        this.cdmaRecords = CdmaObservationWrapper.listAll(CdmaObservationWrapper.class);
        this.connectivityRecords = ConnectivityObservationWrapper.listAll(ConnectivityObservationWrapper.class);
        this.gsmRecords = GsmObservationWrapper.listAll(GsmObservationWrapper.class);
        this.stateRecords = StateChangeWrapper.listAll(StateChangeWrapper.class);
        this.telephonyRecords = TelephonyObservationWrapper.listAll(TelephonyObservationWrapper.class);
        this.trafficRecords = TrafficObservationWrapper.listAll(TrafficObservationWrapper.class);
    }

    public void cleanDBRecords(){
        CdmaObservationWrapper.deleteAll(CdmaObservationWrapper.class);
        GsmObservationWrapper.deleteAll(GsmObservationWrapper.class);
        SampleWrapper.deleteAll(SampleWrapper.class);

        ConnectivityObservationWrapper.deleteAll(ConnectivityObservationWrapper.class);
        StateChangeWrapper.deleteAll(StateChangeWrapper.class);
        TelephonyObservationWrapper.deleteAll(TelephonyObservationWrapper.class);
        TrafficObservationWrapper.deleteAll(TrafficObservationWrapper.class);
    }
}

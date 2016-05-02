package cl.niclabs.adkintunmobile.data;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cl.niclabs.adkintunmobile.data.persistent.CdmaObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.GsmObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.SampleWrapper;
import cl.niclabs.adkintunmobile.data.persistent.StateChangeWrapper;
import cl.niclabs.adkintunmobile.data.persistent.TelephonyObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.TrafficObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionTypeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;

/**
 * Clase para manipular los datos de eventos persistidos en el telefono.
 *
 * La creación de un objeto Report rescata todos los valores persistidos en Sugar en el dispositivo
 * de manera que al serializarlo, se genera un JSON con el reporte listo para el servidor.
 * 
 */
public class Report {

    // TODO: Modificar Nombres de los campos Serializados

    @SerializedName("sim_records")
    SimSingleton simRecord;
    @SerializedName("device_records")
    DeviceSingleton deviceRecord;

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

    public Report(Context context) {
        this.simRecord = SimSingleton.getInstance(context);
        this.deviceRecord = DeviceSingleton.getInstance(context);
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

    public void saveVisualSamples(){
        int lastType = -1;
        for (ConnectivityObservationWrapper observation : connectivityRecords){
            ConnectionTypeSample sample = new ConnectionTypeSample(observation);
            if (sample.getType() == lastType)
                continue;
            else {
                sample.save();
                lastType = sample.getType();
            }
        }

        lastType = -1;
        for (GsmObservationWrapper observation : gsmRecords){
            NetworkTypeSample sample = new NetworkTypeSample(observation);
            if (sample.getType() == lastType)
                continue;
            else {
                sample.save();
                lastType = sample.getType();
            }
        }
    }
}

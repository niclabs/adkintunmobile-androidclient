package cl.niclabs.adkintunmobile.data;

import android.content.Context;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.data.persistent.CdmaObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.ConnectivityObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.GsmObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.SampleWrapper;
import cl.niclabs.adkintunmobile.data.persistent.StateChangeWrapper;
import cl.niclabs.adkintunmobile.data.persistent.TelephonyObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.TrafficObservationWrapper;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.ConnectivityTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.MediaTestReport;
import cl.niclabs.adkintunmobile.data.persistent.activemeasurement.SpeedTestReport;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;
import cl.niclabs.adkintunmobile.data.persistent.visualization.ConnectionModeSample;
import cl.niclabs.adkintunmobile.data.persistent.visualization.NetworkTypeSample;
import cl.niclabs.adkintunmobile.utils.compression.CompressionUtils;

/**
 * Clase para manipular los datos de eventos persistidos en el telefono.
 *
 * La creación de un objeto Report rescata todos los valores persistidos en Sugar en el dispositivo
 * de manera que al serializarlo, se genera un JSON con el reporte listo para el servidor.
 * 
 */
public class Report {

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

    @SerializedName("speedtest_records")
    public List <SpeedTestReport> speedTestRecords;
    @SerializedName("mediatest_records")
    public List <MediaTestReport> mediaTestRecords;
    @SerializedName("connectivitytest_records")
    public List <ConnectivityTestReport> connectivityTestRecords;

    public Report(Context context) {
        this.simRecord = SimSingleton.getInstance(context);
        this.deviceRecord = DeviceSingleton.getInstance(context);
        this.cdmaRecords = CdmaObservationWrapper.listAll(CdmaObservationWrapper.class);
        this.stateRecords = StateChangeWrapper.listAll(StateChangeWrapper.class);
        this.telephonyRecords = TelephonyObservationWrapper.listAll(TelephonyObservationWrapper.class);
        this.trafficRecords = TrafficObservationWrapper.listAll(TrafficObservationWrapper.class);
        this.gsmRecords = GsmObservationWrapper.findWithQuery(GsmObservationWrapper.class,
                "SELECT * FROM GSM_OBSERVATION_WRAPPER ORDER BY timestamp ASC");
        this.connectivityRecords = ConnectivityObservationWrapper.findWithQuery(ConnectivityObservationWrapper.class,
                "SELECT * FROM CONNECTIVITY_OBSERVATION_WRAPPER ORDER BY timestamp ASC");

        this.speedTestRecords = SpeedTestReport.getPendingToSendReports();
        this.mediaTestRecords = MediaTestReport.getPendingToSendReports();
        this.connectivityTestRecords = ConnectivityTestReport.getPendingToSendReports();

        cleanRecords();
    }

    /**
     * Prepares the report to be sent.
     *  Saves the last GsmObservation to avoid saving incremental records and remove events with the same timestamp.
     *  Removes ConnectivityObservation events with the same timestamp.
     */
    private void cleanRecords() {
        //Clean gsmRecords
        int gsmRecordsSize = gsmRecords.size();
        if (gsmRecordsSize > 0) {
            GsmObservationWrapper lastObservation = gsmRecords.get(gsmRecordsSize - 1);
            GsmObservationWrapper previousObservation;
            for (int i = gsmRecordsSize - 2; i >= 0; i--) {
                previousObservation = gsmRecords.get(i);
                if (previousObservation.timestamp == lastObservation.timestamp) {
                    gsmRecords.remove(i);                           //Removes gsmRecords with the same timestamp.
                } else
                    lastObservation = previousObservation;
            }
        }

        //Clean connectivityRecords
        int connectivityRecordsSize = connectivityRecords.size();
        if (connectivityRecordsSize > 0) {
            ConnectivityObservationWrapper lastObservation = connectivityRecords.get(connectivityRecordsSize - 1);
            ConnectivityObservationWrapper previousObservation;
            for (int i = connectivityRecordsSize - 2; i >= 0; i--) {
                previousObservation = connectivityRecords.get(i);
                if (previousObservation.timestamp == lastObservation.timestamp) {
                    connectivityRecords.remove(i);                  //Remove connectivityRecords with the same timestamp
                } else
                    lastObservation = previousObservation;
            }
        }
    }

    public boolean recordsToSend(){
        boolean existsRecords = false;

        existsRecords = existsRecords || !this.cdmaRecords.isEmpty();
        existsRecords = existsRecords || !this.connectivityRecords.isEmpty();
        existsRecords = existsRecords || !this.gsmRecords.isEmpty();
        existsRecords = existsRecords || !this.stateRecords.isEmpty();
        existsRecords = existsRecords || !this.telephonyRecords.isEmpty();
        existsRecords = existsRecords || !this.trafficRecords.isEmpty();

        existsRecords = existsRecords || !this.speedTestRecords.isEmpty();
        existsRecords = existsRecords || !this.mediaTestRecords.isEmpty();
        existsRecords = existsRecords || !this.connectivityTestRecords.isEmpty();

        return existsRecords;
    }

    public boolean saveFile(Context context, CompressionUtils.CompressionType compressionType){
        // Store to String
        GsonBuilder builder = new GsonBuilder();
        builder.setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().equals("parentReport");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        Gson gson = builder.create();
        String reportData = gson.toJson(this);

        // Store in local directory
        File outputDir = context.getFilesDir();
        try {
            String filename = context.getString(R.string.synchronization_report_filename);
            filename += "_" + System.currentTimeMillis();
            String fileExtension = context.getString(R.string.synchronization_report_file_extension);

            File outputFile = File.createTempFile(filename, fileExtension, outputDir);
            FileOutputStream outStream = new FileOutputStream(outputFile);
            switch (compressionType){
                case NOCOMPRESSION:
                    outStream.write(reportData.getBytes());
                    break;
                case GZIP:
                    outStream.write(CompressionUtils.gzip(reportData.getBytes()));
                    break;
                case ZIPDEFLATER:
                    outStream.write(CompressionUtils.compress(reportData.getBytes()));
                    break;
                default:                                /* Use no compression */
                    outStream.write(reportData.getBytes());
                    break;

            }
            outStream.flush();
            outStream.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void cleanDBRecords(){
        CdmaObservationWrapper.deleteAll(CdmaObservationWrapper.class);
        GsmObservationWrapper.deleteAll(GsmObservationWrapper.class);
        SampleWrapper.deleteAll(SampleWrapper.class);
        ConnectivityObservationWrapper.deleteAll(ConnectivityObservationWrapper.class);
        StateChangeWrapper.deleteAll(StateChangeWrapper.class);
        TelephonyObservationWrapper.deleteAll(TelephonyObservationWrapper.class);
        TrafficObservationWrapper.deleteAll(TrafficObservationWrapper.class);

        for (SpeedTestReport r: this.speedTestRecords) {
            r.dispatched = true;
            r.save();
        }
        for (MediaTestReport r: this.mediaTestRecords) {
            r.dispatched = true;
            r.save();
        }
        for (ConnectivityTestReport r: this.connectivityTestRecords) {
            r.dispatched = true;
            r.save();
        }
    }

    public void saveVisualSamples(){
        int lastType = -1;
        for (ConnectivityObservationWrapper observation : connectivityRecords){
            ConnectionModeSample sample = new ConnectionModeSample(observation);
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

        // Backup ApplicationTraffic
        Iterator<TrafficObservationWrapper> iterator = TrafficObservationWrapper.findAsIterator(TrafficObservationWrapper.class, "uid > 0");
        while(iterator.hasNext()){
            ApplicationTraffic value = new ApplicationTraffic(iterator.next());
            ApplicationTraffic refValue = ApplicationTraffic.findFirst(ApplicationTraffic.class,
                    "uid = ? and timestamp = ? and network_type = ?",
                    Integer.toString(value.uid),
                    Long.toString(value.timestamp),
                    Integer.toString(value.networkType));
            if(refValue == null)
                value.save();
            else{
                refValue.txBytes += value.txBytes;
                refValue.rxBytes += value.rxBytes;
                //refValue.tcpRxBytes += value.tcpRxBytes;
                //refValue.tcpTxBytes += value.tcpTxBytes;
                refValue.save();
            }

        }
    }
}

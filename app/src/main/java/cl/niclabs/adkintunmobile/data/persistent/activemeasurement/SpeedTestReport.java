package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class SpeedTestReport extends ActiveMeasurement {

    @SerializedName("host")
    public String host;
    @SerializedName("upload_size")
    public long uploadSize;
    @SerializedName("download_size")
    public long downloadSize;
    @SerializedName("upload_speed")
    public float uploadSpeed;
    @SerializedName("download_speed")
    public float downloadSpeed;
    @SerializedName("elapsed_upload_time")
    public long elapsedUploadTime;
    @SerializedName("elapsed_download_time")
    public long elapsedDownloadTime;

    public SpeedTestReport(){
        super();
    }

    static public ArrayList<String> getTimestampsAllReports() {
        ArrayList<String> ret = new ArrayList<>();
        for (SpeedTestReport r : find(SpeedTestReport.class, null, null, null, "timestamp DESC", null)) {
            ret.add(r.timestamp + "");
        }
        return ret;
    }

    static public ArrayList<String> getDatetimeAllReports() {
        ArrayList<String> ret = new ArrayList<>();
        for (SpeedTestReport r : find(SpeedTestReport.class, null, null, null, "timestamp DESC", null)) {
            ret.add(DisplayDateManager.getDateString(r.timestamp));
        }
        return ret;
    }

    static public List<SpeedTestReport> getSentMediaReports(){
        return find(SpeedTestReport.class, "sent = true");
    }

    static public void deleteAllReports(){
        SpeedTestReport.deleteAll(SpeedTestReport.class);
    }
}

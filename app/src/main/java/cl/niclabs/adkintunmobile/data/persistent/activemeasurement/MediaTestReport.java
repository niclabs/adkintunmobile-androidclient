package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class MediaTestReport extends ActiveMeasurement {

    @SerializedName("video_id")
    public String videoId;
    @SerializedName("video_results")
    public List<VideoResult> videoResults;

    public MediaTestReport() {
    }

    public List<VideoResult> getVideoResults() {
        return VideoResult.find(VideoResult.class, "parent_report = ?", new String(getId().toString()));
    }

    static public ArrayList<String> getTimestampsAllReports() {
        ArrayList<String> ret = new ArrayList<>();
        for (MediaTestReport r : find(MediaTestReport.class, null, null, null, "timestamp DESC", null)) {
            ret.add(r.timestamp + "");
        }
        return ret;
    }

    static public ArrayList<String> getDatetimeAllReports() {
        ArrayList<String> ret = new ArrayList<>();
        for (MediaTestReport r : find(MediaTestReport.class, null, null, null, "timestamp DESC", null)) {
            ret.add(DisplayDateManager.getDateString(r.timestamp));
        }
        return ret;
    }

    static public List<MediaTestReport> getSentReports(){
        return find(MediaTestReport.class, "dispatched = ?", "1");
    }

    static public List<MediaTestReport> getPendingToSendReports(){
        List<MediaTestReport> results = new ArrayList<MediaTestReport>();
        for (MediaTestReport report : find(MediaTestReport.class, "dispatched = ?", "0") ){
            report.videoResults = report.getVideoResults();
            results.add(report);
        }
        return results;
    }

    public static void deleteAllReports() {
        VideoResult.deleteAll(VideoResult.class);
        MediaTestReport.deleteAll(MediaTestReport.class);
    }
}

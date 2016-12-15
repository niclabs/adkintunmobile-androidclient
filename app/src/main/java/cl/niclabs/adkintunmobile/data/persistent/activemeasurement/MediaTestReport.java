package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class MediaTestReport extends ActiveMeasurement {

    @SerializedName("video_id")
    public String videoId;

    public MediaTestReport() {
    }

    public List<VideoResult> getVideoResults() {
        return VideoResult.find(VideoResult.class, "report = ?", new String(getId().toString()));
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

    static public List<MediaTestReport> getSentMediaReports(){
        return find(MediaTestReport.class, "sent = true");
    }
}

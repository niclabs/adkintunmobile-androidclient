package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MediaTestReport extends ActiveMeasurement {

    @SerializedName("video_id")
    public String videoId;
    @SerializedName("video_results")
    public List<VideoResult> videoResults;

    public MediaTestReport(Context context) {
        super(context);
        this.videoResults = new ArrayList<VideoResult>();
    }


}

package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaTestReport extends ActiveMeasurement {

    @SerializedName("video_id")
    public String videoId;
    @SerializedName("video_results")
    public List<VideoResult> videoResults;

}

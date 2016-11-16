package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public class VideoResult extends Persistent<VideoResult>{

    @SerializedName("quality")
    public String quality;
    @SerializedName("buffering_time")
    public long bufferingTime;
    @SerializedName("loaded_percentage")
    public double loadedPercentage;
    @SerializedName("downloaded_bytes")
    public long downloadedBytes;

    public VideoResult() {
    }
}

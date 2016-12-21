package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public class VideoResult extends Persistent<VideoResult>{

    @SerializedName("quality")
    public String quality;
    @SerializedName("buffering_time")
    public long bufferingTime;
    @SerializedName("loaded_fraction")
    public double loadedFraction;
    @SerializedName("downloaded_bytes")
    public long downloadedBytes;

    public MediaTestReport parentReport;

    public VideoResult() {
    }

    public void setUpVideoResult(String quality, long bufferingTime, double loadedFraction, long downloadedBytes) {
        this.quality = quality;
        this.bufferingTime = bufferingTime;
        this.loadedFraction = loadedFraction;
        this.downloadedBytes = downloadedBytes;
    }
}

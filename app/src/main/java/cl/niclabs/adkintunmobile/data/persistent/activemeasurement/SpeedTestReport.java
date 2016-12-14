package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

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
}

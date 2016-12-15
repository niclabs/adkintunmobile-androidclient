package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public class SiteResult extends Persistent<SiteResult>{

    @SerializedName("url")
    public String url;
    @SerializedName("loaded")
    public boolean loaded;
    @SerializedName("loading_time")
    public long loadingTime;
    @SerializedName("downloaded_bytes")
    public long downloadedBytes;

    public ConnectivityTestReport report;

    public SiteResult() {
    }

    public void setUpSiteResult(String url, boolean loaded, long loadingTime, long downloadedBytes){
        this.url = url;
        this.loaded = loaded;
        this.loadingTime = loadingTime;
        this.downloadedBytes = downloadedBytes;
    }
}

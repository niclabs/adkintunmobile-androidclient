package cl.niclabs.adkintunmobile.data.persistent;

import com.google.gson.annotations.SerializedName;

import cl.niclabs.android.data.Persistent;

public class SampleWrapper extends Persistent<SampleWrapper>{

    @SerializedName("size")
    public int size;
    @SerializedName("mean")
    public double mean;
    @SerializedName("variance")
    public double variance;

    public SampleWrapper() {
    }
}

package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ConnectivityTestReport extends ActiveMeasurement {

    @SerializedName("site_results")
    public List<SiteResult> siteResults;

    public ConnectivityTestReport() {
        super();
        this.siteResults = new ArrayList<SiteResult>();
    }
}

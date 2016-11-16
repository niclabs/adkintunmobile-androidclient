package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConnectivityTestReport extends ActiveMeasurement {

    @SerializedName("site_results")
    public List<SiteResult> siteResults;
}

package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import android.content.Context;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class ConnectivityTestReport extends ActiveMeasurement {

    @SerializedName("site_results")
    public List<SiteResult> siteResults;

    public ConnectivityTestReport() {
        super();
        this.siteResults = new ArrayList<SiteResult>();
    }

    static public ArrayList<String> getTimestampsAllReports() {
        ArrayList<String> ret = new ArrayList<>();
        for (ConnectivityTestReport r : find(ConnectivityTestReport.class, null, null, null, "timestamp DESC", null)) {
            ret.add(r.timestamp + "");
        }
        return ret;
    }

    static public ArrayList<String> getDatetimeAllReports() {
        ArrayList<String> ret = new ArrayList<>();
        for (ConnectivityTestReport r : find(ConnectivityTestReport.class, null, null, null, "timestamp DESC", null)) {
            ret.add(DisplayDateManager.getDateString(r.timestamp));
        }
        return ret;
    }

    static public List<ConnectivityTestReport> getSentMediaReports(){
        return find(ConnectivityTestReport.class, "sent = true");
    }
}

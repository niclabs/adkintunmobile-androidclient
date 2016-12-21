package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class ConnectivityTestReport extends ActiveMeasurement {

    @SerializedName("sites_results")
    public List<SiteResult> sitesResults;

    public ConnectivityTestReport() {
    }

    public List<SiteResult> getSiteResults(){
        return SiteResult.find(SiteResult.class, "parent_report = ?", new String(getId().toString()));
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

    static public List<ConnectivityTestReport> getSentReports(){
        return find(ConnectivityTestReport.class, "dispatched = ?", "1");
    }

    static public List<ConnectivityTestReport> getPendingToSendReports(){
        List<ConnectivityTestReport> results = new ArrayList<ConnectivityTestReport>();
        for (ConnectivityTestReport report : find(ConnectivityTestReport.class, "dispatched = ?", "0") ){
            report.sitesResults = report.getSiteResults();
            results.add(report);
        }
        return results;
    }

    public static void deleteAllReports() {
        SiteResult.deleteAll(SiteResult.class);
        ConnectivityTestReport.deleteAll(ConnectivityTestReport.class);
    }
}

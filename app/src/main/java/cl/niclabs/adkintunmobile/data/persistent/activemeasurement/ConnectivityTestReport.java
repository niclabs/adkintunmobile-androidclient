package cl.niclabs.adkintunmobile.data.persistent.activemeasurement;

import java.util.ArrayList;
import java.util.List;

import cl.niclabs.adkintunmobile.utils.display.DisplayDateManager;

public class ConnectivityTestReport extends ActiveMeasurement {

    public ConnectivityTestReport() {
    }

    public List<SiteResult> getSiteResults(){
        return SiteResult.find(SiteResult.class, "report = ?", new String(getId().toString()));
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

    public static void deleteAllReports() {
        SiteResult.deleteAll(SiteResult.class);
        ConnectivityTestReport.deleteAll(ConnectivityTestReport.class);
    }
}

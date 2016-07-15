package cl.niclabs.adkintunmobile.views.applicationstraffic;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import cl.niclabs.adkintunmobile.data.persistent.visualization.ApplicationTraffic;

public class ApplicationsTrafficListElement {

    private String packageName;
    private String label;
    private Drawable logo;
    private int uid;
    private Long rxBytes;
    private Long txBytes;

    public ApplicationsTrafficListElement(Context context, int uid) {

        this.uid = uid;

        String [] packages = context.getPackageManager().getPackagesForUid(uid);
        if (packages != null && packages.length > 0)
            this.packageName = packages[0];

        PackageManager pkgManager = context.getPackageManager();

        try {
            PackageInfo pkgInfo = pkgManager.getPackageInfo(this.packageName, 0);
            ApplicationInfo appInfo = pkgInfo.applicationInfo;

            this.label = pkgManager.getApplicationLabel(appInfo).toString();
            this.logo = pkgManager.getApplicationIcon(pkgInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            this.label = this.packageName;
            this.logo = null;
        }

        this.rxBytes = 0L;
        this.txBytes = 0L;
    }


    public ApplicationsTrafficListElement(Context context, ApplicationTraffic applicationTraffic) {
        this(context, applicationTraffic.uid);
        this.updateRxBytes(applicationTraffic.rxBytes);
        this.updateTxBytes(applicationTraffic.txBytes);
    }

    public void updateRxBytes(Long rxBytes) {
        this.rxBytes += rxBytes;
    }
    public void updateTxBytes(Long txBytes) {
        this.txBytes += txBytes;
    }

    public Long getRxBytes() { return rxBytes; }
    public Long getTxBytes() { return txBytes; }

    public String getLabel() {
        return label;
    }

    public Drawable getLogo() {
        return logo;
    }

    public String getPackageName(){
        return packageName;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "uid: " + this.uid + "\n";
        ret += "label: " + this.label + "\n";
        ret += "rx: " + this.rxBytes + "\n";
        ret += "tx: " + this.txBytes + "\n";
        return ret;
    }
}

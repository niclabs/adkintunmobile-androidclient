package cl.niclabs.adkintunmobile.views.activeconnections;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collection;

import cl.niclabs.adkintunmobile.utils.information.SystemSocket;

public class ActiveConnectionListElement {

    private String packageName;
    private String label;
    private Drawable logo;
    private int uid;
    private ArrayList<String> ipAddr;
    private ArrayList<Integer> portAddr;


    public ActiveConnectionListElement(Context context, SystemSocket systemSocket) {
        this.uid = systemSocket.getUid();

        this.ipAddr = new ArrayList<String>();
        this.ipAddr.add(systemSocket.getRemoteAddress());

        this.portAddr = new ArrayList<Integer>();
        this.portAddr.add(systemSocket.getRemotePort());

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
    }

    public void addIpAddr(Collection<String> ipAddr){
        this.ipAddr.addAll(ipAddr);
    }

    public void addPortAddr(Collection<Integer> portAddr){
        this.portAddr.addAll(portAddr);
    }

    public String getLabel() {
        return label;
    }

    public Drawable getLogo() {
        return logo;
    }

    public ArrayList<String> getIpAddr() {
        return ipAddr;
    }

    public ArrayList<Integer> getPortAddr() {
        return portAddr;
    }

    public boolean isValidElement(){
        return this.logo != null &&
                !this.label.equals("") &&
                this.label != null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ActiveConnectionListElement){
            return this.getLabel().equals(((ActiveConnectionListElement) o).getLabel());
        }
        return super.equals(o);
    }
}

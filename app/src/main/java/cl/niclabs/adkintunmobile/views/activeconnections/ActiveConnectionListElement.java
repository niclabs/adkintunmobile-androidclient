package cl.niclabs.adkintunmobile.views.activeconnections;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;

import cl.niclabs.adkintunmobile.utils.information.SystemSocket;
import cl.niclabs.adkintunmobile.utils.information.SystemSockets;

public class ActiveConnectionListElement {

    private String packageName;
    private String label;
    private Drawable logo;
    private int uid;
    private HashMap<SystemSockets.Type, ArrayList<String>> connAddress;


    public ActiveConnectionListElement(Context context, SystemSocket systemSocket) {
        // Setup uid y packageName
        this.uid = systemSocket.getUid();
        String [] packages = context.getPackageManager().getPackagesForUid(this.uid);
        if (packages != null && packages.length > 0)
            this.packageName = packages[0];

        // Setup label and logo
        try {
            PackageManager pkgManager = context.getPackageManager();
            PackageInfo pkgInfo = pkgManager.getPackageInfo(this.packageName, 0);
            ApplicationInfo appInfo = pkgInfo.applicationInfo;

            this.label = pkgManager.getApplicationLabel(appInfo).toString();
            this.logo = pkgManager.getApplicationIcon(pkgInfo.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            this.label = this.packageName;
            this.logo = null;
        }

        // Setup map
        this.connAddress = new HashMap<SystemSockets.Type, ArrayList<String>>();
        for (SystemSockets.Type type : SystemSockets.Type.values()){
            this.connAddress.put(type, new ArrayList<String>());
        }
        this.addConnection(systemSocket);

    }

    public void addConnection(SystemSocket socket){
        String dir = socket.getRemoteAddress() + ":" + socket.getRemotePort();
        ArrayList<String> connections = this.connAddress.get(socket.getType());
        if (!connections.contains(dir))
            connections.add(dir);
    }

    public String getLabel() {
        return label;
    }

    public Drawable getLogo() {
        return logo;
    }

    public ArrayList<String> getIpConnections(SystemSockets.Type type){
        return connAddress.get(type);
    }

    public int getTotalActiveConnections(){
        int total = 0;
        for (SystemSockets.Type type : SystemSockets.Type.values()){
            total += this.connAddress.get(type).size();
        }
        return total;
    }

    public int getSpecificTotalActiveConnection(SystemSockets.Type type){
        return this.connAddress.get(type).size();
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

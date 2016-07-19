package cl.niclabs.adkintunmobile.utils.information.Connections;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemSocket {

    static final private Pattern fieldsPattern = Pattern.compile("^\\s*(\\d+): ([0-9A-F]+):(....) ([0-9A-F]+):(....) (..) (?:\\S+ ){3}\\s*(\\d+)\\s+\\d+\\s+(\\d+).*$");

    //private String slot;
    private Connections.Type type;
    private String localAddress;
    private int localPort;
    private String remoteAddress;
    private int remotePort;
    private String state;
    private int uid;
    private String inode;

    public SystemSocket(String line, Connections.Type type) {
        Matcher match = fieldsPattern.matcher(line);
        match.lookingAt();

        //slot            = match.group(1);
        this.type            = type;
        this.localAddress    = parseIp(match.group(2));
        this.localPort       = parsePort(match.group(3));
        this.remoteAddress   = parseIp(match.group(4));
        this.remotePort      = parsePort(match.group(5));
        this.state           = match.group(6);
        this.uid             = Integer.parseInt(match.group(7));
        this.inode           = match.group(8);
    }

    private int parsePort(String port){
        return Integer.parseInt(port, 16);
    }

    public String getAppName(Context context){
        String [] packages = context.getPackageManager().getPackagesForUid(uid);
        String packageName = "", label = "";



        if (packages != null && packages.length > 0)
            packageName = packages[0];

        PackageManager pkgManager = context.getPackageManager();

        try {
            PackageInfo pkgInfo = pkgManager.getPackageInfo(packageName, 0);
            ApplicationInfo appInfo = pkgInfo.applicationInfo;

            label = pkgManager.getApplicationLabel(appInfo).toString();
        } catch (PackageManager.NameNotFoundException e) {
            label = packageName;
        }

        return label;
    }

    @Override
    public String toString() {
        return "Connections.Socket(" +
                "localAddress = " + localAddress +
                ", localPort = " + getLocalPort() +
                ", remoteAddress = " + remoteAddress +
                ", remotePort = " + getRemotePort() +
                ", state = " + state +
                ", uid = " + uid +
                ", inode = " + inode + ")";
    }

    public int getRemotePort() {
        return remotePort;
    }

    public int getLocalPort() {
        return localPort;
    }

    public Connections.Type getType() {
        return type;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public int getUid() {
        return uid;
    }

    public boolean isOutsideActiveConnection(){
        return !this.remoteAddress.contains("0.0.0.0") &&
                !this.remoteAddress.contains("127.0.0.1") &&
                (this.state.equals("01") || this.state.toLowerCase().equals("0a"));
    }

    private String parseIp(String ip){
        int len = ip.length();

        byte[] retval = new byte[len / 2];
        for (int i = 0; i < len / 2; i += 4) {
            retval[i] = (byte) ((Character.digit(ip.charAt(2*i + 6), 16) << 4)
                    + Character.digit(ip.charAt(2*i + 7), 16));
            retval[i + 1] = (byte) ((Character.digit(ip.charAt(2*i + 4), 16) << 4)
                    + Character.digit(ip.charAt(2*i + 5), 16));
            retval[i + 2] = (byte) ((Character.digit(ip.charAt(2*i + 2), 16) << 4)
                    + Character.digit(ip.charAt(2*i + 3), 16));
            retval[i + 3] = (byte) ((Character.digit(ip.charAt(2*i), 16) << 4)
                    + Character.digit(ip.charAt(2*i + 1), 16));
        }
        try {
            return InetAddress.getByAddress(retval).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SystemSocket){
            return ((SystemSocket) o).getLocalAddress() == this.getLocalAddress() &&
                    ((SystemSocket) o).getRemoteAddress() == this.getRemoteAddress() &&
                    ((SystemSocket) o).getUid() == this.getUid();
        }
        return super.equals(o);
    }
}

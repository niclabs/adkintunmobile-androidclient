package cl.niclabs.adkintunmobile.data.persistent;

import cl.niclabs.android.data.Persistent;

/**
 * Created by diego on 15-06-16.
 */
public class IpLocation extends Persistent<IpLocation>{
    private String ipAddress;
    private double latitude;
    private double longitude;
    private String country;

    public IpLocation(){}

    public IpLocation(String ipAddress, double latitude, double longitude, String country) {
        this.ipAddress = ipAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public static IpLocation getIpLocationByIp(String ip){
        String[] whereArgs = new String[1];
        whereArgs[0] =  ip;
        return findFirst(IpLocation.class, "ip_address = ?", whereArgs);
    }

    public static boolean existIpLocationByIp(String ip){
        String[] whereArgs = new String[1];
        whereArgs[0] =  ip;
        return count(IpLocation.class, "ip_address = ?", whereArgs) > 0;
    }

    public static void cleanDB(){
        deleteAll(IpLocation.class);
    }
}

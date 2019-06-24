package cl.niclabs.adkintunmobile.data.persistent;

import com.orm.SugarRecord;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Measurement extends SugarRecord {

    private BigDecimal uploadRate;
    private BigDecimal downloadRate;
    private Timestamp timeStamp;
    private String type;
    private int dBm;
    private int mcc;
    private int mnc;
    private int cID;
    private int lac;
    private double altitude;
    private double latitude;
    private double longitude;
    private double accuracy;

    public Measurement() { }

    public Measurement(BigDecimal uploadRate, BigDecimal downloadRate, Timestamp timestamp, String type, int dBm, int mcc, int mnc, int cID, int lac, double altitude, double latitude, double longitude, double accuracy)  {
        this.uploadRate = uploadRate;
        this.downloadRate = downloadRate;
        this.timeStamp = timestamp;
        this.type = type;
        this.dBm = dBm;
        this.mcc = mcc;
        this.mnc = mnc;
        this.lac = lac;
        this.cID = cID;
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this. accuracy = accuracy;
    }


    public void setUploadRate(BigDecimal uploadRate) {
        this.uploadRate = uploadRate;
    }

    public void setDownloadRate(BigDecimal downloadRate) {
        this.downloadRate = downloadRate;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setdBm(int dBm) {
        this.dBm = dBm;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public void setcID(int cID) {
        this.cID = cID;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getUploadRate() {
        return uploadRate;
    }

    public BigDecimal getDownloadRate() {
        return downloadRate;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public String getType() {
        return type;
    }

    public int getdBm() {
        return dBm;
    }

    public int getMcc() {
        return mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public int getcID() {
        return cID;
    }

    public int getLac() {
        return lac;
    }

    public double getAltitude() {
        return altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

}

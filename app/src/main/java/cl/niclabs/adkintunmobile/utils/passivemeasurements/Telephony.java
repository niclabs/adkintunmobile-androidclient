package cl.niclabs.adkintunmobile.utils.passivemeasurements;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.niclabs.adkintunmobile.data.persistent.Measurement;

public class Telephony {
    private static final String LAC = "LAC";
    private static final String CID = "CID";
    private static final String DBM = "DBM";
    private static final String TYPE = "TYPE";
    private static final String MNC = "MNC";
    private static final String MCC = "MCC";
    private static final String TIMESTAMP = "TIMESTAMP";
    private Map<String, Object> result;
    private Context context;
    private TelephonyManager telephonyManager;
    private Measurement measurement;

    public Telephony(Context ctx) {
        this.context = ctx;
        measurement = new Measurement();
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    private Map<String, Object> checkSignal() {
        Log.i("TELEPHONY", "IT WORKS");
        Map<String, Object> map = new HashMap<>();
        int permissionCheck = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.e("Exception", "No permission ACCESS COARSE LOCATION");
            return map;
        }

        String type = "";
        String strength = "";
        String lac = "";
        String cid = "";
        String mcc = "";
        String mnc = "";
        List<CellInfo> cellInfos = telephonyManager.getAllCellInfo();   //This will give info of all sims present inside your mobile
        if(cellInfos!=null) {
            for (int i = 0; i < cellInfos.size(); i++) {
                if (cellInfos.get(i).isRegistered()) {
                    if (cellInfos.get(i) instanceof CellInfoWcdma) {
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthWcdma.getDbm());
                        type = "UMTS";
                        CellIdentityWcdma cellID = cellInfoWcdma.getCellIdentity();
                        cid = Integer.toString(cellID.getCid());
                        lac = Integer.toString((cellID.getLac()));
                        mcc = Integer.toString(cellID.getMcc());
                        mnc = Integer.toString(cellID.getMnc());
                    } else if (cellInfos.get(i) instanceof CellInfoGsm) {
                        CellInfoGsm cellInfogsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfogsm.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthGsm.getDbm());
                        type = "GSM";
                        CellIdentityGsm cellID = cellInfogsm.getCellIdentity();
                        cid = Integer.toString(cellID.getCid());
                        lac = Integer.toString((cellID.getLac()));
                        mcc = Integer.toString(cellID.getMcc());
                        mnc = Integer.toString(cellID.getMnc());
                    } else if (cellInfos.get(i) instanceof CellInfoLte) {
                        CellInfoLte cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                        strength = String.valueOf(cellSignalStrengthLte.getDbm());
                        type = "LTE";
                        CellIdentityLte cellID = cellInfoLte.getCellIdentity();
                        cid = Integer.toString(cellID.getCi());
                        lac = Integer.toString((cellID.getTac()));
                        mcc = Integer.toString(cellID.getMcc());
                        mnc = Integer.toString(cellID.getMnc());
                    }
                }
            }
        }



        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        measurement.setTimeStamp(timestamp);
        measurement.setType(type);
        measurement.setcID(Integer.parseInt(cid));
        measurement.setLac(Integer.parseInt(lac));
        measurement.setMcc(Integer.parseInt(mcc));
        measurement.setMnc(Integer.parseInt(mnc));

        map.put(TIMESTAMP, timestamp.toString());
        map.put(TYPE, type);
        map.put(DBM, strength);
        map.put(CID, cid);
        map.put(LAC, lac);
        map.put(MCC, mcc);
        map.put(MNC, mnc);
        result = map;
        return map;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public Measurement getMeasurement() {
        return measurement;
    }

    public Map<String, Object> run() {
        return checkSignal();
    }
}

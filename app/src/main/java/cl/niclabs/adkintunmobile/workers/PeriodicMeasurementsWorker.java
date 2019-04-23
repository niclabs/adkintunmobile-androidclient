package cl.niclabs.adkintunmobile.workers;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import cl.niclabs.adkintunmobile.views.dashboard.MainActivity;

public class PeriodicMeasurementsWorker extends AdkintunWorker {

    private static final String TAG = PeriodicMeasurementsWorker.class.getSimpleName();
    public static final String SLAVE_WORKER = "SLAVE_WORKER";
    public static final String LAC = "LAC";
    public static final String CID = "CID";
    public static final String DBM = "DBM";
    public static final String TYPE = "TYPE";
    public static final String MNC = "MNC";
    public static final String MCC = "MCC";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String ALTITUDE = "ALTITUDE";
    public static final String ACCURACY = "ACCURACY";
    public GPSListener locationListener;
    private TelephonyManager telephonyManager;
    public FusedLocationProviderClient client;


    public PeriodicMeasurementsWorker(Context context, WorkerParameters params) {
        super(context, params);
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        // Location stuff must change later, this is a quick implementation
        client = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationListener = new GPSListener();
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        Map<String, Object> data = new HashMap<>();
        while(data != null) {
            // Get signal data
            data = checkSignal();
            // Get GPS data
            data = getLocation(data);
            Log.i("DATA", data.toString());
            // Write to log
            writeToCSV("AdkintunLogging.csv", data);

            for (String key : data.keySet()) {
                updateFieldWithName(key, (String) data.get(key));
            }
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                System.out.println("got interrupted!");
            }
        }

        //Data output = new Data.Builder().putAll(data).build();
        return Result.success();
    }

    public void writeToCSV(String fileName, Map<String, Object> map) {
        if (map != null) {
            try {
                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                String filePath = baseDir + File.separator + fileName;
                File f = new File(filePath);
                CSVWriter writer;

                Object[] mapKeys = map.keySet().toArray();
                Object[] mapValues = map.values().toArray();
                if (f.exists() && !f.isDirectory()) {
                    FileWriter mFileWriter = new FileWriter(filePath, true);
                    writer = new CSVWriter(mFileWriter);
                } else {
                    // Create new file and put headers on it
                    writer = new CSVWriter(new FileWriter(filePath));
                    String[] header = Arrays.copyOf(mapKeys, mapKeys.length, String[].class);
                    writer.writeNext(header);
                }
                String[] data = Arrays.copyOf(mapValues, mapValues.length, String[].class);
                writer.writeNext(data);
                writer.close();
            } catch (Exception e) {
                Log.e("IOException", e.toString());
            }
        }
    }

    public Map<String, Object> getLocation(Map<String, Object> map) {
        int permissionCheck = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Exception", "No permission ACCESS_FINE_LOCATION");
            return map;
        }

        client.getLastLocation().addOnSuccessListener(locationListener);
        if (map == null) {
            map = locationListener.getDataHashMap();
        } else {
            map.putAll(locationListener.getDataHashMap());
        }
        return map;
    }

    public Map<String, Object> checkSignal() {
        Map<String, Object> map = new HashMap<>();
        int permissionCheck = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.i("Exception", "No permission ACCESS COARSE LOCATION");
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
        map.put(TIMESTAMP, timestamp.toString());
        map.put(TYPE, type);
        map.put(DBM, strength);
        map.put(CID, cid);
        map.put(LAC, lac);
        map.put(MCC, mcc);
        map.put(MNC, mnc);
        return map;
    }

    public void updateFieldWithName(String name, String value){
        String packageName = getApplicationContext().getPackageName();
        int resId = getApplicationContext().getResources().getIdentifier(name, "string", packageName);
        if (resId == 0) {
            return;
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getApplicationContext().getString(resId), value);
        editor.apply();
    }

    public class GPSListener implements OnSuccessListener<Location> {
        double altitude = -1;
        double latitude = -1;
        double longitude = -1;
        double accuracy = -1;

        @Override
        public void onSuccess(Location location) {
            if (location != null) {
                altitude = location.getAltitude();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                accuracy = location.getAccuracy();
            }
        }

        public Map<String, Object> getDataHashMap() {
            Map<String, Object> map = new HashMap<>();
            map.put(ALTITUDE, Double.toString(altitude));
            map.put(LATITUDE, Double.toString(latitude));
            map.put(LONGITUDE, Double.toString(longitude));
            map.put(ACCURACY, Double.toString(accuracy));

            return map;
        }

    }
}

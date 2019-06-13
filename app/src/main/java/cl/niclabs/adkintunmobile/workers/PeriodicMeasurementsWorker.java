package cl.niclabs.adkintunmobile.workers;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
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

import com.google.android.gms.location.LocationResult;
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
import cl.niclabs.adkintunmobile.R;
import cl.niclabs.adkintunmobile.utils.activemeasurements.speedtest.SpeedTest;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import fr.bmartel.speedtest.model.SpeedTestMode;

public class PeriodicMeasurementsWorker extends AdkintunWorker  {

    public static final String SLAVE_WORKER = "SLAVE_WORKER";
    private static final String LAC = "LAC";
    private static final String CID = "CID";
    private static final String DBM = "DBM";
    private static final String TYPE = "TYPE";
    private static final String MNC = "MNC";
    private static final String MCC = "MCC";
    private static final String TIMESTAMP = "TIMESTAMP";
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String ALTITUDE = "ALTITUDE";
    private static final String ACCURACY = "ACCURACY";

    private int fileSize;
    private String serverHost;
    private String serverPort;
    private SpeedTest downloadSpeedTest;
    private SpeedTest uploadSpeedTest;

    private double altitude = -1;
    private double latitude = -1;
    private double longitude = -1;
    private double accuracy = -1;
    private Location lastLocation;

    private LocationManager.Callback callback;

    private TelephonyManager telephonyManager;

    private LocationManager locationManager;


    public PeriodicMeasurementsWorker(final Context context, WorkerParameters params) {
        super(context, params);
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        // Location stuff must change later, this is a quick implementation
        callback = new LocationManager.Callback(){
            @Override
            public void onSuccess(Location location) {
                setLocation(location);
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                setLocation(locationResult.getLastLocation());
            }
        };
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        locationManager = new LocationManager(Looper.myLooper());

        if (!locationManager.start(context, callback)) {
            Log.e("LOCATION_MANAGER", "FAILED TO START");
        }
        // Set Speed Test
        downloadSpeedTest = setupSpeedTest(getSpeedTest());
        uploadSpeedTest = setupSpeedTest(getSpeedTest());
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        waitForLocation(10);

        Map<String, Object> data;

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

        // Remember to remove CallBack by using stop
        locationManager.stop();

        runSpeedTest(downloadSpeedTest, SpeedTestMode.DOWNLOAD);
        runSpeedTest(uploadSpeedTest, SpeedTestMode.UPLOAD);

        Data output = new Data.Builder().putAll(data).build();
        return Result.success(output);
    }

    // Timeout represents the seconds to be waited
    private void waitForLocation(int timeout) {
        int loopCount = 0;
        while (lastLocation == null && loopCount < timeout) {
            locationManager.getLastLocation();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.i("EXCEPTION", e.toString());
            }
            loopCount += 1;
        }
    }
    private void setLocation(Location location) {
        lastLocation = location;
        if (lastLocation != null) {
            altitude = lastLocation.getAltitude();
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            accuracy = lastLocation.getAccuracy();
        }
    }

    private void writeToCSV(String fileName, Map<String, Object> map) {
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
                    if (mapKeys != null) {
                        String[] header = Arrays.copyOf(mapKeys, mapKeys.length, String[].class);
                        writer.writeNext(header);
                    }
                }
                String[] data = Arrays.copyOf(mapValues, mapValues.length, String[].class);
                writer.writeNext(data);
                writer.close();
            } catch (Exception e) {
                Log.e("IOException", e.toString());
            }
        }
    }


    private Map<String, Object> getLocation(Map<String, Object> map) {

        int permissionCheck = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.e("Exception", "No permission ACCESS_FINE_LOCATION");
            return map;
        }

        locationManager.getLastLocation();

        if (map == null) {
            map = new HashMap<>();

        }
        map.put(ALTITUDE, Double.toString(altitude));
        map.put(LATITUDE, Double.toString(latitude));
        map.put(LONGITUDE, Double.toString(longitude));
        map.put(ACCURACY, Double.toString(accuracy));

        return map;
    }

    private Map<String, Object> checkSignal() {
        Map<String, Object> map = new HashMap<>();
        int permissionCheck = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
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
        map.put(TIMESTAMP, timestamp.toString());
        map.put(TYPE, type);
        map.put(DBM, strength);
        map.put(CID, cid);
        map.put(LAC, lac);
        map.put(MCC, mcc);
        map.put(MNC, mnc);
        return map;
    }

    public SpeedTest getSpeedTest() {
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String speedTestFileSizeValue = sharedPreferences.getString(context.getString(R.string.settings_speed_test_file_size_key), "1000000");
        serverHost = sharedPreferences.getString(context.getString(R.string.settings_speed_test_server_host_key), "http://dev.adkintunmobile.cl");
        serverPort = sharedPreferences.getString(context.getString(R.string.settings_speed_test_server_port_key), "8080");
        fileSize = Integer.parseInt(speedTestFileSizeValue);
        Log.i("SpeedtestData", serverHost + " " + serverPort + " " + fileSize);
        return new SpeedTest(serverHost, serverPort, fileSize);
    }

    public SpeedTest setupSpeedTest(SpeedTest speedtest) {
        ISpeedTestListener listener = new ISpeedTestListener() {
            @Override
            public void onCompletion(final SpeedTestReport report) {
                String mode = report.getSpeedTestMode() == SpeedTestMode.UPLOAD ? "UPLOAD" : "DOWNLOAD";
                Log.i("SpeedTestInfo:", mode + " - " + report.getTotalPacketSize()
                        + " - " + report.getTransferRateBit() + " - " +
                        report.getTransferRateOctet());
            }

            @Override
            public void onError(final SpeedTestError speedTestError, final String errorMessage) {
                Log.e("SpeedTestError:","Error " + speedTestError + " : " + errorMessage);
            }

            @Override
            public void onProgress(final float percent, final SpeedTestReport report) {
                Log.i("SpeedTestProgress", report.toString());
            }
        };
        speedtest.setListener(listener);
        return speedtest;
    }

    public void runSpeedTest(SpeedTest speedTest, SpeedTestMode mode) {
        speedTest.run(mode);
    }

    private void updateFieldWithName(String name, String value){
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

}

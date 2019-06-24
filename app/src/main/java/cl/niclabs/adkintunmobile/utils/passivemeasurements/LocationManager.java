package cl.niclabs.adkintunmobile.utils.passivemeasurements;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

import cl.niclabs.adkintunmobile.data.persistent.Measurement;

public class LocationManager extends LocationCallback {
    private static final String LATITUDE = "LATITUDE";
    private static final String LONGITUDE = "LONGITUDE";
    private static final String ALTITUDE = "ALTITUDE";
    private static final String ACCURACY = "ACCURACY";

    private double altitude = -1;
    private double latitude = -1;
    private double longitude = -1;
    private double accuracy = -1;
    private Location lastLocation;
    private FusedLocationProviderClient client;
    private Callback callback;
    private Looper looper;
    private Context context;
    private Measurement measurement;
    Map<String, Object> result;

    public interface Callback extends OnSuccessListener<Location> {
        void onLocationResult(LocationResult locationResult);
    }

    public LocationManager(Looper looper, Context ctx) {
        super();
        this.context = ctx;
        this.looper = looper;
        this.measurement = new Measurement();

        callback = new Callback(){
            @Override
            public void onSuccess(Location location) {
                setLocation(location);
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                setLocation(locationResult.getLastLocation());
            }
        };
    }

    public boolean start() {
        client = LocationServices.getFusedLocationProviderClient(context);
        if (!checkLocationPermission(context)) return false;
        client.requestLocationUpdates(getLocationRequest(), this, looper);
        return true;
    }

    private void waitForLocation(int timeout) {
        int loopCount = 0;
        while (lastLocation == null && loopCount < timeout) {
            getLastLocation();
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.i("EXCEPTION", e.toString());
            }
            loopCount += 1;
        }
    }

    public Map<String, Object> run() {
        if (start()) {
            Log.e("LOCATION_MANAGER", "FAILED TO START");
        }
        waitForLocation(10);
        result = getLocation(null);
        stop();
        return result;
    }


    public void stop() {
        client.removeLocationUpdates(this);
    }

    @Override
    public void onLocationResult(LocationResult locationResult) {
        callback.onLocationResult(locationResult);
    }

    private boolean checkLocationPermission(Context context) {
        int permissionFineCheck = ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCoarseCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionFineCheck == PackageManager.PERMISSION_GRANTED
                && permissionCoarseCheck == PackageManager.PERMISSION_GRANTED;
    }

    private LocationRequest getLocationRequest() {
        return LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(4000)
                .setFastestInterval(4000);
    }

    public void getLastLocation() {
        if (!checkLocationPermission(context)) return;
        client.getLastLocation().addOnSuccessListener(callback);
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

    private Map<String, Object> getLocation(Map<String, Object> map) {

        int permissionCheck = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.e("Exception", "No permission ACCESS_FINE_LOCATION");
            return map;
        }

        getLastLocation();

        if (map == null) {
            map = new HashMap<>();

        }

        measurement.setAltitude(altitude);
        measurement.setLatitude(latitude);
        measurement.setLongitude(longitude);
        measurement.setAccuracy(accuracy);

        map.put(ALTITUDE, Double.toString(altitude));
        map.put(LATITUDE, Double.toString(latitude));
        map.put(LONGITUDE, Double.toString(longitude));
        map.put(ACCURACY, Double.toString(accuracy));

        return map;
    }

    public void setMeasurement(Measurement measurement) {
        this.measurement = measurement;
    }

    public Measurement getMeasurement() {
        return measurement;
    }
}

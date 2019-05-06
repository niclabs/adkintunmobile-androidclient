package cl.niclabs.adkintunmobile.workers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationManager extends LocationCallback {
    private FusedLocationProviderClient client;
    private Callback callback;
    private Looper looper;
    private Context context;

    public LocationManager(Looper looper) {
        super();
        this.looper = looper;
    }

    public interface Callback extends OnSuccessListener<Location> {
        void onLocationResult(LocationResult locationResult);
    }

    public void getLastLocation() {
        if (!checkLocationPermission(context)) return;
        client.getLastLocation().addOnSuccessListener(callback);
    }


    public boolean start(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
        client = LocationServices.getFusedLocationProviderClient(context);
        if (!checkLocationPermission(context)) return false;
        client.requestLocationUpdates(getLocationRequest(), this, looper);
        return true;
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

}

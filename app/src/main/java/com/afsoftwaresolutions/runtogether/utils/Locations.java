package com.afsoftwaresolutions.runtogether.utils;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.afsoftwaresolutions.runtogether.ui.races_details.run.LocationsChangeListener;

import java.util.List;

public class Locations implements LocationListener {

    private LocationsChangeListener locationsChangeListener;
    private Context mContext;
    private final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;
    private final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1;

    private LocationManager locationManager;
    Location loc;
    Double latitude;
    Double longitude;
    boolean checkGPS = false;
    boolean checkNetwork = false;

    public Locations(Context context,LocationsChangeListener locationsChangeListener) {
        this.mContext = context;
        this.locationsChangeListener = locationsChangeListener;
        getLocation();
    }

    private void getLocation() {

        locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

        checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!checkGPS && !checkNetwork) {
            Toast.makeText(mContext, "No Service Provider is available", Toast.LENGTH_SHORT).show();
        } else {
            if (checkGPS) {

                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (loc != null) {
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();
                    }
                }
            }
        }


    }

    public void stopListener() {
        if (locationManager != null) {

            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(Locations.this);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.loc = location;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        locationsChangeListener.onLocationChange(location);
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    public Location getLoc() {
        return loc;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }
}

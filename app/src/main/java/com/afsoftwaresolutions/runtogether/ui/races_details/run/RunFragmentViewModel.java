package com.afsoftwaresolutions.runtogether.ui.races_details.run;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.models.races.Race;
import com.afsoftwaresolutions.runtogether.network.races_details.RealTimeDatabase;
import com.afsoftwaresolutions.runtogether.services.run.LocationForegroundService;
import com.afsoftwaresolutions.runtogether.ui.races.RacesActivity;
import com.afsoftwaresolutions.runtogether.utils.Locations;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

import javax.inject.Inject;

//public class RunFragmentViewModel extends ViewModel implements LocationListener {
public class RunFragmentViewModel extends ViewModel implements LocationsChangeListener {

    private static final String TAG = "RunFragmentViewModel";

    private MutableLiveData<Location> _location = new MutableLiveData<>();
    public LiveData<Location> location = _location;

    private MutableLiveData<Double> _distance = new MutableLiveData<>(0.0);
    public LiveData<Double> distance = _distance;
    private MutableLiveData<String> _timeRunned = new MutableLiveData<>("00:00:00.000");
    public LiveData<String> timeRunned = _timeRunned;
    private String raceId;

    HashMap<Integer, Location> locations = new HashMap<>();

    Application application;
    RealTimeDatabase realTimeDatabase;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private boolean isProcessRunning = false;

    @Inject
    public RunFragmentViewModel(Application application, RealTimeDatabase realTimeDatabase) {

        Log.d(TAG, "RunFragmentViewModel: model initiated 456...");

        this.application = application;
        this.realTimeDatabase = realTimeDatabase;

        sharedPref = application.getSharedPreferences(application.getString(R.string.shrundata),Context.MODE_PRIVATE);
        this.editor = sharedPref.edit();
    }

    public void getDistanceRunned(Location location) {

        if (locations.size() == 0) {
            locations.put(0, location);
            locations.put(1, location);
        } else {
            locations.replace(0, locations.get(1));
            locations.replace(1, location);
        }

        _location.setValue(location);

        double dist = distance.getValue() + SphericalUtil.computeDistanceBetween(new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude()), new LatLng(locations.get(1).getLatitude(), locations.get(1).getLongitude()));

        dist = Math.round(dist);

        Log.d(TAG, "getDistanceRunned: New Distance " + String.valueOf(dist) + "");

        _distance.setValue(dist);

        editor.putString(application.getString(R.string.shpr_run_time)+raceId, timeRunned.getValue());
        editor.putFloat(application.getString(R.string.shpr_run_distance)+raceId, Float.valueOf(String.valueOf(distance.getValue())));
        editor.apply();

        updateUserData();

        updateNotification();

    }

    Notification.Builder notification;

    private void updateNotification() {

        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_ID = "ForegroundLocationService";
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_NONE);
            application.getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
            notification = new Notification.Builder(application, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.play_button)
                    .setContentTitle("Marathon App")
                    .setContentText("Distancia: " + String.format("%.2f", distance.getValue() / 1000) + " Km " +
                            "" + Html.fromHtml("<br />") + " " +
                            "Tiempo: " + timeRunned.getValue() + "")
                    .setOngoing(true);

            NotificationManager notificationManager = (NotificationManager) application.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1001, notification.build());

        }

    }

    @Override
    public void onLocationChange(Location location) {
        getDistanceRunned(location);
    }


/*    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d(TAG, "onLocationChanged: New Location " + location.getLatitude() + "," + location.getLongitude() + "");
        getDistanceRunned(location);
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
    private final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 20;
    private final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1;
    private LocationManager locationManager;
    Location loc;
    Double latitude;
    Double longitude;
    boolean checkGPS = false;
    boolean checkNetwork = false;*/

    Locations locationsListener;
    public void getCurrentLocation(String raceId) {

        isProcessRunning = true;

        this.raceId = raceId;

        locationsListener = new Locations(application,this);


        /*String time = sharedPref.getString(application.getString(R.string.shpr_run_time)+this.raceId,"");
        Float distance = sharedPref.getFloat(application.getString(R.string.shpr_run_distance)+this.raceId,0);*/

        /*locationManager = (LocationManager) application.getSystemService(LOCATION_SERVICE);

        checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!checkGPS && !checkNetwork) {
            Toast.makeText(application, "No Service Provider is available", Toast.LENGTH_SHORT).show();
        } else {
            if (checkGPS) {

                if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        this);

                if (locationManager != null) {
                    loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (loc != null) {
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();
                    }
                }
            }
        }*/

    }

    public void stopListener() {

        if(locationsListener != null){
            if(locationsListener.getLocationManager() != null){
                locationsListener.stopListener();
            }
        }

        /*if (locationManager != null) {

            if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                return;
            }
            locationManager.removeUpdates(RunFragmentViewModel.this);
        }*/
    }

    public void setTimeRunned(String timeRunned) {
        _timeRunned.setValue(timeRunned);
    }

    int hours = 0, minutes = 0, seconds = 0;
    boolean isRunning = false;
    Handler handler = new Handler();
    Runnable runnable;
    long intiTime = 0;

    public void startChronometer() {

        intiTime = System.currentTimeMillis();

        isRunning = true;
        runnable = new Runnable() {
            @Override
            public void run() {
                /*seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                    if (minutes == 60) {
                        minutes = 0;
                        hours++;
                    }
                }*/
                long currentTime = System.currentTimeMillis();

                long timeRunned = currentTime - intiTime;

                seconds = (int) (timeRunned / 1000) % 60;
                minutes = (int) ((timeRunned / (1000 * 60)) % 60);
                hours = (int) ((timeRunned / (1000 * 60 * 60)) % 24);

                setTimeRunned(formatTime(hours, minutes, seconds));
                handler.postDelayed(this, 1000);
                updateNotification();
            }
        };
        handler.post(runnable);
    }

    public void stopChronometer() {
        if (isRunning) {
            handler.removeCallbacks(runnable);
            isRunning = false;
        }
    }

    private String formatTime(int hours, int minutes, int seconds) {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void updateUserData() {
        realTimeDatabase.updateRunnerDistanceandTime(application, raceId, distance.getValue(), timeRunned.getValue(), location.getValue());
    }

    public void uploadLastData() {

        if(locationsListener != null && isProcessRunning){
            if(locationsListener.getLocationManager() != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(application, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        locationsListener.getLocationManager().getCurrentLocation(
                            LocationManager.GPS_PROVIDER,
                            null,
                            application.getMainExecutor(),
                            new Consumer<Location>() {
                                @Override
                                public void accept(Location location) {
                                    if(location != null){
                                        isProcessRunning = false;
                                        getDistanceRunned(location);
                                    }
                                }
                            }
                        );
                    }
                }
            }
        }
    }
}

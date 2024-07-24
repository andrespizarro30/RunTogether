package com.afsoftwaresolutions.runtogether.services.run;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import com.afsoftwaresolutions.runtogether.BaseActivity;
import com.afsoftwaresolutions.runtogether.BaseApplication;
import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.ui.races_details.run.LocationsChangeListener;
import com.afsoftwaresolutions.runtogether.ui.races_details.run.RunFragment;
import com.afsoftwaresolutions.runtogether.ui.races_details.run.RunFragmentViewModel;
import com.afsoftwaresolutions.runtogether.utils.Locations;
import com.afsoftwaresolutions.runtogether.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class LocationForegroundService extends Service{

    private static final String TAG = "LocationForegroundServi";
    private boolean isServiceRunning = false;
    Notification.Builder notification;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getExtras().getString("action");

        switch (action){

            case "START":{

                final String CHANNEL_ID = "ForegroundLocationService";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
                    getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);
                    notification = new Notification.Builder(this,CHANNEL_ID)
                            .setSmallIcon(R.mipmap.play_button)
                            .setContentText("Corriendo carrera")
                            .setContentTitle("Marathon App")
                            .setOngoing(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        startForeground(1001,notification.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION);
                    }else {
                        startForeground(1001, notification.build());
                    }
                }

                isServiceRunning = true;

                break;
            }
            case "STOP":{
                stopForeground(true);
                if(isServiceRunning){
                    stopSelf(startId);
                    isServiceRunning = false;
                }
                break;
            }
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

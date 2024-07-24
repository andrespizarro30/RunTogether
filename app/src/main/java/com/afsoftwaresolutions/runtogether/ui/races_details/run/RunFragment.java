package com.afsoftwaresolutions.runtogether.ui.races_details.run;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.databinding.FragmentMapBinding;
import com.afsoftwaresolutions.runtogether.databinding.FragmentRunBinding;
import com.afsoftwaresolutions.runtogether.network.races_details.RealTimeDatabase;
import com.afsoftwaresolutions.runtogether.services.run.LocationForegroundService;
import com.afsoftwaresolutions.runtogether.ui.races_details.RaceDetailsActivity;
import com.afsoftwaresolutions.runtogether.viewmodels.ViewModelProviderFactory;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class RunFragment  extends DaggerFragment {

    FragmentRunBinding fragmentRunBinding;

    private RunFragmentViewModel runFragmentViewModel;
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentRunBinding = FragmentRunBinding.inflate(getLayoutInflater(),container,false);

        return fragmentRunBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        runFragmentViewModel = ViewModelProviders.of(this,viewModelProviderFactory).get(RunFragmentViewModel.class);

        setRunButton();
        setStopButton();

        subscribeObservers();

    }

    private void subscribeObservers(){

        runFragmentViewModel.distance.removeObservers(getViewLifecycleOwner());
        runFragmentViewModel.distance.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double distance) {
                fragmentRunBinding.distanceMtsTV.setText(""+ String.valueOf(Math.round(distance)) +"Mts");
                fragmentRunBinding.distanceKmsTV.setText(""+ String.format("%.2f", distance / 1000) +" Km");
            }
        });

    }

    private void startTimeRunned(){
        runFragmentViewModel.startChronometer();
        runFragmentViewModel.timeRunned.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String timeRunned) {
                fragmentRunBinding.timeRunnedTV.setText(timeRunned);
            }
        });
    }

    private void setRunButton(){
        fragmentRunBinding.btnStartRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragmentRunBinding.darkScreen.setVisibility(View.VISIBLE);
                fragmentRunBinding.cardDistance.setVisibility(View.GONE);
                fragmentRunBinding.cardTime.setVisibility(View.GONE);

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    private long startTime = System.currentTimeMillis();
                    int count = 3;
                    public void run() {
                        while (count>=0) {
                            try {
                                Thread.sleep(1000);
                            }
                            catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.post(new Runnable(){
                                public void run() {
                                    fragmentRunBinding.counterTV.setText(String.valueOf(count));
                                }
                            });
                            count = count - 1;
                        }
                        handler.post(new Runnable(){
                            public void run() {
                                fragmentRunBinding.cardDistance.setVisibility(View.VISIBLE);
                                fragmentRunBinding.cardTime.setVisibility(View.VISIBLE);
                                fragmentRunBinding.darkScreen.setVisibility(View.GONE);
                                fragmentRunBinding.btnStartRace.setVisibility(View.GONE);
                                fragmentRunBinding.btnStopRace.setVisibility(View.VISIBLE);
                            }
                        });

                        Intent serviceIntent = new Intent(getContext(), LocationForegroundService.class);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            serviceIntent.putExtra("action","START");
                            v.getContext().startForegroundService(serviceIntent);
                            foregroundServiceRunning();
                        }else{
                            v.getContext().startService(serviceIntent);
                        }
                    }
                };
                new Thread(runnable).start();

                fragmentRunBinding.counterTV.setText(String.valueOf(3));
                runFragmentViewModel.getCurrentLocation(((RaceDetailsActivity)getActivity()).raceId);

                startTimeRunned();

            }
        });
    }

    private void setStopButton(){
        fragmentRunBinding.btnStopRace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getContext(), LocationForegroundService.class);
                serviceIntent.putExtra("action","STOP");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.getContext().startForegroundService(serviceIntent);
                }else{
                    v.getContext().stopService(serviceIntent);
                }
                fragmentRunBinding.btnStartRace.setVisibility(View.VISIBLE);
                fragmentRunBinding.btnStopRace.setVisibility(View.GONE);
                runFragmentViewModel.stopChronometer();
                runFragmentViewModel.stopListener();
                runFragmentViewModel.uploadLastData();
            }
        });
    }

    private boolean foregroundServiceRunning(){
        ActivityManager activityManager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service: activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if(LocationForegroundService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    private void stopService(){
        Intent serviceIntent = new Intent(getContext(), LocationForegroundService.class);
        serviceIntent.putExtra("action","STOP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getContext().startForegroundService(serviceIntent);
        }else{
            getContext().stopService(serviceIntent);
        }
    }

    @Override
    public void onDestroy() {
        runFragmentViewModel.stopChronometer();
        runFragmentViewModel.stopListener();
        runFragmentViewModel.uploadLastData();
        if(foregroundServiceRunning()){
            stopService();
        }
        super.onDestroy();
    }
}

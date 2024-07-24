package com.afsoftwaresolutions.runtogether;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.afsoftwaresolutions.runtogether.databinding.ActivityMainBinding;
import com.afsoftwaresolutions.runtogether.ui.races.RacesActivity;
import com.bumptech.glide.RequestManager;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    ActivityMainBinding activityMainBinding;

    @Inject
    @Named("marathon_logo")
    Drawable logo;
    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(activityMainBinding.getRoot());

        setImageLogo();

        gotoNextScreen();

    }

    private void gotoNextScreen(){

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, RacesActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);

    }

    private void setImageLogo(){
        requestManager.load(logo).into(activityMainBinding.splashLogo);
    }

}
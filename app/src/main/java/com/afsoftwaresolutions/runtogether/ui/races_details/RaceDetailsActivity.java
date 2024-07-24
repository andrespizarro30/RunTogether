package com.afsoftwaresolutions.runtogether.ui.races_details;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.databinding.ActivityRaceDetailsBinding;
import com.afsoftwaresolutions.runtogether.databinding.ActivityRacesBinding;
import com.afsoftwaresolutions.runtogether.models.races.Race;
import com.afsoftwaresolutions.runtogether.services.run.LocationForegroundService;
import com.afsoftwaresolutions.runtogether.ui.races.RacesActivity;
import com.afsoftwaresolutions.runtogether.ui.races.RacesViewModel;
import com.afsoftwaresolutions.runtogether.ui.races_details.map.MapFragment;
import com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions.RacersPositionFragment;
import com.afsoftwaresolutions.runtogether.ui.races_details.run.RunFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import javax.inject.Inject;

import dagger.android.DaggerActivity;
import dagger.android.support.DaggerAppCompatActivity;

public class RaceDetailsActivity extends DaggerAppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    ActivityRaceDetailsBinding activityRaceDetailsBinding;

    public String raceId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRaceDetailsBinding = ActivityRaceDetailsBinding.inflate(getLayoutInflater());

        setContentView(activityRaceDetailsBinding.getRoot());

        activityRaceDetailsBinding.navView.setOnItemSelectedListener(this);
        activityRaceDetailsBinding.navView.setSelectedItemId(R.id.nav_start_run);

        raceId = this.getIntent().getStringExtra("race_id");

    }

    @Inject
    RunFragment runFragment;
    @Inject
    MapFragment mapFragment;
    @Inject
    RacersPositionFragment racersPositionFragment;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        if(menuItem.getItemId()==R.id.nav_start_run){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("RUN");
            if (fragment == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,runFragment,"RUN").commit();
            }else{
                getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(racersPositionFragment).commit();
                getSupportFragmentManager().beginTransaction().show(runFragment).commit();
            }
            return true;
        }else
        if(menuItem.getItemId()==R.id.nav_map){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("MAP");
            if (fragment == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,mapFragment,"MAP").commit();
            }else{
                getSupportFragmentManager().beginTransaction().hide(runFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(racersPositionFragment).commit();
                getSupportFragmentManager().beginTransaction().show(mapFragment).commit();
            }
            return true;
        }else
        if(menuItem.getItemId()==R.id.nav_race_positions){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("POS");
            if (fragment == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment,racersPositionFragment,"POS").commit();
            }else{
                getSupportFragmentManager().beginTransaction().hide(runFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(mapFragment).commit();
                getSupportFragmentManager().beginTransaction().show(racersPositionFragment).commit();
            }
            return true;
            //getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,racersPositionFragment).commit();
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Salir de la carrera")
                .setMessage("Desea terminar la carrera?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RaceDetailsActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

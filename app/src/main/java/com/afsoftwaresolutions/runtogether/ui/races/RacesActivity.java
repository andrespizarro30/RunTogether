package com.afsoftwaresolutions.runtogether.ui.races;

import static android.text.TextUtils.isEmpty;
import static androidx.core.location.LocationManagerCompat.getCurrentLocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.databinding.ActivityRacesBinding;
import com.afsoftwaresolutions.runtogether.databinding.FormAddUserBinding;
import com.afsoftwaresolutions.runtogether.models.races.Race;
import com.afsoftwaresolutions.runtogether.ui.races_details.map.MapFragmentViewModel;
import com.afsoftwaresolutions.runtogether.viewmodels.ViewModelProviderFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class RacesActivity extends DaggerAppCompatActivity {

    ActivityRacesBinding activityRacesBinding;

    private RacesViewModel racesViewModel;

    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    RacesRecyclerViewAdapter racesRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRacesBinding = ActivityRacesBinding.inflate(getLayoutInflater());

        setContentView(activityRacesBinding.getRoot());

        setSupportActionBar(activityRacesBinding.toolbar);

        racesViewModel = ViewModelProviders.of(this,viewModelProviderFactory).get(RacesViewModel.class);

        initRecyclerView();

        subscribeObservers();

        requestPermissions();

        initSearchRaceListener();

    }

    private void subscribeObservers(){

        racesViewModel.races.observe(this, new Observer<List<Race>>() {
            @Override
            public void onChanged(List<Race> races) {
                if(races.size()>0){
                    racesRecyclerViewAdapter.setRaces(races);
                }
            }
        });

        racesViewModel.getAllRaces();
    }

    private void initRecyclerView(){
        activityRacesBinding.racesRV.setLayoutManager(new LinearLayoutManager(RacesActivity.this));
        activityRacesBinding.racesRV.setAdapter(racesRecyclerViewAdapter);

        activityRacesBinding.racesRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    activityRacesBinding.titleTV.setVisibility(View.GONE);
                    activityRacesBinding.searchRace.setVisibility(View.GONE);
                    activityRacesBinding.subTitleTV.setVisibility(View.GONE);
                } else if (dy < 0) {
                    activityRacesBinding.titleTV.setVisibility(View.VISIBLE);
                    activityRacesBinding.searchRace.setVisibility(View.VISIBLE);
                    activityRacesBinding.subTitleTV.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    int REQUEST_CODE_POST_NOTIFICATION_PERMISSION = 500;
    int REQUEST_CODE_LOCATION_PERMISSION = 501;


    private void requestPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED 
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATION_PERMISSION);
            }
            
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 500:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                    } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case 501:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String a = "";
                } else {
                    Toast.makeText(this, "Camera permission is required to Use camera.", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.races_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add_name_opt) {
            showDialogForm();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogForm() {

        SharedPreferences sharedPref = RacesActivity.this.getSharedPreferences(getString(R.string.shuserdata),Context.MODE_PRIVATE);
        String userName = sharedPref.getString(getString(R.string.shpr_user_name), "");
        String userid = sharedPref.getString(getString(R.string.shpr_user_id), "");

        LayoutInflater inflater = LayoutInflater.from(this);

        //View dialogView = inflater.inflate(R.layout.form_add_user, null);
        FormAddUserBinding formAddUserBinding = FormAddUserBinding.inflate(inflater);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(formAddUserBinding.getRoot());
        builder.setTitle("Datos del Corredor");

//        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
//        final EditText editTextID = dialogView.findViewById(R.id.editTextID);

        formAddUserBinding.editTextName.setText(userName);
        formAddUserBinding.editTextID.setText(userid);

//        builder.setPositiveButton("OK", null);
//        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        formAddUserBinding.btnAdduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = formAddUserBinding.editTextName.getText().toString().trim();
                String id = formAddUserBinding.editTextID.getText().toString().trim();

                if (name.isEmpty() || id.isEmpty()) {
                    Toast.makeText(v.getContext(), "Diligencie ambos campos", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPref = RacesActivity.this.getSharedPreferences(getString(R.string.shuserdata),Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.shpr_user_name), name);
                    editor.putString(getString(R.string.shpr_user_id), id);
                    editor.apply();
                    dialog.dismiss();
                }
            }
        });
    }

    List<Race> racesFiltered = new ArrayList<>();

    private void initSearchRaceListener(){

        try {
            Field searchTextField = SearchView.class.getDeclaredField("mSearchSrcTextView");
            searchTextField.setAccessible(true);
            TextView searchTextView = (TextView) searchTextField.get(activityRacesBinding.searchRace);
            searchTextView.setTextColor(Color.BLACK);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        activityRacesBinding.searchRace.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isEmpty(query)) {
                    filterData(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!isEmpty(newText)) {
                    filterData(newText);
                } else {
                    racesRecyclerViewAdapter.setRaces(racesViewModel.races.getValue());
                }
                return false;
            }
        });

    }

    private void filterData(String query) {
        racesFiltered = new ArrayList<>();
        for (Race race : racesViewModel.races.getValue()) {
            if (race.getRaceId().toLowerCase().contains(query.toLowerCase())) {
                racesFiltered.add(race);
            }
        }
        if (racesFiltered.isEmpty()) {
            Toast.makeText(this, "No se encontraron carreras", Toast.LENGTH_SHORT).show();
        }
        racesRecyclerViewAdapter.setRaces(racesFiltered);
    }


}

package com.afsoftwaresolutions.runtogether.ui.races_details.map;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.databinding.FragmentMapBinding;
import com.afsoftwaresolutions.runtogether.models.races_details.RunnersData;
import com.afsoftwaresolutions.runtogether.ui.races_details.RaceDetailsActivity;
import com.afsoftwaresolutions.runtogether.utils.CreateCustomMarker;
import com.afsoftwaresolutions.runtogether.viewmodels.ViewModelProviderFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MapFragment extends DaggerFragment implements OnMapReadyCallback {


    FragmentMapBinding fragmentMapBinding;

    private GoogleMap raceMap;

    private MapFragmentViewModel mapFragmentViewModel;
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentMapBinding = FragmentMapBinding.inflate(getLayoutInflater(),container,false);

        return fragmentMapBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.raceMap);

        mapFragment.getMapAsync(this);

        mapFragmentViewModel = ViewModelProviders.of(this,viewModelProviderFactory).get(MapFragmentViewModel.class);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        raceMap = googleMap;

        LatLng myPos = new LatLng(0,0);

        raceMap.addMarker(new MarkerOptions().position(myPos));

        raceMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));

        subscribeRunnerPositionsChanges();

    }

    private void subscribeRunnerPositionsChanges(){

        Map<String, Marker> mapMarkers = new HashMap<>();

        mapFragmentViewModel.runnersData.removeObservers(getViewLifecycleOwner());
        mapFragmentViewModel.runnersData.observe(getViewLifecycleOwner(), new Observer<Map<String, RunnersData>>() {
            @Override
            public void onChanged(Map<String, RunnersData> stringRunnersDataMap) {

                for (RunnersData runnersData:stringRunnersDataMap.values()) {

                    LatLng runnerpos = new LatLng(runnersData.getLatitude(),runnersData.getLongitude());

                    String initialsName = "";

                    for (String letter:runnersData.userName.split(" ")) {
                        initialsName = ""+ initialsName +""+ letter.substring(0,1).toUpperCase() +"";
                    }

                    CreateCustomMarker ccm = new CreateCustomMarker();

                    Bitmap customMarkerBitmap = ccm.createCustomMarker(getContext(), initialsName);


                    if(mapMarkers.containsKey(runnersData.getUserName())){
                        mapMarkers.get(runnersData.getUserName()).setPosition(runnerpos);
                    }else{
                        mapMarkers.putIfAbsent(
                            runnersData.getUserName(),
                            raceMap.addMarker(new MarkerOptions().position(runnerpos).icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap))));
                    }

                    //raceMap.moveCamera(CameraUpdateFactory.newLatLng(runnerpos));

                    //raceMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

                }
            }
        });


    }

    @Override
    public void onStop() {
        super.onStop();
        mapFragmentViewModel.stopEventListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapFragmentViewModel.getRunnersPositions(((RaceDetailsActivity)getActivity()).raceId);
    }

    @Override
    public void onDestroy() {
        mapFragmentViewModel.stopEventListener();
        super.onDestroy();
    }
}

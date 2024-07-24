package com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.databinding.FragmentMapBinding;
import com.afsoftwaresolutions.runtogether.databinding.FragmentRacersPositionsBinding;
import com.afsoftwaresolutions.runtogether.models.races.Race;
import com.afsoftwaresolutions.runtogether.models.races_details.RunnersData;
import com.afsoftwaresolutions.runtogether.ui.races.RacesActivity;
import com.afsoftwaresolutions.runtogether.ui.races_details.RaceDetailsActivity;
import com.afsoftwaresolutions.runtogether.ui.races_details.map.MapFragmentViewModel;
import com.afsoftwaresolutions.runtogether.viewmodels.ViewModelProviderFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class RacersPositionFragment extends DaggerFragment {

    FragmentRacersPositionsBinding fragmentRacersPositionsBinding;

    private RacersPositionFragmentViewModel racersPositionFragmentViewModel;
    @Inject
    ViewModelProviderFactory viewModelProviderFactory;

    @Inject
    RacersPositionRecyclerViewAdapter racersPositionRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentRacersPositionsBinding = FragmentRacersPositionsBinding.inflate(getLayoutInflater(),container,false);

        return fragmentRacersPositionsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        racersPositionFragmentViewModel = ViewModelProviders.of(this,viewModelProviderFactory).get(RacersPositionFragmentViewModel.class);

        initRecyclerView();

        subscribeObservers();

        racersPositionFragmentViewModel.getRunnersPositions(((RaceDetailsActivity)getActivity()).raceId);

    }

    private void subscribeObservers(){

        racersPositionFragmentViewModel.runnersData.removeObservers(getViewLifecycleOwner());
        racersPositionFragmentViewModel.runnersData.observe(getViewLifecycleOwner(), new Observer<List<RunnersData>>() {
            @Override
            public void onChanged(List<RunnersData> runnersData) {
                if(runnersData.size()>0){

                    Collections.sort(runnersData,Collections.reverseOrder(Comparator.comparing(s -> s.getDistanceRunned())));

                    racersPositionRecyclerViewAdapter.setRacersData(runnersData);
                }
            }
        });
    }

    private void initRecyclerView(){
        fragmentRacersPositionsBinding.runnersDataRV.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentRacersPositionsBinding.runnersDataRV.setAdapter(racersPositionRecyclerViewAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        racersPositionFragmentViewModel.stopEventListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        racersPositionFragmentViewModel.getRunnersPositions(((RaceDetailsActivity)getActivity()).raceId);
    }

    @Override
    public void onDestroy() {
        racersPositionFragmentViewModel.stopEventListener();
        super.onDestroy();
    }
}

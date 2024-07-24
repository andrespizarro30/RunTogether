package com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.afsoftwaresolutions.runtogether.models.races_details.RunnersData;
import com.afsoftwaresolutions.runtogether.network.races_details.RealTimeDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class RacersPositionFragmentViewModel extends ViewModel implements RacersPositionListener {

    private static final String TAG = "RacersPositionFragmentV";
    private RealTimeDatabase realTimeDatabase;
    private MutableLiveData<List<RunnersData>> _runnersData = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<RunnersData>> runnersData = _runnersData;

    @Inject
    public RacersPositionFragmentViewModel(RealTimeDatabase realTimeDatabase){
        this.realTimeDatabase = realTimeDatabase;
    }

    Map<String,RunnersData> runnersMap = new HashMap<>();

    public void getRunnersPositions(String raceId){
        realTimeDatabase.getRunnersData(raceId,this);
    }

    public void stopEventListener(){
        realTimeDatabase.stopEventPositionsListener();
    }

    @Override
    public void onRacersLocationChange(List<RunnersData> runnersDataList) {
        _runnersData.setValue(runnersDataList);
    }
}

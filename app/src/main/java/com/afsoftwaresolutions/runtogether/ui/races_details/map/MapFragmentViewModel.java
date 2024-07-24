package com.afsoftwaresolutions.runtogether.ui.races_details.map;

import android.location.Location;
import android.util.ArrayMap;
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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class MapFragmentViewModel extends ViewModel implements  MapRunnersPositionListener{

    private static final String TAG = "MapFragmentViewModel";

    private RealTimeDatabase realTimeDatabase;

    private MutableLiveData<Map<String,RunnersData>> _runnersData = new MutableLiveData<>(new HashMap<>());
    public LiveData<Map<String,RunnersData>> runnersData = _runnersData;

    @Inject
    public MapFragmentViewModel(RealTimeDatabase realTimeDatabase){

        this.realTimeDatabase = realTimeDatabase;

    }

    public void getRunnersPositions(String raceId){
        realTimeDatabase.getRunnersData(raceId,this);
    }

    public void stopEventListener(){
        realTimeDatabase.stopEventMapPositionsListener();
    }

    @Override
    public void onMapRunnersLocationChange(Map<String, RunnersData> runnersMap) {
        _runnersData.setValue(runnersMap);
    }
}

package com.afsoftwaresolutions.runtogether.network.races_details;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.models.races.Race;
import com.afsoftwaresolutions.runtogether.models.races_details.RunnersData;
import com.afsoftwaresolutions.runtogether.ui.races.RacesDataListener;
import com.afsoftwaresolutions.runtogether.ui.races_details.map.MapRunnersPositionListener;
import com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions.RacersPositionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class RealTimeDatabase {

    private static final String TAG = "RealTimeDatabase";

    DatabaseReference referenceRaces;
    DatabaseReference referencePositions;
    DatabaseReference referenceMapPositions;
    ChildEventListener childEventPositionsListener;
    ChildEventListener childEventMapPositionsListener;
    FirebaseDatabase firebaseDatabase;
    @Inject
    public RealTimeDatabase(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
    }

    public void updateRunnerDistanceandTime(Context context,String raceId,double distance, String time, Location location){

        Log.d(TAG, "updateRunnerDistanceandTime: called");

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.shuserdata),
                Context.MODE_PRIVATE
        );

        String userName = sharedPref.getString(context.getString(R.string.shpr_user_name),"");
        String userId = sharedPref.getString(context.getString(R.string.shpr_user_id),"");

        Map<String, Object> dataRace = new HashMap<>();
        dataRace.put("DistanceRunned",distance);
        dataRace.put("TimeRunned",time);
        dataRace.put("Speed", location.getSpeed());
        dataRace.put("name",userName);

        Map<String,Double> position = new HashMap<>();
        position.put("latitude",location.getLatitude());
        position.put("longitude",location.getLongitude());
        dataRace.put("Location",position);


        firebaseDatabase.getReference("Races")
                .child(raceId)
                .child("Competitors")
                .child(userId)
                .child("RaceData")
                .setValue(dataRace)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: Runner Data updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        
                    }
                });

    }

    RacesDataListener racesDataListener;

    public void getAllRacesData(RacesDataListener racesDataListener){

        this.racesDataListener = racesDataListener;

        referenceRaces = firebaseDatabase.getReference("Races");

        referenceRaces.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()) {
                    List<Race> races = new ArrayList<>();
                    for(DataSnapshot snapshot :task.getResult().getChildren()) {
                        Race race = new Race();
                        race.setRaceId(snapshot.getKey());
                        race.setDates(snapshot.child("Dates").getValue(String.class));
                        race.setLogo(snapshot.child("logo").getValue(String.class));
                        races.add(race);
                    }

                    racesDataListener.onRacesDataChange(races);

                }

            }
        });

    }

    RacersPositionListener racersPositionList;
    Map<String,RunnersData> runnersMap = new HashMap<>();

    public void getRunnersData(String raceId,RacersPositionListener racersPositionListener){

        this.racersPositionList = racersPositionListener;

        referencePositions = firebaseDatabase.getReference("Races")
                .child(raceId)
                .child("Competitors");

        childEventPositionsListener = referencePositions.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                RunnersData runnerData = new RunnersData();
                runnerData.setUserId(snapshot.getKey());
                runnerData.setUserName(snapshot.child("RaceData").child("name").getValue(String.class));
                runnerData.setDistanceRunned(snapshot.child("RaceData").child("DistanceRunned").getValue(Integer.class));
                runnerData.setTimeRunned(snapshot.child("RaceData").child("TimeRunned").getValue(String.class));
                runnerData.setSpeed(snapshot.child("RaceData").child("Speed").getValue(Double.class));
                runnerData.setLatitude(snapshot.child("RaceData").child("Location").child("latitude").getValue(Double.class));
                runnerData.setLongitude(snapshot.child("RaceData").child("Location").child("longitude").getValue(Double.class));

                if(runnersMap.containsKey(snapshot.getKey())){
                    runnersMap.replace(snapshot.getKey(),runnerData);
                }else{
                    runnersMap.putIfAbsent(snapshot.getKey(),runnerData);
                }

                List<RunnersData> runnersDataList = new ArrayList<RunnersData>(runnersMap.values());

                racersPositionList.onRacersLocationChange(runnersDataList);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                RunnersData runnerData = new RunnersData();
                runnerData.setUserId(snapshot.getKey());
                runnerData.setUserName(snapshot.child("RaceData").child("name").getValue(String.class));
                runnerData.setDistanceRunned(snapshot.child("RaceData").child("DistanceRunned").getValue(Integer.class));
                runnerData.setTimeRunned(snapshot.child("RaceData").child("TimeRunned").getValue(String.class));
                runnerData.setSpeed(snapshot.child("RaceData").child("Speed").getValue(Double.class));
                runnerData.setLatitude(snapshot.child("RaceData").child("Location").child("latitude").getValue(Double.class));
                runnerData.setLongitude(snapshot.child("RaceData").child("Location").child("longitude").getValue(Double.class));

                if(runnersMap.containsKey(snapshot.getKey())){
                    runnersMap.replace(snapshot.getKey(),runnerData);
                }else{
                    runnersMap.putIfAbsent(snapshot.getKey(),runnerData);
                }

                List<RunnersData> runnersDataList = new ArrayList<RunnersData>(runnersMap.values());

                racersPositionList.onRacersLocationChange(runnersDataList);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //return referencePostions;

    }

    public void stopEventPositionsListener(){
        referencePositions.removeEventListener(childEventPositionsListener);
    }

    MapRunnersPositionListener mapRunnersPositionList;

    Map<String,RunnersData> runnersData = new HashMap<>();

    public void getRunnersData(String raceId,MapRunnersPositionListener racersPositionListener){

        this.mapRunnersPositionList = racersPositionListener;

        referenceMapPositions = firebaseDatabase.getReference("Races")
                .child(raceId)
                .child("Competitors");

        childEventMapPositionsListener = referenceMapPositions.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Map<String,RunnersData> runnersMap = runnersData;

                RunnersData runnerData = new RunnersData();
                runnerData.setUserName(snapshot.child("RaceData").child("name").getValue(String.class));
                runnerData.setDistanceRunned(snapshot.child("RaceData").child("DistanceRunned").getValue(Integer.class));
                runnerData.setTimeRunned(snapshot.child("RaceData").child("TimeRunned").getValue(String.class));
                runnerData.setLatitude(snapshot.child("RaceData").child("Location").child("latitude").getValue(Double.class));
                runnerData.setLongitude(snapshot.child("RaceData").child("Location").child("longitude").getValue(Double.class));

                if(runnersMap.containsKey(snapshot.getKey())){
                    runnersMap.replace(snapshot.getKey(),runnerData);
                }else{
                    runnersMap.putIfAbsent(snapshot.getKey(),runnerData);
                }

                runnersData = runnersMap;

                mapRunnersPositionList.onMapRunnersLocationChange(runnersMap);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Map<String,RunnersData> runnersMap = runnersData;

                RunnersData runnerData = new RunnersData();
                runnerData.setUserName(snapshot.child("RaceData").child("name").getValue(String.class));
                runnerData.setDistanceRunned(snapshot.child("RaceData").child("DistanceRunned").getValue(Integer.class));
                runnerData.setTimeRunned(snapshot.child("RaceData").child("TimeRunned").getValue(String.class));
                runnerData.setLatitude(snapshot.child("RaceData").child("Location").child("latitude").getValue(Double.class));
                runnerData.setLongitude(snapshot.child("RaceData").child("Location").child("longitude").getValue(Double.class));

                if(runnersMap.containsKey(snapshot.getKey())){
                    runnersMap.replace(snapshot.getKey(),runnerData);
                }else{
                    runnersMap.putIfAbsent(snapshot.getKey(),runnerData);
                }

                runnersData = runnersMap;

                mapRunnersPositionList.onMapRunnersLocationChange(runnersMap);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void stopEventMapPositionsListener(){
        referenceMapPositions.removeEventListener(childEventMapPositionsListener);
    }

}

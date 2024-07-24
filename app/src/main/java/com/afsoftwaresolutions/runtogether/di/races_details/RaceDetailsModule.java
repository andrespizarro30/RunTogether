package com.afsoftwaresolutions.runtogether.di.races_details;

import com.afsoftwaresolutions.runtogether.di.races.RacesScope;
import com.afsoftwaresolutions.runtogether.network.races.RacesApi;
import com.afsoftwaresolutions.runtogether.network.races_details.RealTimeDatabase;
import com.afsoftwaresolutions.runtogether.services.run.LocationForegroundService;
import com.afsoftwaresolutions.runtogether.ui.races.RacesRecyclerViewAdapter;
import com.afsoftwaresolutions.runtogether.ui.races_details.map.MapFragment;
import com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions.RacersPositionFragment;
import com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions.RacersPositionRecyclerViewAdapter;
import com.afsoftwaresolutions.runtogether.ui.races_details.run.RunFragment;
import com.google.android.gms.maps.GoogleMap;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class RaceDetailsModule {

    @RacesDetailsScope
    @Provides
    static RunFragment provideRunFragment(){
        return new RunFragment();
    }

    @RacesDetailsScope
    @Provides
    static MapFragment provideMapFragment(){
        return new MapFragment();
    }

    @RacesDetailsScope
    @Provides
    static RacersPositionFragment provideRacersPositionFragment(){
        return new RacersPositionFragment();
    }

    @RacesDetailsScope
    @Provides
    static RacersPositionRecyclerViewAdapter provideRacersPositionRecyclerViewAdapter(){
        return new RacersPositionRecyclerViewAdapter();
    }

}

package com.afsoftwaresolutions.runtogether.di.races;

import android.app.Application;

import com.afsoftwaresolutions.runtogether.network.races.RacesApi;
import com.afsoftwaresolutions.runtogether.ui.races.RacesRecyclerViewAdapter;
import com.bumptech.glide.RequestManager;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class RacesModule {

    @RacesScope
    @Provides
    static RacesApi provideRacesApi(Retrofit retrofit){
        return retrofit.create(RacesApi.class);
    }

    @RacesScope
    @Provides
    static RacesRecyclerViewAdapter provideRacesRecyclerAdapter(Application application){
        return new RacesRecyclerViewAdapter(application);
    }

}

package com.afsoftwaresolutions.runtogether.di.races_details;

import com.afsoftwaresolutions.runtogether.services.run.LocationForegroundService;
import com.afsoftwaresolutions.runtogether.ui.races_details.run.RunFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RaceDetailsServicesModule {

    @ContributesAndroidInjector(
            modules = {

            }
    )
    abstract LocationForegroundService contributeLocationForegroundService();


}

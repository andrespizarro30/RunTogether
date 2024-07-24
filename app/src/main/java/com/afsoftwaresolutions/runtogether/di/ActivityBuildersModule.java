package com.afsoftwaresolutions.runtogether.di;

import com.afsoftwaresolutions.runtogether.MainActivity;
import com.afsoftwaresolutions.runtogether.di.main.MainScope;
import com.afsoftwaresolutions.runtogether.di.races.RacesModule;
import com.afsoftwaresolutions.runtogether.di.races.RacesScope;
import com.afsoftwaresolutions.runtogether.di.races.RacesViewModelModule;
import com.afsoftwaresolutions.runtogether.di.races_details.RaceDetailsModule;
import com.afsoftwaresolutions.runtogether.di.races_details.RaceDetailsServicesModule;
import com.afsoftwaresolutions.runtogether.di.races_details.RaceDetailsViewModelsModules;
import com.afsoftwaresolutions.runtogether.di.races_details.RacesDetailsFragmentBuildersModule;
import com.afsoftwaresolutions.runtogether.di.races_details.RacesDetailsScope;
import com.afsoftwaresolutions.runtogether.ui.races.RacesActivity;
import com.afsoftwaresolutions.runtogether.ui.races_details.RaceDetailsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @MainScope
    @ContributesAndroidInjector(
            modules = {

            }
    )
    abstract MainActivity contributeMainActivity();

    @RacesScope
    @ContributesAndroidInjector(
            modules = {
                    RacesModule.class,
                    RacesViewModelModule.class
            }
    )
    abstract RacesActivity contributeRacesActivity();

    @RacesDetailsScope
    @ContributesAndroidInjector(
            modules = {
                    RacesDetailsFragmentBuildersModule.class,
                    RaceDetailsModule.class,
                    RaceDetailsServicesModule.class,
                    RaceDetailsViewModelsModules.class
            }
    )
    abstract RaceDetailsActivity contributeRaceDetailActivity();


}

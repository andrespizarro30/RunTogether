package com.afsoftwaresolutions.runtogether.di.races_details;

import com.afsoftwaresolutions.runtogether.ui.races_details.map.MapFragment;
import com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions.RacersPositionFragment;
import com.afsoftwaresolutions.runtogether.ui.races_details.run.RunFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class RacesDetailsFragmentBuildersModule {

    @ContributesAndroidInjector(
            modules = {

            }
    )
    abstract RunFragment contributeRunFragment();

    @ContributesAndroidInjector(
            modules = {

            }
    )
    abstract MapFragment contributeMapFragment();

    @ContributesAndroidInjector(
            modules = {

            }
    )
    abstract RacersPositionFragment contributeRacersPositionFragment();

}

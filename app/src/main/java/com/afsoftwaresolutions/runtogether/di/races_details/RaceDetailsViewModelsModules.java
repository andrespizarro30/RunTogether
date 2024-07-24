package com.afsoftwaresolutions.runtogether.di.races_details;

import androidx.lifecycle.ViewModel;

import com.afsoftwaresolutions.runtogether.di.ViewModelKey;
import com.afsoftwaresolutions.runtogether.ui.races_details.map.MapFragmentViewModel;
import com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions.RacersPositionFragmentViewModel;
import com.afsoftwaresolutions.runtogether.ui.races_details.run.RunFragmentViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RaceDetailsViewModelsModules {

    @Binds
    @IntoMap
    @ViewModelKey(RunFragmentViewModel.class)
    public abstract ViewModel bindRunFragmentViewModel(RunFragmentViewModel profileViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MapFragmentViewModel.class)
    public abstract ViewModel bindMapFragmentViewModel(MapFragmentViewModel mapFragmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RacersPositionFragmentViewModel.class)
    public abstract ViewModel bindRacersPositionFragmentViewModel(RacersPositionFragmentViewModel racersPositionFragmentViewModel);


}

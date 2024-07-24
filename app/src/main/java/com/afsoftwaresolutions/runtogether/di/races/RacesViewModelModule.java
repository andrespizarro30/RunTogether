package com.afsoftwaresolutions.runtogether.di.races;

import androidx.lifecycle.ViewModel;

import com.afsoftwaresolutions.runtogether.di.ViewModelKey;
import com.afsoftwaresolutions.runtogether.ui.races.RacesViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class RacesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RacesViewModel.class)
    public abstract ViewModel bindRacesViewModel(RacesViewModel racesViewModel);

}

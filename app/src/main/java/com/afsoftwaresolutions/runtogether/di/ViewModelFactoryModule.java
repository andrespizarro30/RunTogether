package com.afsoftwaresolutions.runtogether.di;

import androidx.lifecycle.ViewModelProvider;

import com.afsoftwaresolutions.runtogether.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelProviderFactory);

    //THIS WAY CAN BE DONE TOO AS ABOVE, IT IS EXACTLY THE SAME, BUT ABOVE'S WAY IS MORE EFFICIENT
   /* @Provides
    public ViewModelProvider.Factory provideViewModelFactory(ViewModelProviderFactory viewModelProviderFactory){
        return viewModelProviderFactory;
    }
    */

}

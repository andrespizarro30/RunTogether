package com.afsoftwaresolutions.runtogether.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.network.races_details.RealTimeDatabase;
import com.afsoftwaresolutions.runtogether.utils.Constants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Singleton
    @Provides
    static Retrofit provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions(){
        return RequestOptions
                .placeholderOf(R.drawable.white_background)
                .error(R.drawable.white_background);
    }


    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions){
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    @Named("marathon_logo")
    static Drawable provideAppDrawable(Application application){
        return ContextCompat.getDrawable(application,R.drawable.city_marathon);
    }

    @Singleton
    @Provides
    static FirebaseDatabase provideFirebaseDatabaseInstance(){
        return FirebaseDatabase.getInstance();
    }

    @Singleton
    @Provides
    static RealTimeDatabase provideRealTimeDatabaseInstance(FirebaseDatabase firebaseDatabase){
        return new RealTimeDatabase(firebaseDatabase);
    }

}

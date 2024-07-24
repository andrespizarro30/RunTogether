package com.afsoftwaresolutions.runtogether.network.races;

import com.afsoftwaresolutions.runtogether.models.races.Race;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RacesApi {

    @GET("/posts")
    Flowable<List<Race>> getPostsFromUser(@Query("userId") int userId);

}

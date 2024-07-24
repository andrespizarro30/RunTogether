package com.afsoftwaresolutions.runtogether.ui.races;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.afsoftwaresolutions.runtogether.models.races.Race;
import com.afsoftwaresolutions.runtogether.network.races.RacesApi;
import com.afsoftwaresolutions.runtogether.network.races_details.RealTimeDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RacesViewModel extends ViewModel implements RacesDataListener {

    private MutableLiveData<List<Race>> _races = new MutableLiveData<>();
    public LiveData<List<Race>> races = _races;

    private RealTimeDatabase realTimeDatabase;

    @Inject
    public RacesViewModel(RealTimeDatabase realTimeDatabase){
        this.realTimeDatabase = realTimeDatabase;
    }

    DatabaseReference reference;
    ChildEventListener childEventListener;
    public void getAllRaces(){

        realTimeDatabase.getAllRacesData(this);

    }

    @Override
    public void onRacesDataChange(List<Race> racesList) {
        _races.setValue(racesList);
    }


    /*@Inject
    public RacesViewModel(RacesApi racesApi){
        this.racesApi = racesApi;
    }

    public LiveData<RaceResource<List<Post>>> observePosts(){
        if(posts == null){
            posts = new MediatorLiveData<>();
            posts.setValue(RaceResource.loading((List<Post>)null));

            LiveData<RaceResource<List<Post>>> source  = LiveDataReactiveStreams.fromPublisher(
                    racesApi.getPostsFromUser(8)
                            .onErrorReturn(new Function<Throwable, List<Post>>() {
                                @Override
                                public List<Post> apply(Throwable throwable) throws Exception {
                                    ArrayList<Post> postsError = new ArrayList<>();
                                    return postsError;
                                }
                            })
                            .map(new Function<List<Post>, RaceResource<List<Post>>>() {
                                @Override
                                public RaceResource<List<Post>> apply(List<Post> posts) throws Exception {
                                    if(posts.isEmpty()){
                                        return RaceResource.error("Something went wrong",(List<Post>)null);
                                    }
                                    return RaceResource.success(posts);
                                }
                            })
                            .subscribeOn(Schedulers.io())
            );

            posts.addSource(source, new Observer<RaceResource<List<Post>>>() {
                @Override
                public void onChanged(RaceResource<List<Post>> postMainResource) {
                    posts.setValue(postMainResource);
                    posts.removeSource(source);
                }
            });
        }

        return posts;
    }*/


}

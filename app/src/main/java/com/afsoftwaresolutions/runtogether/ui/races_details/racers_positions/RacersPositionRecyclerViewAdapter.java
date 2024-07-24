package com.afsoftwaresolutions.runtogether.ui.races_details.racers_positions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afsoftwaresolutions.runtogether.databinding.RacesViewHolderItemListBinding;
import com.afsoftwaresolutions.runtogether.databinding.RunnersDataViewHolderItemListBinding;
import com.afsoftwaresolutions.runtogether.models.races.Race;
import com.afsoftwaresolutions.runtogether.models.races_details.RunnersData;
import com.afsoftwaresolutions.runtogether.ui.races.RacesRecyclerViewAdapter;
import com.afsoftwaresolutions.runtogether.ui.races_details.RaceDetailsActivity;
import com.afsoftwaresolutions.runtogether.utils.CreateCustomMarker;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RacersPositionRecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<RunnersData> runners = new ArrayList<>();

    public void setRacersData(List<RunnersData> runners){
        this.runners = runners;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RunnersDataViewHolderItemListBinding racesViewHolderItemListBinding = RunnersDataViewHolderItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RacersPositionRecyclerViewAdapter.RunnersDataViewHolder(racesViewHolderItemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((RacersPositionRecyclerViewAdapter.RunnersDataViewHolder)holder).bind(runners.get(position), position);
    }

    @Override
    public int getItemCount() {
        return runners.size();
    }

    HashMap<String,Bitmap> customMarkers = new HashMap<>();

    public class RunnersDataViewHolder extends RecyclerView.ViewHolder{

        RunnersDataViewHolderItemListBinding runnersDataViewHolderItemListBinding;

        public RunnersDataViewHolder(RunnersDataViewHolderItemListBinding runnersDataViewHolderItemListBinding) {
            super(runnersDataViewHolderItemListBinding.getRoot());

            this.runnersDataViewHolderItemListBinding = runnersDataViewHolderItemListBinding;
        }

        public void bind(RunnersData runnersData,int position){
            runnersDataViewHolderItemListBinding.runnerName.setText(runnersData.getUserName());

            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);

            runnersDataViewHolderItemListBinding.runnerDistance.setText(df.format(runnersData.getDistanceRunned()/1000) + " Km");

            runnersDataViewHolderItemListBinding.runnerSpeed.setText(df.format(runnersData.getSpeed() * 3.6) + " Km/h");

            runnersDataViewHolderItemListBinding.runnerTime.setText(runnersData.getTimeRunned());

            if(!customMarkers.containsKey(runnersData.getUserId())){
                String initialsName = "";
                for (String letter:runnersData.userName.split(" ")) {
                    initialsName = ""+ initialsName +""+ letter.substring(0,1).toUpperCase() +"";
                }
                CreateCustomMarker ccm = new CreateCustomMarker();
                Bitmap customMarkerBitmap = ccm.createCustomMarker(itemView.getContext(), initialsName);

                customMarkers.putIfAbsent(runnersData.getUserId(),customMarkerBitmap);

            }

            runnersDataViewHolderItemListBinding.userLogo.setImageBitmap(customMarkers.get(runnersData.getUserId()));

            runnersDataViewHolderItemListBinding.positionTV.setText(String.valueOf(position+1)+"º");

        }
    }
}

package com.afsoftwaresolutions.runtogether.ui.races;

import static androidx.core.content.ContextCompat.getString;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.afsoftwaresolutions.runtogether.R;
import com.afsoftwaresolutions.runtogether.databinding.RacesViewHolderItemListBinding;
import com.afsoftwaresolutions.runtogether.models.races.Race;
import com.afsoftwaresolutions.runtogether.ui.races_details.RaceDetailsActivity;
import com.bumptech.glide.RequestManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RacesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Race> races = new ArrayList<>();

    public void setRaces(List<Race> races){
        this.races = races;
        notifyDataSetChanged();
    }

    private Context mContext;

    public RacesRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RacesViewHolderItemListBinding racesViewHolderItemListBinding = RacesViewHolderItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RaceViewHolder(racesViewHolderItemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final String raceId = races.get(position).raceId;

        ((RaceViewHolder)holder).bind(races.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = mContext.getSharedPreferences(
                        holder.itemView.getContext().getString(R.string.shuserdata),
                        Context.MODE_PRIVATE
                );
                String userName = sharedPref.getString(holder.itemView.getContext().getString(R.string.shpr_user_name),"");
                if(!userName.equals("")){
                    Intent intent = new Intent(v.getContext(), RaceDetailsActivity.class);
                    intent.putExtra("race_id",raceId);
                    v.getContext().startActivity(intent);
                }else{
                    Toast.makeText(v.getContext(), "Ingrese datos de usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return races.size();
    }

    public class RaceViewHolder extends RecyclerView.ViewHolder{

        RacesViewHolderItemListBinding racesViewHolderItemListBinding;

        public RaceViewHolder(RacesViewHolderItemListBinding racesViewHolderItemListBinding) {
            super(racesViewHolderItemListBinding.getRoot());

            this.racesViewHolderItemListBinding = racesViewHolderItemListBinding;
        }

        public void bind(Race race){
            racesViewHolderItemListBinding.nameRace.setText(race.getRaceId());
            racesViewHolderItemListBinding.dateRace.setText(race.getDate());
            Uri logoUri = Uri.parse(race.getLogo());
            Picasso.get().load(logoUri).into(racesViewHolderItemListBinding.imageView);
        }
    }

}

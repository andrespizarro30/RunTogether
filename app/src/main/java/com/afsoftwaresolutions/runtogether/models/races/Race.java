package com.afsoftwaresolutions.runtogether.models.races;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Race implements Parcelable {

    @SerializedName("raceId") public String raceId;
    @SerializedName("date") public String date;
    @SerializedName("logo") public String logo;
    @SerializedName("competitors") public List<Competitor> competitors;

    public Race(String raceId, String date, List<Competitor> competitors) {
        this.raceId = raceId;
        this.date = date;
        this.logo = logo;
        this.competitors = competitors;
    }

    public Race() {

    }

    public String getRaceId() {
        return raceId;
    }

    public String getDate() {
        return date;
    }
    public String getLogo() {
        return logo;
    }


    public List<Competitor> getCompetitors() {
        return competitors;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public void setDates(String date) {
        this.date = date;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setCompetitors(List<Competitor> competitors) {
        this.competitors = competitors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(raceId);
        dest.writeString(date);
        dest.writeString(logo);
    }

    protected Race(Parcel in) {
        raceId = in.readString();
        date = in.readString();
        logo = in.readString();
    }

    public static final Creator<Race> CREATOR = new Creator<Race>() {
        @Override
        public Race createFromParcel(Parcel in) {
            return new Race(in);
        }

        @Override
        public Race[] newArray(int size) {
            return new Race[size];
        }
    };

}

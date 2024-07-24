package com.afsoftwaresolutions.runtogether.models.races_details;

import android.location.Location;

import com.google.gson.annotations.SerializedName;

public class RunnersData {

    @SerializedName("userId") public String userId;
    @SerializedName("UserName") public String userName;
    @SerializedName("DistanceRunned") public double distanceRunned;
    @SerializedName("Latitude") public double latitude;
    @SerializedName("Longitude") public double longitude;
    @SerializedName("TimeRunned") public String timeRunned;
    @SerializedName("Speed") public double speed;

    public RunnersData(String userId,String userName, int distanceRunned, double latitude, double longitude, String timeRunned,double speed) {
        this.userId = userId;
        this.userName = userName;
        this.distanceRunned = distanceRunned;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeRunned = timeRunned;
        this.speed = speed;
    }
    public RunnersData() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getDistanceRunned() {
        return distanceRunned;
    }

    public void setDistanceRunned(double distanceRunned) {
        this.distanceRunned = distanceRunned;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getUserName() {
        return userName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimeRunned() {
        return timeRunned;
    }

    public void setTimeRunned(String timeRunned) {
        this.timeRunned = timeRunned;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}

package com.afsoftwaresolutions.runtogether.models.races;

import com.google.gson.annotations.SerializedName;

public class Competitor {
    @SerializedName("compId") public int compId;
    @SerializedName ("name") public String name;
    @SerializedName ("email") public String email;
    @SerializedName ("phone") public String phone;

    public Competitor(int compId, String name, String email, String phone) {
        this.compId = compId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Competitor() {

    }

    public int getCompId() {
        return compId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setCompId(int compId) {
        this.compId = compId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

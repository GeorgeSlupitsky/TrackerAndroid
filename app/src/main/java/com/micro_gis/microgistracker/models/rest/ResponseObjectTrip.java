package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User9 on 07.03.2018.
 */

public class ResponseObjectTrip {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("dataList")
    @Expose
    private List<Trip> trips;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}

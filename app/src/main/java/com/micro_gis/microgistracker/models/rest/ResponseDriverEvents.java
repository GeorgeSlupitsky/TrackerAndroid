package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseDriverEvents {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("voyages")
    @Expose
    private List<Voyage> voyages;

    @SerializedName("currentDate")
    @Expose
    private long currentDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Voyage> getVoyages() {
        return voyages;
    }

    public void setVoyages(List<Voyage> voyages) {
        this.voyages = voyages;
    }

    public long getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(long currentDate) {
        this.currentDate = currentDate;
    }
}

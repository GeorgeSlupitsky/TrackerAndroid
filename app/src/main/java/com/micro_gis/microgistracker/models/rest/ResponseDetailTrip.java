package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User9 on 13.03.2018.
 */

public class ResponseDetailTrip {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("dataList")
    @Expose
    private List <PointInMap> points;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PointInMap> getPoints() {
        return points;
    }

    public void setPoints(List<PointInMap> points) {
        this.points = points;
    }
}

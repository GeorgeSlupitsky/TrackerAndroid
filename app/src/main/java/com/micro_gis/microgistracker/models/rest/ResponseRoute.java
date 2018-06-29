package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ResponseRoute {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("points")
    @Expose
    private Map<String, Point> points;

    @SerializedName("voyageId")
    @Expose
    private Long voyageId;

    @SerializedName("lastPoint")
    @Expose
    private Integer lastPoint;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Point> getPoints() {
        return points;
    }

    public void setPoints(Map<String, Point> points) {
        this.points = points;
    }

    public Long getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(Long voyageId) {
        this.voyageId = voyageId;
    }

    public Integer getLastPoint() {
        return lastPoint;
    }

    public void setLastPoint(Integer lastPoint) {
        this.lastPoint = lastPoint;
    }
}

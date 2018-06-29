package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoyageDetail {

    @SerializedName("timeliness")
    @Expose
    private Integer timeliness;

    @SerializedName("timelinessString")
    @Expose
    private String timelinessString;

    @SerializedName("currentPointArrival")
    @Expose
    private Long currentPointArrival;

    @SerializedName("currentPointArrivalString")
    @Expose
    private String currentPointArrivalString;

    @SerializedName("currentPointDeparture")
    @Expose
    private Long currentPointDeparture;

    @SerializedName("currentPointDepartureString")
    @Expose
    private String currentPointDepartureString;

    @SerializedName("nextPointArrivalTime")
    @Expose
    private Long nextPointArrivalTime;

    @SerializedName("nextPointArrivalString")
    @Expose
    private String nextPointArrivalString;

    @SerializedName("currentPointOrdinal")
    @Expose
    private Integer currentPointOrdinal;

    @SerializedName("currentPointString")
    @Expose
    private String currentPointString;

    @SerializedName("nextPointOrdinal")
    @Expose
    private Integer nextPointOrdinal;

    @SerializedName("nextPointString")
    @Expose
    private String nextPointString;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("voyageStatus")
    @Expose
    private Boolean voyageStatus;

    public Integer getTimeliness() {
        return timeliness;
    }

    public void setTimeliness(Integer timeliness) {
        this.timeliness = timeliness;
    }

    public String getTimelinessString() {
        return timelinessString;
    }

    public void setTimelinessString(String timelinessString) {
        this.timelinessString = timelinessString;
    }

    public Long getCurrentPointArrival() {
        return currentPointArrival;
    }

    public void setCurrentPointArrival(Long currentPointArrival) {
        this.currentPointArrival = currentPointArrival;
    }

    public String getCurrentPointArrivalString() {
        return currentPointArrivalString;
    }

    public void setCurrentPointArrivalString(String currentPointArrivalString) {
        this.currentPointArrivalString = currentPointArrivalString;
    }

    public Long getCurrentPointDeparture() {
        return currentPointDeparture;
    }

    public void setCurrentPointDeparture(Long currentPointDeparture) {
        this.currentPointDeparture = currentPointDeparture;
    }

    public String getCurrentPointDepartureString() {
        return currentPointDepartureString;
    }

    public void setCurrentPointDepartureString(String currentPointDepartureString) {
        this.currentPointDepartureString = currentPointDepartureString;
    }

    public Long getNextPointArrivalTime() {
        return nextPointArrivalTime;
    }

    public void setNextPointArrivalTime(Long nextPointArrivalTime) {
        this.nextPointArrivalTime = nextPointArrivalTime;
    }

    public String getNextPointArrivalString() {
        return nextPointArrivalString;
    }

    public void setNextPointArrivalString(String nextPointArrivalString) {
        this.nextPointArrivalString = nextPointArrivalString;
    }

    public Integer getCurrentPointOrdinal() {
        return currentPointOrdinal;
    }

    public void setCurrentPointOrdinal(Integer currentPointOrdinal) {
        this.currentPointOrdinal = currentPointOrdinal;
    }

    public String getCurrentPointString() {
        return currentPointString;
    }

    public void setCurrentPointString(String currentPointString) {
        this.currentPointString = currentPointString;
    }

    public Integer getNextPointOrdinal() {
        return nextPointOrdinal;
    }

    public void setNextPointOrdinal(Integer nextPointOrdinal) {
        this.nextPointOrdinal = nextPointOrdinal;
    }

    public String getNextPointString() {
        return nextPointString;
    }

    public void setNextPointString(String nextPointString) {
        this.nextPointString = nextPointString;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getVoyageStatus() {
        return voyageStatus;
    }

    public void setVoyageStatus(Boolean voyageStatus) {
        this.voyageStatus = voyageStatus;
    }
}

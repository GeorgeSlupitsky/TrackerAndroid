package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Point {

    @SerializedName("scheduleId")
    @Expose
    private Long scheduleId;

    @SerializedName("pointName")
    @Expose
    private String pointName;

    @SerializedName("ordinal")
    @Expose
    private Integer ordinal;

    @SerializedName("circle")
    @Expose
    private Integer circle;

    @SerializedName("arrival")
    @Expose
    private Long arrival;

    @SerializedName("departure")
    @Expose
    private Long departure;

    @SerializedName("arrivalString")
    @Expose
    private String arrivalString;

    @SerializedName("departureString")
    @Expose
    private String departureString;

    @SerializedName("arrivalDeflection")
    @Expose
    private Integer arrivalDeflection;

    @SerializedName("routePointId")
    @Expose
    private Long routePointId;

    @SerializedName("geozoneId")
    @Expose
    private Long geozoneId;

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public Integer getCircle() {
        return circle;
    }

    public void setCircle(Integer circle) {
        this.circle = circle;
    }

    public Integer getArrivalDeflection() {
        return arrivalDeflection;
    }

    public void setArrivalDeflection(Integer arrivalDeflection) {
        this.arrivalDeflection = arrivalDeflection;
    }

    public Long getRoutePointId() {
        return routePointId;
    }

    public void setRoutePointId(Long routePointId) {
        this.routePointId = routePointId;
    }

    public Long getGeozoneId() {
        return geozoneId;
    }

    public void setGeozoneId(Long geozoneId) {
        this.geozoneId = geozoneId;
    }

    public Long getArrival() {
        return arrival;
    }

    public void setArrival(Long arrival) {
        this.arrival = arrival;
    }

    public Long getDeparture() {
        return departure;
    }

    public void setDeparture(Long departure) {
        this.departure = departure;
    }

    public String getArrivalString() {
        return arrivalString;
    }

    public void setArrivalString(String arrivalString) {
        this.arrivalString = arrivalString;
    }

    public String getDepartureString() {
        return departureString;
    }

    public void setDepartureString(String departureString) {
        this.departureString = departureString;
    }
}

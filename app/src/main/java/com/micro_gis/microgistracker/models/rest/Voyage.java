package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Voyage {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("voyageName")
    @Expose
    private String voyageName;

    @SerializedName("dateStart")
    @Expose
    private Long dateStart;

    @SerializedName("dateEnd")
    @Expose
    private Long dateEnd;

    @SerializedName("routeId")
    @Expose
    private Integer routeId;

    @SerializedName("routeName")
    @Expose
    private String routeName;

    @SerializedName("scheduleId")
    @Expose
    private Long scheduleId;

    @SerializedName("scheduleName")
    @Expose
    private String scheduleName;

    @SerializedName("messageByTime")
    @Expose
    private Map<Long, String> messageByTime;

    @SerializedName("active")
    @Expose
    private boolean active;

    @SerializedName("deviceName")
    @Expose
    private String deviceName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoyageName() {
        return voyageName;
    }

    public void setVoyageName(String voyageName) {
        this.voyageName = voyageName;
    }

    public Long getDateStart() {
        return dateStart;
    }

    public void setDateStart(Long dateStart) {
        this.dateStart = dateStart;
    }

    public Long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public Map<Long, String> getMessageByTime() {
        return messageByTime;
    }

    public void setMessageByTime(Map<Long, String> messageByTime) {
        this.messageByTime = messageByTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}

package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User9 on 16.03.2018.
 */

public class ResponseDeviceStatus {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("dataList")
    @Expose
    private List<DeviceStatus> deviceStatuses;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DeviceStatus> getDeviceStatuses() {
        return deviceStatuses;
    }

    public void setDeviceStatuses(List<DeviceStatus> deviceStatuses) {
        this.deviceStatuses = deviceStatuses;
    }
}

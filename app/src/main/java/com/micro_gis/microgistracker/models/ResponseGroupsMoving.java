package com.micro_gis.microgistracker.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by postp on 24.02.2018.
 */

public class ResponseGroupsMoving {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("devices")
    @Expose
    private List<Device> devices;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}

package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User9 on 02.03.2018.
 */

public class ResponseObjectMoving {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("device")
    @Expose
    private Device device;

    @SerializedName("warning")
    @Expose
    private List<String> warnings;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }
}

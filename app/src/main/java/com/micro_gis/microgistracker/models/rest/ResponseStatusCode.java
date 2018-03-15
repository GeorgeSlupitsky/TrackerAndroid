package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User9 on 15.03.2018.
 */

public class ResponseStatusCode {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("dataList")
    @Expose
    private List<StatusCode> statusCodes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<StatusCode> getStatusCodes() {
        return statusCodes;
    }

    public void setStatusCodes(List<StatusCode> statusCodes) {
        this.statusCodes = statusCodes;
    }
}

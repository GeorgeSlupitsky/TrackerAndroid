package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseCheckPassword {

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

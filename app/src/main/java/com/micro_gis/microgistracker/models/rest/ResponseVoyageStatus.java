package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseVoyageStatus {


    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("voyageDetail")
    @Expose
    private VoyageDetail voyageDetail;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VoyageDetail getVoyageDetail() {
        return voyageDetail;
    }

    public void setVoyageDetail(VoyageDetail voyageDetail) {
        this.voyageDetail = voyageDetail;
    }
}

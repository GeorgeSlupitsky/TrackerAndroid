package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User9 on 16.03.2018.
 */

public class RequestSaveStatus {

    @SerializedName("account")
    @Expose
    private String account;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("statusId")
    @Expose
    private Integer statusId;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
}

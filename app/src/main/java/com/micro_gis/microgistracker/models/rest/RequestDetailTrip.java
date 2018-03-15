package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User9 on 13.03.2018.
 */

public class RequestDetailTrip {

    @SerializedName("account")
    @Expose
    private String account;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("dateFrom")
    @Expose
    private long dateFrom;

    @SerializedName("dateTo")
    @Expose
    private long dateTo;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(long dateFrom) {
        this.dateFrom = dateFrom;
    }

    public long getDateTo() {
        return dateTo;
    }

    public void setDateTo(long dateTo) {
        this.dateTo = dateTo;
    }
}

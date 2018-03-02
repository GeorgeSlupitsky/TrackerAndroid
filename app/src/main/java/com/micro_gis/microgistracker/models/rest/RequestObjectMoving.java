package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User9 on 02.03.2018.
 */

public class RequestObjectMoving {

    @SerializedName("account")
    @Expose
    private String account;

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("useGeocoder")
    @Expose
    private boolean useGeocoder;

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

    public boolean isUseGeocoder() {
        return useGeocoder;
    }

    public void setUseGeocoder(boolean useGeocoder) {
        this.useGeocoder = useGeocoder;
    }
}

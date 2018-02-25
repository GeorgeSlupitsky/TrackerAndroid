package com.micro_gis.microgistracker.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by postp on 24.02.2018.
 */

public class Account {

    @SerializedName("account")
    @Expose
    private String account;

    @SerializedName("useGeocoder")
    @Expose
    private boolean useGeocoder;

    @SerializedName("groups")
    @Expose
    private List <String> groups;

    public boolean isUseGeocoder() {
        return useGeocoder;
    }

    public void setUseGeocoder(boolean useGeocoder) {
        this.useGeocoder = useGeocoder;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}

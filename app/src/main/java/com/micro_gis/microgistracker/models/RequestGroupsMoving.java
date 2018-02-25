package com.micro_gis.microgistracker.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by postp on 24.02.2018.
 */

public class RequestGroupsMoving {

    @SerializedName("accounts")
    @Expose
    private List<Account> accounts;

    @SerializedName("key")
    @Expose
    private String key;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

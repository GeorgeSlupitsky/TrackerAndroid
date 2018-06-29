package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestDriverEvents {

    @SerializedName("id")
    @Expose
    private Integer login;

    @SerializedName("password")
    @Expose
    private String password;

    public Integer getLogin() {
        return login;
    }

    public void setLogin(Integer login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

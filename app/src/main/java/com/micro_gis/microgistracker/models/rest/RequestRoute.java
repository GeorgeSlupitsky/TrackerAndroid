package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestRoute {

    @SerializedName("id")
    @Expose
    private Integer login;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("voyageId")
    @Expose
    private Long voyageId;

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

    public Long getVoyageId() {
        return voyageId;
    }

    public void setVoyageId(Long voyageId) {
        this.voyageId = voyageId;
    }
}

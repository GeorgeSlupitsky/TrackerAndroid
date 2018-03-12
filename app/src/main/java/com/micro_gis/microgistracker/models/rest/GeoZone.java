package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User9 on 12.03.2018.
 */

public class GeoZone {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("geom")
    @Expose
    private String geom;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("priority")
    @Expose
    private int priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

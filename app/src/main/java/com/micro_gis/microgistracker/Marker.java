package com.micro_gis.microgistracker;

/**
 * Created by oleg on 30.06.16.
 */
public class Marker {
    private String name;
    private String latlng;
    private String url;
    private String shadowUrl;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShadowUrl() {
        return shadowUrl;
    }

    public void setShadowUrl(String shadowUrl) {
        this.shadowUrl = shadowUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }
}

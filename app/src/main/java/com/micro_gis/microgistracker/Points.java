package com.micro_gis.microgistracker;

/**
 * Created by oleg on 13.06.16.
 */
class Points{
    String lat, lon;
    Points(String lat, String lon){
        this.lat=lat;
        this.lon=lon;
    }

    @Override
    public String toString() {
        return "["+lat+","+lon+"]";
    }
}
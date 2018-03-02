package com.micro_gis.microgistracker.models.rest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by postp on 24.02.2018.
 */

public class Device {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("driverName")
    @Expose
    private String driverName;

    @SerializedName("organization")
    @Expose
    private String organization;

    @SerializedName("plate")
    @Expose
    private String plate;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;

    @SerializedName("speed")
    @Expose
    private double speed;

    @SerializedName("event")
    @Expose
    private long event;

    @SerializedName("deflection")
    @Expose
    private int deflection;

    @SerializedName("heading")
    @Expose
    private int heading;

    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("aclass")
    @Expose
    private String aclass;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("statusCode")
    @Expose
    private int statusCode;

    @SerializedName("wifi")
    @Expose
    private boolean wifi;

    @SerializedName("lowFlor")
    @Expose
    private boolean lowFlor;

    @SerializedName("trailer")
    @Expose
    private String trailer;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("altitude")
    @Expose
    private int altitude;

    @SerializedName("satCount")
    @Expose
    private int satCount;

    @SerializedName("hdop")
    @Expose
    private double hdop;

    @SerializedName("fuelExpense")
    @Expose
    private double fuelExpense;

    @SerializedName("fuelLevel")
    @Expose
    private double fuelLevel;

    @SerializedName("sensors")
    @Expose
    private Map<String, String> sensors;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public long getEvent() {
        return event;
    }

    public void setEvent(long event) {
        this.event = event;
    }

    public int getDeflection() {
        return deflection;
    }

    public void setDeflection(int deflection) {
        this.deflection = deflection;
    }

    public int getHeading() {
        return heading;
    }

    public void setHeading(int heading) {
        this.heading = heading;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAclass() {
        return aclass;
    }

    public void setAclass(String aclass) {
        this.aclass = aclass;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isLowFlor() {
        return lowFlor;
    }

    public void setLowFlor(boolean lowFlor) {
        this.lowFlor = lowFlor;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public int getSatCount() {
        return satCount;
    }

    public void setSatCount(int satCount) {
        this.satCount = satCount;
    }

    public double getHdop() {
        return hdop;
    }

    public void setHdop(double hdop) {
        this.hdop = hdop;
    }

    public double getFuelExpense() {
        return fuelExpense;
    }

    public void setFuelExpense(double fuelExpense) {
        this.fuelExpense = fuelExpense;
    }

    public double getFuelLevel() {
        return fuelLevel;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public Map<String, String> getSensors() {
        return sensors;
    }

    public void setSensors(Map<String, String> sensors) {
        this.sensors = sensors;
    }
}

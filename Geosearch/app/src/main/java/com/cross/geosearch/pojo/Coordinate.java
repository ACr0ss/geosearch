package com.cross.geosearch.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Coordinate implements Serializable {
    @Expose
    @SerializedName("lat")
    public double lat;

    @Expose
    @SerializedName("lon")
    public double lon;

    @Expose
    @SerializedName("primary")
    public String primary;

    @Expose
    @SerializedName("globe")
    public String globe;

    public Coordinate(double lat, double lon, String primary, String globe) {
        this.lat = lat;
        this.lon = lon;
        this.primary = primary;
        this.globe = globe;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getGlobe() {
        return globe;
    }

    public void setGlobe(String globe) {
        this.globe = globe;
    }
}

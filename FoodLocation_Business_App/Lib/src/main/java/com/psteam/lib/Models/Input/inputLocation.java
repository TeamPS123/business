package com.psteam.lib.Models.Input;

public class inputLocation {
    public inputLocation(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    private double lon;
    private double lat;
}

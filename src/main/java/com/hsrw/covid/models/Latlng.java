package com.hsrw.covid.models;

import java.io.Serializable;

public class Latlng implements Serializable {
    private double lat;
    private double lon;

    public Latlng(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}

package com.hsrw.covid.models;


import java.io.Serializable;

public class Region implements Serializable {
    private String name;
    private String iso;
    private String province;



    private double lat;
    private double lon;

    public Region(String name, String iso, String province, double lat, double lon) {
        this.name = name;
        this.iso = iso;
        this.lat = lat;
        this.lon = lon;
        this.province=province;
    }

    public String getName() {
        return name;
    }

    public String getProvince() {
        return province;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}

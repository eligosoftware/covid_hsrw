package com.hsrw.covid.models;

import java.util.ArrayList;

public class Region {
    private String name;
    private String iso;
    private String province;

    private ArrayList<City> cities;


    private double lat;
    private double lon;

    public Region(String name, String iso, String province, double lat, double lon) {
        this.name = name;
        this.iso = iso;
        this.lat = lat;
        this.lon = lon;
        this.province=province;
        cities=new ArrayList();
    }

    public void addCity(City city){
        cities.add(city);
    }
}

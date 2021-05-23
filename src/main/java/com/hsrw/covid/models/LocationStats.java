package com.hsrw.covid.models;

import java.util.Date;

public class LocationStats {

    private Region region;
    private City city;
    private Date date;
    private Date last_update;
    private int totalDeaths;
    private int totalConfirmed;
    private int totalRecovered;
    private int totalActive;
    private int active;
    private int deaths;
    private int confirmed;
    private int recovered;
    private double fatality_rate;

    public LocationStats(Region region, City city, Date date,Date last_update, int totalDeaths, int totalConfirmed, int totalRecovered, int totalActive, int active, int deaths, int confirmed, int recovered, double fatality_rate) {
        this.region = region;
        this.city = city;
        this.date = date;
        this.last_update=last_update;
        this.totalDeaths = totalDeaths;
        this.totalConfirmed = totalConfirmed;
        this.totalRecovered = totalRecovered;
        this.totalActive = totalActive;
        this.active = active;
        this.deaths = deaths;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.fatality_rate = fatality_rate;
    }

    @Override
    public String toString() {
        return "LocationStats{" +
                "region='" + region + '\'' +","+
                "city='" + city + '\''+
                '}';
    }

    public Region getRegion() {
        return region;
    }

    public City getCity() {
        return city;
    }

    public int getTotalConfirmed() {
        return totalConfirmed;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public Date getDate() {
        return date;
    }

    public Date getLast_update() {
        return last_update;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public int getTotalActive() {
        return totalActive;
    }

    public int getActive() {
        return active;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getRecovered() {
        return recovered;
    }

    public double getFatality_rate() {
        return fatality_rate;
    }
}

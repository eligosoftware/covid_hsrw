package com.hsrw.covid.models;

import java.util.Date;

public class LocationStats {

    private Region region;
    private City city;
    private double Lat;
    private Date date;
    private int totalDeaths;
    private int totalConfirmed;
    private int totalRecovered;
    private int totalActive;
    private int active;
    private int deaths;
    private int confirmed;
    private int recovered;
    private double fatality_rate;


    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    private double Long;

    private int latestTotalCases;
    private int diffFromPreviousDay;

    public LocationStats(Region region, City city, double lat, Date date, int totalDeaths, int totalConfirmed, int totalRecovered, int totalActive, int active, int deaths, int confirmed, int recovered, double fatality_rate, double aLong, int latestTotalCases, int diffFromPreviousDay) {
        this.region = region;
        this.city = city;
        this.Lat = lat;
        this.date = date;
        this.totalDeaths = totalDeaths;
        this.totalConfirmed = totalConfirmed;
        this.totalRecovered = totalRecovered;
        this.totalActive = totalActive;
        this.active = active;
        this.deaths = deaths;
        this.confirmed = confirmed;
        this.recovered = recovered;
        this.fatality_rate = fatality_rate;
        this.Long = aLong;
        this.latestTotalCases = latestTotalCases;
        this.diffFromPreviousDay = diffFromPreviousDay;
    }

    @Override
    public String toString() {
        return "LocationStats{" +
                "region='" + region + '\'' +","+
                "city='" + city + '\'' +","+
                ", latestTotalCases=" + latestTotalCases +
                '}';
    }



    public int getLatestTotalCases() {
        return latestTotalCases;
    }

    public void setLatestTotalCases(int latestTotalCases) {
        this.latestTotalCases = latestTotalCases;
    }

    public int getDiffFromPreviousDay() {
        return diffFromPreviousDay;
    }

    public void setDiffFromPreviousDay(int diffFromPreviousDay) {
        this.diffFromPreviousDay = diffFromPreviousDay;
    }
}

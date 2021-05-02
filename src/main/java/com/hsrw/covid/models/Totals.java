package com.hsrw.covid.models;

import java.util.Date;

public class Totals {
    private Date forDate;
    private int newCases;
    private int newDeaths;
    private int newRecovered;
    private double fatalityRate;
    private Date updatedAt;

    private int totalCases;
    private int totalDeaths;
    private int totalActive;
    private int totalRecovered;

    public Totals(Date forDate, int newCases, int newDeaths, int newRecovered, double fatalityRate, Date updatedAt, int totalCases, int totalDeaths, int totalActive, int totalRecovered) {
        this.forDate = forDate;
        this.newCases = newCases;
        this.newDeaths = newDeaths;
        this.newRecovered = newRecovered;
        this.fatalityRate = fatalityRate;
        this.updatedAt = updatedAt;
        this.totalCases = totalCases;
        this.totalDeaths = totalDeaths;
        this.totalActive = totalActive;
        this.totalRecovered = totalRecovered;
    }
}

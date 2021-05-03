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

    public int getTotalCases() {
        return totalCases;
    }

    public Date getForDate() {
        return forDate;
    }

    public int getNewCases() {
        return newCases;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public int getNewRecovered() {
        return newRecovered;
    }

    public double getFatalityRate() {
        return fatalityRate;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int getTotalActive() {
        return totalActive;
    }

    public int getTotalRecovered() {
        return totalRecovered;
    }

    public Totals(int totalDeaths, int totalActive, int totalRecovered) {
        this.totalDeaths = totalDeaths;
        this.totalActive = totalActive;
        this.totalRecovered = totalRecovered;
    }

    public int getTotalDeaths() {
        return totalDeaths;
    }
}

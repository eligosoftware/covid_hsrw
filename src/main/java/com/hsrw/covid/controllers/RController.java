package com.hsrw.covid.controllers;

import com.hsrw.covid.models.LocationStats;
import com.hsrw.covid.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@RestController
public class RController {

    @Autowired
    DataService covidDataService;

    @PostMapping("/updateNumbers")
    public String updateNumbers(@RequestParam String country ){

        if(country.equals("europe") ||
                country.equals("asia") ||
                country.equals("africa") ||
                country.equals("oceania") ||
                country.equals("north_america") ||
                country.equals("south_america")){

            try {
                List<LocationStats> regionStats = covidDataService.getStatsCountry_by_continent(country);

                double fatality_rate=0;
                int new_confirmed=0;
                int new_active=0;
                int new_recovered=0;
                int new_deaths=0;
                int total_confirmed=0;
                int total_active=0;
                int total_recovered=0;
                int total_deaths=0;
                for(LocationStats stat:regionStats){
                    new_active+=stat.getActive();
                    new_recovered+=stat.getRecovered();
                    new_deaths+=stat.getDeaths();
                    new_confirmed+=stat.getConfirmed();
                    total_confirmed+=stat.getTotalConfirmed();
                    total_active+=stat.getTotalActive();
                    total_deaths+=stat.getTotalDeaths();
                    total_recovered+=stat.getTotalRecovered();
                    fatality_rate+=stat.getFatality_rate();
                }


                StringBuilder builder = new StringBuilder();
                builder.append("{");//1
                builder.append("\"country\":\""+country+"\",");
                builder.append("\"new_cases\":"+new_confirmed+",");
                builder.append("\"new_active\":"+new_active+",");
                builder.append("\"new_recovered\":"+new_recovered+",");
                builder.append("\"new_deaths\":"+new_deaths+",");
                builder.append("\"total_cases\":"+total_confirmed+",");
                builder.append("\"total_deaths\":"+total_deaths+",");
                builder.append("\"total_recovered\":"+total_recovered+",");
                builder.append("\"total_active\":"+total_active+",");
                builder.append("\"fatalityRate\":"+String.format("%.3g%n", fatality_rate/regionStats.size())+",");

                builder.append("\"chart1\":{");//2
                builder.append("\"update\":true,");

                Collections.sort(regionStats, new Comparator<LocationStats>() {
                    @Override
                    public int compare(LocationStats o1, LocationStats o2) {
                        return o2.getTotalConfirmed() - o1.getTotalConfirmed();
                    }
                });
                builder.append("\"values\":[");//3
                LocationStats region;
                for(int i=0;i<9;i++){
                    region=regionStats.get(i);
                    builder.append("{");//4
                    builder.append("\"name\":\""+region.getRegion().getName()+"\",");
                    builder.append("\"y\": "+region.getTotalConfirmed());
                    builder.append("},");//4
                }
                builder.append("{");//4
                region=regionStats.get(9);
                builder.append("\"name\":\""+region.getRegion().getName()+"\",");
                builder.append("\"y\": "+region.getTotalConfirmed());
                builder.append("}");//4

                builder.append("]");//3
                builder.append("},");//2

                Map<String,Integer> hashmap = covidDataService.getStatsTotal_14days_continents(country);

                builder.append("\"chart2\":{");//2
                builder.append("\"update\":true,");
                builder.append("\"values\":[");//3

                String daily_plots="";
                // setting up iterator.
                SortedSet<String> keys = new TreeSet<>(hashmap.keySet());
                for (String key : keys) {
                    // do something
                    builder.append("{");//4
                    builder.append("\"key\":\""+key+"\",");
                    builder.append("\"value\": "+hashmap.get(key));
                    builder.append("},");//4
                }

                builder.append("]");//3
                builder.append("}");//2
                builder.append("}");

                return builder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "error";
        }
        else{

        String country_param=country.replace(" ","%20");
        try {
            List<LocationStats> regionStats = covidDataService.getStatsCountry_by_country(country_param);

            LocationStats stat=regionStats.get(0);
            StringBuilder builder = new StringBuilder();

                builder.append("{");
                builder.append("\"country\":\""+country+"\",");
                builder.append("\"new_cases\":"+stat.getConfirmed()+",");
                builder.append("\"new_active\":"+stat.getActive()+",");
                builder.append("\"new_recovered\":"+stat.getRecovered()+",");
                builder.append("\"new_deaths\":"+stat.getDeaths()+",");
                builder.append("\"total_cases\":"+stat.getTotalConfirmed()+",");
                builder.append("\"total_deaths\":"+stat.getTotalDeaths()+",");
                builder.append("\"total_recovered\":"+stat.getTotalRecovered()+",");
                builder.append("\"total_active\":"+stat.getTotalActive()+",");
                builder.append("\"fatalityRate\":"+String.format("%.3g%n", stat.getFatality_rate())+",");

            builder.append("\"chart1\":{");//2
            builder.append("\"update\":false");
            builder.append("},");//2

            Map<String,Integer> hashmap = covidDataService.getStatsTotal_14days_country(country);

            builder.append("\"chart2\":{");//2
            builder.append("\"update\":true,");
            builder.append("\"values\":[");//3

            String daily_plots="";
            // setting up iterator.
            SortedSet<String> keys = new TreeSet<>(hashmap.keySet());
            for (String key : keys) {
                // do something
                builder.append("{");//4
                builder.append("\"key\":\""+key+"\",");
                builder.append("\"value\": "+hashmap.get(key));
                builder.append("},");//4
            }

            builder.append("]");//3
            builder.append("}");//2
                builder.append("}");

            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        }
        return "error";
    }
}

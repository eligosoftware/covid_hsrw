package com.hsrw.covid.controllers;

import com.hsrw.covid.models.LineChartResult;
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
                List<LocationStats> regionStats = covidDataService.getData_locstat_list(country,2);//covidDataService.getStatsCountry_by_continent(country);

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

                int upperbound=9;
                if(regionStats.size()<10)
                    upperbound=regionStats.size()-1;
                for(int i=0;i<upperbound;i++){
                    region=regionStats.get(i);
                    builder.append("{");//4
                    builder.append("\"name\":\""+region.getRegion().getName()+"\",");
                    builder.append("\"y\": "+region.getTotalConfirmed());
                    builder.append("},");//4
                }
                builder.append("{");//4
                region=regionStats.get(upperbound);
                builder.append("\"name\":\""+region.getRegion().getName()+"\",");
                builder.append("\"y\": "+region.getTotalConfirmed());
                builder.append("}");//4

                builder.append("]");//3
                builder.append("},");//2

                LineChartResult result = covidDataService.getData_lin_chart_data(country,8);// covidDataService.getStatsTotal_14days_continents(country);
                Map<String,Integer> hashmap=result.getMap_cases();
                builder.append("\"chart2\":{");//2
                builder.append("\"update\":true,");
                builder.append("\"values\":[");//3


                SortedSet<String> keys = new TreeSet<>(hashmap.keySet());
                for (String key : keys) {
                    // do something
                    builder.append("{");//4
                    builder.append("\"key\":\""+key+"\",");
                    builder.append("\"value\": "+hashmap.get(key));
                    builder.append("},");//4
                }

                builder.append("]");//3
                builder.append("},");//2

                //chart3

                builder.append("\"chart3\":{");//2
                builder.append("\"update\":true,");
                builder.append("\"values\":[");//3

                Collections.sort(regionStats, new Comparator<LocationStats>() {
                    @Override
                    public int compare(LocationStats o1, LocationStats o2) {
                        return (int) (o2.getConfirmed_pro_100k() - o1.getConfirmed_pro_100k());
                    }
                });
                if(regionStats.size()<10)
                    upperbound=regionStats.size()-1;
                for(int i=0;i<upperbound;i++){
                    region=regionStats.get(i);
                    builder.append("{");//4
                    builder.append("\"key\":\""+region.getRegion().getName()+"\",");
                    builder.append("\"value\": "+region.getConfirmed_pro_100k());
                    builder.append("},");//4
                }
                builder.append("{");//4
                region=regionStats.get(upperbound);
                builder.append("\"key\":\""+region.getRegion().getName()+"\",");
                builder.append("\"value\": "+region.getConfirmed_pro_100k());
                builder.append("}");//4

                builder.append("]");//3
                builder.append("},");//2


                //chart4
                Map<String,Double> hashmap2 =result.getMap_ratio();
                builder.append("\"chart4\":{");//2
                builder.append("\"update\":true,");
                builder.append("\"values\":[");//3


                keys = new TreeSet<>(hashmap2.keySet());
                for (String key : keys) {
                    // do something
                    builder.append("{");//4
                    builder.append("\"key\":\""+key+"\",");
                    builder.append("\"value\": "+hashmap2.get(key));
                    builder.append("},");//4
                }
                builder.setLength(builder.length() - 1);
                builder.append("]");//3
                builder.append("},");//2


                //chart5

                builder.append("\"chart5\":{");//2
                builder.append("\"update\":true,");
                builder.append("\"values\":[");//3

                LineChartResult res2=covidDataService.getData_lin_chart_data(country,4);// covidDataService.getDeaths_14days_continent(country);
                Map<String,Integer> map_deaths=res2.getMap_cases();
                SortedSet<String> keys2 = new TreeSet<>(map_deaths.keySet());
                for (String key : keys2) {
                    // do something
                    builder.append("{");//4
                    builder.append("\"key\":\""+key+"\",");
                    builder.append("\"value\": "+map_deaths.get(key));
                    builder.append("},");//4
                }
                builder.setLength(builder.length() - 1);
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
            List<LocationStats> regionStats = covidDataService.getData_locstat_list(country_param,1); //covidDataService.getStatsCountry_by_country(country_param);

            LocationStats stat=regionStats.get(0);

            if(stat==null)
                return "error";

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
            builder.append("\"update\":true,");//dcdc

            String continent=covidDataService.getContinentByCountry(country);
            List<LocationStats> regionStatsContinent =covidDataService.getData_locstat_list(continent,2);//  covidDataService.getStatsCountry_by_continent(continent);

            Collections.sort(regionStatsContinent, new Comparator<LocationStats>() {
                @Override
                public int compare(LocationStats o1, LocationStats o2) {
                    return o2.getTotalConfirmed() - o1.getTotalConfirmed();
                }
            });
            builder.append("\"values\":[");//3
            LocationStats region;
            int upperbound=9;
            if(regionStatsContinent.size()<10)
                upperbound=regionStatsContinent.size()-1;
            for(int i=0;i<upperbound;i++){
                region=regionStatsContinent.get(i);
                builder.append("{");//4
                builder.append("\"name\":\""+region.getRegion().getName()+"\",");
                builder.append("\"y\": "+region.getTotalConfirmed());
                builder.append("},");//4
            }
            builder.append("{");//4
            region=regionStatsContinent.get(upperbound);
            builder.append("\"name\":\""+region.getRegion().getName()+"\",");
            builder.append("\"y\": "+region.getTotalConfirmed());
            builder.append("}");//4

            builder.append("]");//3
            builder.append("},");//2

            LineChartResult result =covidDataService.getData_lin_chart_data(country.replace(" ","%20"),7); //covidDataService.getStatsTotal_14days_country(country.replace(" ","%20"));
            Map<String,Integer> hashmap =result.getMap_cases();
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
            builder.append("},");//2

            //chart3

            builder.append("\"chart3\":{");//2
            builder.append("\"update\":true,");
            builder.append("\"values\":[");//3

            Collections.sort(regionStatsContinent, new Comparator<LocationStats>() {
                @Override
                public int compare(LocationStats o1, LocationStats o2) {
                    return (int) (o2.getConfirmed_pro_100k() - o1.getConfirmed_pro_100k());
                }
            });
            if(regionStatsContinent.size()<10)
                upperbound=regionStatsContinent.size()-1;
            for(int i=0;i<upperbound;i++){
                region=regionStatsContinent.get(i);
                builder.append("{");//4
                builder.append("\"key\":\""+region.getRegion().getName()+"\",");
                builder.append("\"value\": "+region.getConfirmed_pro_100k());
                builder.append("},");//4
            }
            builder.append("{");//4
            region=regionStatsContinent.get(upperbound);
            builder.append("\"key\":\""+region.getRegion().getName()+"\",");
            builder.append("\"value\": "+region.getConfirmed_pro_100k());
            builder.append("}");//4

            builder.append("]");//3
            builder.append("},");//2


            //chart4
            Map<String,Double> hashmap2 =result.getMap_ratio();
            builder.append("\"chart4\":{");//2
            builder.append("\"update\":true,");
            builder.append("\"values\":[");//3


            keys = new TreeSet<>(hashmap2.keySet());
            for (String key : keys) {
                // do something
                builder.append("{");//4
                builder.append("\"key\":\""+key+"\",");
                builder.append("\"value\": "+hashmap2.get(key));
                builder.append("},");//4
            }
            builder.setLength(builder.length() - 1);
            builder.append("]");//3
            builder.append("},");//2


            //chart5

            builder.append("\"chart5\":{");//2
            builder.append("\"update\":true,");
            builder.append("\"values\":[");//3

            LineChartResult res2=covidDataService.getData_lin_chart_data(country.replace(" ","%20"),5); //covidDataService.getDeaths_14days_country(country.replace(" ","%20"));
            Map<String,Integer> map_deaths=res2.getMap_cases();
            SortedSet<String> keys2 = new TreeSet<>(map_deaths.keySet());
            for (String key : keys2) {
                // do something
                builder.append("{");//4
                builder.append("\"key\":\""+key+"\",");
                builder.append("\"value\": "+map_deaths.get(key));
                builder.append("},");//4
            }
            builder.setLength(builder.length() - 1);
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

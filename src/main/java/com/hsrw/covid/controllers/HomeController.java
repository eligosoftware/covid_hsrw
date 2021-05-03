package com.hsrw.covid.controllers;

import com.hsrw.covid.database.config;
import com.hsrw.covid.models.LocationStats;
import com.hsrw.covid.models.Totals;
import com.hsrw.covid.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class HomeController {

    @Autowired
    DataService covidDataService;

    @GetMapping("/")
    public String home(Model model){
        config conf=config.newInstance();
        conf.connect();
        List<LocationStats> regionStats = covidDataService.getRegionStats();
        List<LocationStats> cityStats = covidDataService.getCityStats();
        List<LocationStats> allStats = Stream.concat(regionStats.stream(),cityStats.stream()).collect(Collectors.toList());
        Totals totals=covidDataService.getmTotals();
        int totalCases=totals.getTotalCases();//allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
        int totalNewCases=totals.getNewCases();//allStats.stream().mapToInt(stat->stat.getDiffFromPreviousDay()).sum();
        int totalDeaths=totals.getTotalDeaths();
        int totalRecovered=totals.getTotalRecovered();
        int totalActive=totals.getTotalActive();

        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases",totalCases);
        model.addAttribute("totalNewCases",totalNewCases);
        model.addAttribute("totalDeaths",totalDeaths);
        model.addAttribute("totalRecovered",totalRecovered);
        model.addAttribute("totalActive",totalActive);

        StringBuilder builder = new StringBuilder();

        builder.append("[");
        for( LocationStats stat:allStats){
            builder.append("{");
            builder.append("\"country\":\""+stat.getRegion().getName()+"\",");
            builder.append("\"province\":\""+stat.getRegion().getProvince()+"\",");
            builder.append("\"city\":\""+((stat.getCity()==null)?"":stat.getCity().getName())+"\",");

            builder.append("\"lat\":"+((stat.getCity()==null)?stat.getRegion().getLat():stat.getCity().getLat())+",");
            builder.append("\"long\":"+((stat.getCity()==null)?stat.getRegion().getLon():stat.getCity().getLon())+",");

            builder.append("\"ltc\":"+stat.getTotalConfirmed()+"");

            builder.append("},");
        }
        builder.setLength(builder.length() - 1);
        builder.append("]");
        //System.out.println(builder.toString());
        //System.out.println(JSONArray.toJSONString(allStats));
        System.out.println(builder.toString());
        //model.addAttribute("json", JSONArray.toJSONString(allStats));//builder.toString());
        model.addAttribute("json", builder.toString());
        model.addAttribute("max_stat",covidDataService.getMax_stat());

        conf.disconnect();
        return "home";
    }
}

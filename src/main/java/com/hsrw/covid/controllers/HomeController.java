package com.hsrw.covid.controllers;

import com.hsrw.covid.database.config;
import com.hsrw.covid.models.LocationStats;
import com.hsrw.covid.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    DataService covidDataService;

    @GetMapping("/")
    public String home(Model model){
        config conf=config.newInstance();
        conf.connect();
        List<LocationStats> allStats = covidDataService.getAllStats();
        int totalCases=allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
        int totalNewCases=allStats.stream().mapToInt(stat->stat.getDiffFromPreviousDay()).sum();

        model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases",totalCases);
        model.addAttribute("totalNewCases",totalNewCases);

        StringBuilder builder = new StringBuilder();

        builder.append("[");
        for( LocationStats stat:allStats){
            builder.append("{");
            //builder.append("\"state\":\""+stat.getState()+"\",");
            //builder.append("\"country\":\""+stat.getCountry()+"\",");
            builder.append("\"lat\":"+stat.getLat()+",");
            builder.append("\"long\":"+stat.getLong()+",");

            builder.append("\"ltc\":"+stat.getLatestTotalCases()+"");

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

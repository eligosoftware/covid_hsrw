package com.hsrw.covid.controllers;

import com.hsrw.covid.models.LineChartResult;
import com.hsrw.covid.models.LocationStats;
import com.hsrw.covid.models.Totals;
import com.hsrw.covid.services.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    DataService covidDataService;




    @GetMapping("/")
    public String home(Model model){
       // config conf=config.newInstance();
        //conf.connect();
        List<LocationStats> regionStats = covidDataService.getRegionStatsCountry();
      //  List<LocationStats> cityStats = covidDataService.getCityStats();
       // List<LocationStats> allStats = Stream.concat(regionStats.stream(),cityStats.stream()).collect(Collectors.toList());
        Totals totals=covidDataService.getmTotals();
        int totalNewCases=totals.getNewCases();//allStats.stream().mapToInt(stat->stat.getDiffFromPreviousDay()).sum();
        int totalNewActive=totals.getNewActive();
        int totalNewRecovered=totals.getNewRecovered();
        int totalNewDeaths=totals.getNewDeaths();
        int totalCases=totals.getTotalCases();//allStats.stream().mapToInt(stat->stat.getLatestTotalCases()).sum();
        int totalDeaths=totals.getTotalDeaths();
        int totalRecovered=totals.getTotalRecovered();
        int totalActive=totals.getTotalActive();
        double fatalityRate=totals.getFatalityRate();
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');

        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);


        model.addAttribute("new_cases",formatter.format(totalNewCases));
        model.addAttribute("new_active",formatter.format(totalNewActive));
        model.addAttribute("new_recovered",formatter.format(totalNewRecovered));
        model.addAttribute("new_deaths",formatter.format(totalNewDeaths));
        model.addAttribute("total_cases",formatter.format(totalCases));
        model.addAttribute("total_deaths",formatter.format(totalDeaths));
        model.addAttribute("total_recovered",formatter.format(totalRecovered));
        model.addAttribute("total_active",formatter.format(totalActive));
        model.addAttribute("fatalityRate",fatalityRate);




       // System.out.println(js_script);



        model.addAttribute("locationStats", regionStats);
       // model.addAttribute("locationStats", allStats);
        model.addAttribute("totalReportedCases",formatter.format(totalCases));
        model.addAttribute("totalNewCases",formatter.format(totalNewCases));
        model.addAttribute("totalDeaths",totalDeaths);
        model.addAttribute("totalRecovered",totalRecovered);
        model.addAttribute("totalActive",totalActive);

        StringBuilder builder = new StringBuilder();

        builder.append("[");
        for( LocationStats stat:regionStats){
            builder.append("{");
            builder.append("\"country\":\""+stat.getRegion().getName()+"\",");
           // builder.append("\"province\":\""+stat.getRegion().getProvince()+"\",");
           // builder.append("\"city\":\""+((stat.getCity()==null)?"":stat.getCity().getName())+"\",");

            builder.append("\"lat\":"+stat.getRegion().getLat()+",");
            builder.append("\"long\":"+stat.getRegion().getLon()+",");

        //    builder.append("\"lat\":"+((stat.getCity()==null)?stat.getRegion().getLat():stat.getCity().getLat())+",");
        //    builder.append("\"long\":"+((stat.getCity()==null)?stat.getRegion().getLon():stat.getCity().getLon())+",");

            builder.append("\"ltc\":"+stat.getTotalConfirmed()+"");

            builder.append("},");
        }
        builder.setLength(builder.length() - 1);
        builder.append("]");
        //System.out.println(builder.toString());
        //System.out.println(JSONArray.toJSONString(allStats));
       // System.out.println(builder.toString());
        //model.addAttribute("json", JSONArray.toJSONString(allStats));//builder.toString());
        model.addAttribute("json", builder.toString());
        model.addAttribute("max_stat",covidDataService.getMax_stat());


        Collections.sort(regionStats, new Comparator<LocationStats>() {
            @Override
            public int compare(LocationStats o1, LocationStats o2) {
                return o2.getTotalConfirmed() - o1.getTotalConfirmed();
            }
        });
        String js_script=
                "var chart = JSC.chart('chartDiv', {" +
                        "debug: true," +
                        "legend_position: 'inside left bottom'," +
                        "defaultSeries: { type: 'pie', pointSelection: true }," +
                        "defaultPoint_label: {" +
                        "text: '<b>%name</b>'," +
                        "placement: 'auto'," +
                        "autoHide: false" +
                        "}," +
                        "toolbar_items: {" +
                        "Mode: {" +
                        "margin: 10," +
                        "type: 'select'," +
                        "events_change: setMode," +
                        "items: 'enum_placement'" +
                        "}," +
                        "'Auto Hide': { type: 'checkbox', events_change: setAutoHide }" +
                        "}," +
                        "title_label_text: 'Top 10 countries by case numbers'," +
                        "yAxis: { label_text: 'Cases', formatString: 'n' }," +
                        "series: [" +
                        "{" +
                        "name: 'Countries'," +
                        "points: [";

        String top_stats="";
        for(int i=0;i<10;i++){
            LocationStats region=regionStats.get(i);
            top_stats+=
                "{ name: '"+region.getRegion().getName()+"', y: "+region.getTotalConfirmed()+" },";
        }
        top_stats=top_stats.substring(0,top_stats.length()-1);
        js_script+=top_stats;
        js_script+=
                "]" +
                        "}" +
                        "]" +
                        "}); " +
                        "function setMode(val) {" +
                        " chart.options({ defaultPoint: { label: { placement: val } } }); " +
                        "      }" +
                        " " +
                        "function setAutoHide(val) { " +
                        "chart.options({ defaultPoint: { label: { autoHide: val } } }); " +
                        "}";
        model.addAttribute("js_script",js_script);

        String js_script3=
                "var chart3 = JSC.chart('chartDiv3', {" +
                        "debug: true," +
                        "defaultSeries_type: 'column'," +
                        "title_label_text:'Top 10 Countries by Infections per 100.000'," +
                        "yAxis:{" +
                        "defaultTick_enabled:false," +
                        "scale_range_padding:.15" +
                        "}," +
                        "legend_visible:false," +
                        "toolbar_visible:false," +
                        "series:[" +
                        "{" +
                        "name:'Infections per 100.000 population'," +
                        "color:'turquoise'," +
                        "defaultPoint:{" +
                        "label:{" +
                        "text:'%value'" +
                        "}}," +
                        "points:[";
        Collections.sort(regionStats, new Comparator<LocationStats>() {
            @Override
            public int compare(LocationStats o1, LocationStats o2) {
                return (int) (o2.getConfirmed_pro_100k() - o1.getConfirmed_pro_100k());
            }
        });
        top_stats="";
        for(int i=0;i<10;i++){
            LocationStats region=regionStats.get(i);
            top_stats+=
                    "{ name: '"+region.getRegion().getName()+"', y: "+region.getConfirmed_pro_100k()+" },";
        }
        top_stats=top_stats.substring(0,top_stats.length()-1);
        js_script3+=top_stats;
        js_script3+=
                "]}" +
                        "]" +
                        "});";
        model.addAttribute("js_script3",js_script3);
        try {
        String js_script5=
                "var chart5 = JSC.chart('chartDiv5', {" +
                        "debug: true," +
                        "defaultSeries_type: 'column'," +
                        "title_label_text:'Death toll for the last 14 days'," +
                        "yAxis:{" +
                        "defaultTick_enabled:false," +
                        "scale_range_padding:.15" +
                        "}," +
                        "legend_visible:false," +
                        "toolbar_visible:false," +
                        "series:[" +
                        "{" +
                        "name:'Number of deaths'," +
                        "color:'black'," +
                        "defaultPoint:{" +
                        "label:{" +
                        "text:'%value'" +
                        "}}," +
                        "points:[";

        LineChartResult res2=covidDataService.getData_lin_chart_data(null,6); //covidDataService.getDeaths_14days();
        Map<String,Integer> map_deaths=res2.getMap_cases();
            String daily_plots2="";
            // setting up iterator.
            SortedSet<String> keys = new TreeSet<>(map_deaths.keySet());
            for (String key : keys) {
                daily_plots2+=
                        //"['1/2/2020', 71.5]," +
                "{ name: '"+key+"', y: "+map_deaths.get(key)+" },";

                // do something
            }

            daily_plots2=daily_plots2.substring(0,daily_plots2.length()-1);
            js_script5+=daily_plots2;
            js_script5+=
                    "]}" +
                            "]" +
                            "});";
        model.addAttribute("js_script5",js_script5);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    ///
        LineChartResult res = null;
        try {
            String js_script2=
                    "var chart2 = JSC.chart('chartDiv2', {" +
                            "debug: true," +
                            "type: 'line'," +
                            "title_label_text: 'New confirmed cases'," +
                            "legend_position: 'inside bottom right'," +
                            "toolbar_items: {" +
                            "'Line Type': {" +
                            "type: 'select'," +
                            "label_style_fontSize: 13," +
                            "margin: 5," +
                            "items: 'Line,Step,Spline'," +
                            "events_change: function(val) {" +
                            "chart2.series().options({ type: val });" +
                            "}" +
                            "}" +
                            "}," +
                            "xAxis: { scale_type: 'time' }," +
                            "series: [" +
                            "{" +
                            "name: 'New Cases'," +
                            "points: [";
            res=covidDataService.getData_lin_chart_data(null,3);//covidDataService.getStatsTotal_14days();
            Map<String, Integer> map=res.getMap_cases();
            String daily_plots="";
            // setting up iterator.
            SortedSet<String> keys = new TreeSet<>(map.keySet());
            for (String key : keys) {
                daily_plots+=
                        //"['1/2/2020', 71.5]," +
                        "['"+key+"', "+map.get(key)+"],";
                // do something
            }

            daily_plots=daily_plots.substring(0,daily_plots.length()-1);
            js_script2+=daily_plots;
            js_script2+=
                    "]" +
                            "}" +
                            "]" +
                            "});";
            model.addAttribute("js_script2",js_script2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
                        /*"['1/1/2020', 29.9]," +
                        "['1/2/2020', 71.5]," +
                        "['1/3/2020', 106.4]," +
                        "['1/6/2020', 129.2]," +
                        "['1/7/2020', 144.0]," +
                        "['1/8/2020', 176.0]";*/


            String js_script4=
                    "var chart4 = JSC.chart('chartDiv4', {" +
                            "debug: true," +
                            "type: 'line'," +
                            "title_label_text: 'New Cases to Recovered Cases'," +
                            "legend_visible: false," +
                            "xAxis_scale_type: 'time'," +
                            "yAxis_markers: [{ value: 1, color: 'red', label_text: 'The risk line' }]," +
                            "series: [" +
                            "{" +
                            "name: 'Value'," +
                            "palette: {" +
                            "pointValue: '%yValue'," +
                            "stops: [" +
                            "[0, '%color', 0.7]," +
                            "[1, 'red']" +
                            "]" +
                            "}," +
                            "points: [";
            Map<String, Double> map=res.getMap_ratio();;

            String daily_plots="";
            // setting up iterator.
            SortedSet<String> keys = new TreeSet<>(map.keySet());
            for (String key : keys) {
                daily_plots+=
                        //"['1/2/2020', 71.5]," +
                        "{ x: '"+key+"', y: "+map.get(key)+" },";
                // do something
            }

            daily_plots=daily_plots.substring(0,daily_plots.length()-1);
            js_script4+=daily_plots;
            js_script4+=
                    "]" +
                            "}" +
                            "]" +
                            "});";
            model.addAttribute("js_script4",js_script4);




       // conf.disconnect();
        return "home";
    }
}

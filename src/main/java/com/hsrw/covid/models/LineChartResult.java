package com.hsrw.covid.models;

import java.util.Map;

public class LineChartResult {
    private Map<String,Integer> map_cases;
    private Map<String,Double> map_ratio;

    public Map<String, Integer> getMap_cases() {
        return map_cases;
    }

    public void setMap_cases(Map<String, Integer> map_cases) {
        this.map_cases = map_cases;
    }

    public Map<String, Double> getMap_ratio() {
        return map_ratio;
    }

    public void setMap_ratio(Map<String, Double> map_ratio) {
        this.map_ratio = map_ratio;
    }
}

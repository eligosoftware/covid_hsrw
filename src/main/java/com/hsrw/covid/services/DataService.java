package com.hsrw.covid.services;

import com.hsrw.covid.models.City;
import com.hsrw.covid.models.LocationStats;
import com.hsrw.covid.models.Region;
import com.hsrw.covid.models.Totals;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.json.*;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataService {

    //public static String DATA_URL="https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    //public static String DATA_URL="https://covid-19-statistics.p.rapidapi.com/reports";
    private static String DATA_URL_TOTALS ="https://covid-19-statistics.p.rapidapi.com/reports/total";
    private static String DATA_URL_STATS ="https://covid-19-statistics.p.rapidapi.com/reports";

    private List<LocationStats> regionStats =new ArrayList();
    private List<LocationStats> cityStats =new ArrayList();

    private int max_stat=0;

    private Totals mTotals;
    private HttpClient client;

    public Totals getmTotals() {
        return mTotals;
    }

    public List<LocationStats> getCityStats() {
        return cityStats;
    }

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat simpleDateFormatHours = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private HashMap<String, Integer> mapping;
    public int getMax_stat() {
        return max_stat;
    }

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchData() throws IOException, InterruptedException {
        //List<LocationStats> newStats = new ArrayList();

       // config conf = config.newInstance();

        //conf.connect();
        client = HttpClient.newHttpClient();

        try {
            getTotals();
            getStats();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // csv part --
        //        StringReader csvBodyReader = new StringReader(response.body().toString());
//
//        //Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
//        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(csvBodyReader);
//        boolean headerRow = true;
//        ArrayList<Date> dates=new ArrayList();
//        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yy");
//        int start_index=-1;
//        for (CSVRecord record : records) {
//
//            if (headerRow) {
//                if (start_index==-1)
//                    try{
//                        //Date startDate=subtractDays(sdf.parse(record.get(4)),1);
//                        Date startDate=sdf.parse(record.get(4));
//
//                        start_index=4+db_ops.getStartIndex(startDate);
//                    }catch (ParseException pe){
//                        System.out.println(pe.getMessage());
//                    }
//                for(int i=start_index;i<record.size();i++){
//                    try{
//                    dates.add(sdf.parse(record.get(i)));
//                    }catch (Exception pe){
//                        System.out.println("error on column "+(i+4)+" "+record.get(4+i));
//                    }
//                }
//                headerRow = false;
//                continue;
//            } else {
//                int value;
//                for(int i=0;i<dates.size();i++){
//                   if(start_index+i==0){
//                       value=Integer.parseInt(record.get(4+0));
//                   }
//                   else{
//                       value=Integer.parseInt(record.get(start_index+i))-Integer.parseInt(record.get(start_index+i-1));
//                   }
//                  db_ops.registerRow(record.get(mapping.get("Country/Region")),record.get(mapping.get("Province/State")),Double.parseDouble(record.get(mapping.get("Lat")).equals("")?"0":record.get(mapping.get("Lat"))),Double.parseDouble(record.get(mapping.get("Long")).equals("")?"0":record.get(mapping.get("Long"))),dates.get(i),value);
//                }
        //-- csv part
                /*if (Integer.parseInt(record.get(record.size() - 1)) > max_stat) {
                    max_stat = Integer.parseInt(record.get(record.size() - 1));
                }
                String state = record.get(mapping.get("Province/State"));
                String country = record.get(mapping.get("Country/Region"));
                LocationStats stat = new LocationStats(state, country);
                stat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
                stat.setDiffFromPreviousDay(Integer.parseInt(record.get(record.size() - 1)) - Integer.parseInt(record.get(record.size() - 2)));
                try {
                    stat.setLat(Double.parseDouble(record.get(mapping.get("Lat"))));
                    stat.setLong(Double.parseDouble(record.get(mapping.get("Long"))));
                } catch (Exception e) {
                    System.out.println(record.get(mapping.get("Lat")));
                    System.out.println(record.get(mapping.get("Long")));

                }
                newStats.add(stat);*/
                //System.out.println(stat);
            //}


        //}




        //this.allStats = db_ops.returnStats();
        //max_stat=db_ops.getMax_stat();
        //conf.disconnect();



    }
    private void getStats() throws IOException, InterruptedException, ParseException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATA_URL_STATS))
                .header("accept","application/json")
                .header("x-rapidapi-key", "7b0e4f3eefmsh9a199e54efe9595p1f6669jsn29c7d74d3974")
                .header("x-rapidapi-host", "covid-19-statistics.p.rapidapi.com")
                .header("useQueryString", String.valueOf(true))
                .build();
        /*mapping = new HashMap<String, Integer>();
        mapping.put("Province/State",0);
        mapping.put("Country/Region",1);
        mapping.put("Lat",2);
        mapping.put("Long",3);*/

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()==200){
            String body=String.valueOf(response.body()).replace("\n","");
            JSONObject obj = new JSONObject(body);
            JSONArray data=obj.getJSONArray("data");
            for(int i = 0 ; i < data.length() ; i++){
                JSONObject row=data.getJSONObject(i);//getString("interestKey");
                JSONObject regionJson=row.getJSONObject("region");
                //System.out.println(regionJson.getString("lat"));
                Region region;
                try{
                region=new Region(regionJson.getString("name"),
                        regionJson.getString("iso"),
                        regionJson.getString("province"),
                        Double.parseDouble(regionJson.getString("lat")),
                        Double.parseDouble(regionJson.getString("long")));
                }
                catch (Exception e){
                    region=new Region(regionJson.getString("name"),
                            regionJson.getString("iso"),
                            regionJson.getString("province"),
                            0,
                            0);
                }
                if(row.getInt("confirmed")>max_stat){
                    max_stat=row.getInt("confirmed");
                }
                regionStats.add(new LocationStats(region,
                        null,
                        simpleDateFormat.parse(row.getString("date")),
                        simpleDateFormatHours.parse(row.getString("last_update")),
                        row.getInt("deaths"),
                        row.getInt("confirmed"),
                        row.getInt("recovered"),
                        row.getInt("active"),
                        row.getInt("active_diff"),
                        row.getInt("deaths_diff"),
                        row.getInt("confirmed_diff"),
                        row.getInt("recovered_diff"),
                        row.getDouble("fatality_rate")
                        ));
                JSONArray cities=regionJson.getJSONArray("cities");
                for(int j=0;j<cities.length();j++){
                    JSONObject row_city=cities.getJSONObject(j);
                    City city;
                    try {
                        city = new City(row_city.getString("name"),
                                Double.valueOf(row_city.getString("lat")),
                                Double.valueOf(row_city.getString("long")));
                    } catch (Exception e){
                        city = new City(row_city.getString("name"),
                               0,
                                0);
                    }
                    cityStats.add(new LocationStats(region,
                            city,
                            simpleDateFormat.parse(row_city.getString("date")),
                            simpleDateFormatHours.parse(row_city.getString("last_update")),
                            row_city.getInt("deaths"),
                            row_city.getInt("confirmed"),
                            0,//row_city.getInt("recovered"),
                            0,//row_city.getInt("active"),
                            0,//row_city.getInt("active_diff"),
                            row_city.getInt("deaths_diff"),
                            row_city.getInt("confirmed_diff"),
                            0,//row_city.getInt("recovered_diff"),
                            0//row_city.getDouble("fatality_rate")
                    ));
                }
                //allStats.add(stat);
                //System.out.println("a");
            }

        }
    }
    private void getTotals() throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATA_URL_TOTALS))
                .header("accept","application/json")
                .header("x-rapidapi-key", "7b0e4f3eefmsh9a199e54efe9595p1f6669jsn29c7d74d3974")
                .header("x-rapidapi-host", "covid-19-statistics.p.rapidapi.com")
                .header("useQueryString", String.valueOf(true))
                .build();
        /*mapping = new HashMap<String, Integer>();
        mapping.put("Province/State",0);
        mapping.put("Country/Region",1);
        mapping.put("Lat",2);
        mapping.put("Long",3);*/

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()==200){

            String body=String.valueOf(response.body()).replace("\n","");
            //body=body.replaceAll(" ","");
            JSONObject obj = new JSONObject(body);
            JSONObject data=obj.getJSONObject("data");
            String date=data.getString("date");
            String updateDate=data.getString("last_update");
            mTotals=new Totals(simpleDateFormat.parse(date),data.getInt("confirmed_diff"),
                    data.getInt("deaths_diff"),
                    data.getInt("recovered_diff"),
                    data.getDouble("fatality_rate"),
                    simpleDateFormatHours.parse(updateDate),
                    data.getInt("confirmed"),
                    data.getInt("deaths"),
                    data.getInt("active"),
                    data.getInt("recovered")
                    );
          //  System.out.println("a");
        }
    }


    private static Date subtractDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);

        return cal.getTime();
    }
    public List<LocationStats> getRegionStats() {
        return regionStats;
    }
}

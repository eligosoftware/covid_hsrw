package com.hsrw.covid.services;

import com.hsrw.covid.models.*;
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
    private List<LocationStats> regionStatsCountry =new ArrayList();
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
    //"['1/3/2020', 106.4],"
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM/dd/yyyy");

    private SimpleDateFormat simpleDateFormatHours = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private HashMap<String, Integer> mapping;
    private HashMap<String, Latlng> coords;
    public int getMax_stat() {
        return max_stat;
    }

    @PostConstruct
    //@Scheduled(cron = "* * 1 * * *")
    public void fetchData() throws IOException, InterruptedException {
        //List<LocationStats> newStats = new ArrayList();

        // config conf = config.newInstance();

        //conf.connect();

        coords=new HashMap();
        coords.put("Afghanistan",new Latlng(33.93911,67.709953));
        coords.put("Albania",new Latlng(41.153332,20.168331));
        coords.put("Algeria",new Latlng(28.033886,1.659626));
        coords.put("American Samoa",new Latlng(-14.270972,-170.132217));
        coords.put("Andorra",new Latlng(42.546245,1.601554));
        coords.put("Angola",new Latlng(-11.202692,17.873887));
        coords.put("Anguilla",new Latlng(18.220554,-63.068615));
        coords.put("Antarctica",new Latlng(-75.250973,-0.071389));
        coords.put("Antigua and Barbuda",new Latlng(17.060816,-61.796428));
        coords.put("Argentina",new Latlng(-38.416097,-63.616672));
        coords.put("Armenia",new Latlng(40.069099,45.038189));
        coords.put("Aruba",new Latlng(12.52111,-69.968338));
        coords.put("Australia",new Latlng(-25.274398,133.775136));
        coords.put("Austria",new Latlng(47.516231,14.550072));
        coords.put("Azerbaijan",new Latlng(40.143105,47.576927));
        coords.put("Bahamas",new Latlng(25.03428,-77.39628));
        coords.put("Bahrain",new Latlng(25.930414,50.637772));
        coords.put("Bangladesh",new Latlng(23.684994,90.356331));
        coords.put("Barbados",new Latlng(13.193887,-59.543198));
        coords.put("Saint Martin",new Latlng(18.075277,-63.060001));
        coords.put("Belarus",new Latlng(53.709807,27.953389));
        coords.put("Belgium",new Latlng(50.503887,4.469936));
        coords.put("Belize",new Latlng(17.189877,-88.49765));
        coords.put("Benin",new Latlng(9.30769,2.315834));
        coords.put("Bermuda",new Latlng(32.321384,-64.75737));
        coords.put("Bhutan",new Latlng(27.514162,90.433601));
        coords.put("Bolivia",new Latlng(-16.290154,-63.588653));
        coords.put("Bosnia and Herzegovina",new Latlng(43.915886,17.679076));
        coords.put("Botswana",new Latlng(-22.328474,24.684866));
        coords.put("Bouvet Island",new Latlng(-54.423199,3.413194));
        coords.put("Brazil",new Latlng(-14.235004,-51.92528));
        coords.put("British Indian Ocean Territory",new Latlng(-6.343194,71.876519));
        coords.put("British Virgin Islands",new Latlng(18.420695,-64.639968));
        coords.put("Brunei",new Latlng(4.535277,114.727669));
        coords.put("Bulgaria",new Latlng(42.733883,25.48583));
        coords.put("Burkina Faso",new Latlng(12.238333,-1.561593));
        coords.put("Burma",new Latlng(16.871311,96.199379));
        coords.put("Burundi",new Latlng(-3.373056,29.918886));
        coords.put("Cabo Verde",new Latlng(16.002082,-24.013197));
        coords.put("Cambodia",new Latlng(12.565679,104.990963));
        coords.put("Cameroon",new Latlng(7.369722,12.354722));
        coords.put("Canada",new Latlng(56.130366,-106.346771));
        coords.put("Cape Verde",new Latlng(16.002082,-24.013197));
        coords.put("Cayman Islands",new Latlng(19.513469,-80.566956));
        coords.put("Central African Republic",new Latlng(6.611111,20.939444));
        coords.put("Chad",new Latlng(15.454166,18.732207));
        coords.put("Chile",new Latlng(-35.675147,-71.542969));
        coords.put("China",new Latlng(35.86166,104.195397));
        coords.put("Christmas Island",new Latlng(-10.447525,105.690449));
        coords.put("Cocos [Keeling] Islands",new Latlng(-12.164165,96.870956));
        coords.put("Colombia",new Latlng(4.570868,-74.297333));
        coords.put("Comoros",new Latlng(-11.875001,43.872219));
        coords.put("Congo (Brazzaville)",new Latlng(-0.228021,15.827659));
        coords.put("Congo (Kinshasa)",new Latlng(-4.038333,21.758664));
        coords.put("Cook Islands",new Latlng(-21.236736,-159.777671));
        coords.put("Costa Rica",new Latlng(9.748917,-83.753428));
        coords.put("Cote d'Ivoire",new Latlng(7.539989,-5.54708));
        coords.put("Croatia",new Latlng(45.1,15.2));
        coords.put("Curacao",new Latlng(12.169570,-68.990021));
        coords.put("Channel Islands",new Latlng(	49.372284,-2.364351));
        coords.put("Cuba",new Latlng(21.521757,-77.781167));
        coords.put("Cyprus",new Latlng(35.126413,33.429859));
        coords.put("Czechia",new Latlng(49.817492,15.472962));
        coords.put("Denmark",new Latlng(56.26392,9.501785));
        coords.put("Diamond Princess",new Latlng(0,0));
        coords.put("Djibouti",new Latlng(11.825138,42.590275));
        coords.put("Dominica",new Latlng(15.414999,-61.370976));
        coords.put("Dominican Republic",new Latlng(18.735693,-70.162651));
        coords.put("Ecuador",new Latlng(-1.831239,-78.183406));
        coords.put("Egypt",new Latlng(26.820553,30.802498));
        coords.put("El Salvador",new Latlng(13.794185,-88.89653));
        coords.put("Equatorial Guinea",new Latlng(1.650801,10.267895));
        coords.put("Eritrea",new Latlng(15.179384,39.782334));
        coords.put("Estonia",new Latlng(58.595272,25.013607));
        coords.put("Eswatini",new Latlng(-26.522503,31.465866));
        coords.put("Ethiopia",new Latlng(9.145,40.489673));
        coords.put("Falkland Islands [Islas Malvinas]",new Latlng(-51.796253,-59.523613));
        coords.put("Faroe Islands",new Latlng(61.892635,-6.911806));
        coords.put("Fiji",new Latlng(-16.578193,179.414413));
        coords.put("Finland",new Latlng(61.92411,25.748151));
        coords.put("France",new Latlng(46.227638,2.213749));
        coords.put("French Guiana",new Latlng(3.933889,-53.125782));
        coords.put("French Polynesia",new Latlng(-17.679742,-149.406843));
        coords.put("French Southern Territories",new Latlng(-49.280366,69.348557));
        coords.put("Gabon",new Latlng(-0.803689,11.609444));
        coords.put("Gambia",new Latlng(13.443182,-15.310139));
        coords.put("Gaza Strip",new Latlng(31.354676,34.308825));
        coords.put("Georgia",new Latlng(42.315407,43.356892));
        coords.put("Germany",new Latlng(51.165691,10.451526));
        coords.put("Ghana",new Latlng(7.946527,-1.023194));
        coords.put("Gibraltar",new Latlng(36.137741,-5.345374));
        coords.put("Greece",new Latlng(39.074208,21.824312));
        coords.put("Greenland",new Latlng(71.706936,-42.604303));
        coords.put("Grenada",new Latlng(12.262776,-61.604171));
        coords.put("Guadeloupe",new Latlng(16.995971,-62.067641));
        coords.put("Guam",new Latlng(13.444304,144.793731));
        coords.put("Guatemala",new Latlng(15.783471,-90.230759));
        coords.put("Guernsey",new Latlng(49.465691,-2.585278));
        coords.put("Guinea",new Latlng(9.945587,-9.696645));
        coords.put("Guinea-Bissau",new Latlng(11.803749,-15.180413));
        coords.put("Guyana",new Latlng(4.860416,-58.93018));
        coords.put("Haiti",new Latlng(18.971187,-72.285215));
        coords.put("Heard Island and McDonald Islands",new Latlng(-53.08181,73.504158));
        coords.put("Holy See ",new Latlng(41.9039,12.4521));
        coords.put("Honduras",new Latlng(15.199999,-86.241905));
        coords.put("Hong Kong",new Latlng(22.396428,114.109497));
        coords.put("Hungary",new Latlng(47.162494,19.503304));
        coords.put("Iceland",new Latlng(64.963051,-19.020835));
        coords.put("India",new Latlng(20.593684,78.96288));
        coords.put("Indonesia",new Latlng(-0.789275,113.921327));
        coords.put("Iran",new Latlng(32.427908,53.688046));
        coords.put("Iraq",new Latlng(33.223191,43.679291));
        coords.put("Ireland",new Latlng(53.41291,-8.24389));
        coords.put("Isle of Man",new Latlng(54.236107,-4.548056));
        coords.put("Israel",new Latlng(31.046051,34.851612));
        coords.put("Italy",new Latlng(41.87194,12.56738));
        coords.put("Jamaica",new Latlng(18.109581,-77.297508));
        coords.put("Japan",new Latlng(36.204824,138.252924));
        coords.put("Jersey",new Latlng(49.214439,-2.13125));
        coords.put("Jordan",new Latlng(30.585164,36.238414));
        coords.put("Kazakhstan",new Latlng(48.019573,66.923684));
        coords.put("Kenya",new Latlng(-0.023559,37.906193));
        coords.put("Kiribati",new Latlng(-3.370417,-168.734039));
        coords.put("Korea, South",new Latlng(35.907757,127.766922));
        coords.put("Kosovo",new Latlng(42.602636,20.902977));
        coords.put("Kuwait",new Latlng(29.31166,47.481766));
        coords.put("Kyrgyzstan",new Latlng(41.20438,74.766098));
        coords.put("Laos",new Latlng(19.85627,102.495496));
        coords.put("Latvia",new Latlng(56.879635,24.603189));
        coords.put("Lebanon",new Latlng(33.854721,35.862285));
        coords.put("Lesotho",new Latlng(-29.609988,28.233608));
        coords.put("Liberia",new Latlng(6.428055,-9.429499));
        coords.put("Libya",new Latlng(26.3351,17.228331));
        coords.put("Liechtenstein",new Latlng(47.166,9.555373));
        coords.put("Lithuania",new Latlng(55.169438,23.881275));
        coords.put("Luxembourg",new Latlng(49.815273,6.129583));
        coords.put("Macau",new Latlng(22.198745,113.543873));
        coords.put("Madagascar",new Latlng(-18.766947,46.869107));
        coords.put("Malawi",new Latlng(-13.254308,34.301525));
        coords.put("Malaysia",new Latlng(4.210484,101.975766));
        coords.put("Maldives",new Latlng(3.202778,73.22068));
        coords.put("Mali",new Latlng(17.570692,-3.996166));
        coords.put("Macao SAR",new Latlng(22.210928,113.552971));
        coords.put("Malta",new Latlng(35.937496,14.375416));
        coords.put("Marshall Islands",new Latlng(7.131474,171.184478));
        coords.put("Martinique",new Latlng(14.641528,-61.024174));
        coords.put("Mauritania",new Latlng(21.00789,-10.940835));
        coords.put("Mauritius",new Latlng(-20.348404,57.552152));
        coords.put("Mayotte",new Latlng(-12.8275,45.166244));
        coords.put("Mexico",new Latlng(23.634501,-102.552784));
        coords.put("Micronesia",new Latlng(7.425554,150.550812));
        coords.put("Moldova",new Latlng(47.411631,28.369885));
        coords.put("Monaco",new Latlng(43.750298,7.412841));
        coords.put("Mongolia",new Latlng(46.862496,103.846656));
        coords.put("Montenegro",new Latlng(42.708678,19.37439));
        coords.put("Montserrat",new Latlng(16.742498,-62.187366));
        coords.put("Morocco",new Latlng(31.791702,-7.09262));
        coords.put("Mozambique",new Latlng(-18.665695,35.529562));
        coords.put("MS Zaandam",new Latlng(0,0));
        coords.put("Namibia",new Latlng(-22.95764,18.49041));
        coords.put("Nauru",new Latlng(-0.522778,166.931503));
        coords.put("Nepal",new Latlng(28.394857,84.124008));
        coords.put("Netherlands",new Latlng(52.132633,5.291266));
        coords.put("Netherlands Antilles",new Latlng(12.226079,-69.060087));
        coords.put("New Caledonia",new Latlng(-20.904305,165.618042));
        coords.put("New Zealand",new Latlng(-40.900557,174.885971));
        coords.put("Nicaragua",new Latlng(12.865416,-85.207229));
        coords.put("Niger",new Latlng(17.607789,8.081666));
        coords.put("Nigeria",new Latlng(9.081999,8.675277));
        coords.put("Niue",new Latlng(-19.054445,-169.867233));
        coords.put("Norfolk Island",new Latlng(-29.040835,167.954712));
        coords.put("North Korea",new Latlng(40.339852,127.510093));
        coords.put("North Macedonia",new Latlng(41.608635,21.745275));
        coords.put("Northern Mariana Islands",new Latlng(17.33083,145.38469));
        coords.put("Norway",new Latlng(60.472024,8.468946));
        coords.put("Oman",new Latlng(21.512583,55.923255));
        coords.put("Pakistan",new Latlng(30.375321,69.345116));
        coords.put("Palau",new Latlng(7.51498,134.58252));
        coords.put("Palestinian Territories",new Latlng(31.952162,35.233154));
        coords.put("Panama",new Latlng(8.537981,-80.782127));
        coords.put("Papua New Guinea",new Latlng(-6.314993,143.95555));
        coords.put("Paraguay",new Latlng(-23.442503,-58.443832));
        coords.put("Peru",new Latlng(-9.189967,-75.015152));
        coords.put("Philippines",new Latlng(12.879721,121.774017));
        coords.put("Pitcairn Islands",new Latlng(-24.703615,-127.439308));
        coords.put("Poland",new Latlng(51.919438,19.145136));
        coords.put("Portugal",new Latlng(39.399872,-8.224454));
        coords.put("Puerto Rico",new Latlng(18.220833,-66.590149));
        coords.put("Qatar",new Latlng(25.354826,51.183884));
        coords.put("Reunion",new Latlng(-21.115141,55.536384));
        coords.put("Romania",new Latlng(45.943161,24.96676));
        coords.put("Russia",new Latlng(61.52401,105.318756));
        coords.put("Rwanda",new Latlng(-1.940278,29.873888));
        coords.put("Sao Tome and Principe ",new Latlng(0.18636,6.613081));
        coords.put("Saint Helena",new Latlng(-24.143474,-10.030696));
        coords.put("Saint Kitts and Nevis",new Latlng(17.357822,-62.782998));
        coords.put("Saint Lucia",new Latlng(13.909444,-60.978893));
        coords.put("Saint Barthelemy",new Latlng(17.9139,-62.8339));
        coords.put("Taipei and environs",new Latlng(25.105497,121.597366));
        coords.put("Saint Pierre and Miquelon",new Latlng(46.941936,-56.27111));
        coords.put("Saint Vincent and the Grenadines",new Latlng(12.984305,-61.287228));
        coords.put("Samoa",new Latlng(-13.759029,-172.104629));
        coords.put("San Marino",new Latlng(43.94236,12.457777));
        coords.put("Saudi Arabia",new Latlng(23.885942,45.079162));
        coords.put("Senegal",new Latlng(14.497401,-14.452362));
        coords.put("Serbia",new Latlng(44.016521,21.005859));
        coords.put("Seychelles",new Latlng(-4.679574,55.491977));
        coords.put("Sierra Leone",new Latlng(8.460555,-11.779889));
        coords.put("Singapore",new Latlng(1.352083,103.819836));
        coords.put("Slovakia",new Latlng(48.669026,19.699024));
        coords.put("Slovenia",new Latlng(46.151241,14.995463));
        coords.put("Solomon Islands",new Latlng(-9.64571,160.156194));
        coords.put("Somalia",new Latlng(5.152149,46.199616));
        coords.put("South Africa",new Latlng(-30.559482,22.937506));
        coords.put("South Georgia and the South Sandwich Islands",new Latlng(-54.429579,-36.587909));
        coords.put("Spain",new Latlng(40.463667,-3.74922));
        coords.put("Sri Lanka",new Latlng(7.873054,80.771797));
        coords.put("South Sudan",new Latlng(6.877,31.307));
        coords.put("Sudan",new Latlng(12.862807,30.217636));
        coords.put("Suriname",new Latlng(3.919305,-56.027783));
        coords.put("Svalbard and Jan Mayen",new Latlng(77.553604,23.670272));
        coords.put("Sweden",new Latlng(60.128161,18.643501));
        coords.put("Switzerland",new Latlng(46.818188,8.227512));
        coords.put("Syria",new Latlng(34.802075,38.996815));
        coords.put("Taiwan",new Latlng(23.69781,120.960515));
        coords.put("Tajikistan",new Latlng(38.861034,71.276093));
        coords.put("Tanzania",new Latlng(-6.369028,34.888822));
        coords.put("Thailand",new Latlng(15.870032,100.992541));
        coords.put("Timor-Leste",new Latlng(-8.874217,125.727539));
        coords.put("Togo",new Latlng(8.619543,0.824782));
        coords.put("Tokelau",new Latlng(-8.967363,-171.855881));
        coords.put("Tonga",new Latlng(-21.178986,-175.198242));
        coords.put("Trinidad and Tobago",new Latlng(10.691803,-61.222503));
        coords.put("Tunisia",new Latlng(33.886917,9.537499));
        coords.put("Turkey",new Latlng(38.963745,35.243322));
        coords.put("Turkmenistan",new Latlng(38.969719,59.556278));
        coords.put("Turks and Caicos Islands",new Latlng(21.694025,-71.797928));
        coords.put("Tuvalu",new Latlng(-7.109535,177.64933));
        coords.put("U.S. Minor Outlying Islands",new Latlng(0,0));
        coords.put("U.S. Virgin Islands",new Latlng(18.335765,-64.896335));
        coords.put("Uganda",new Latlng(1.373333,32.290275));
        coords.put("Ukraine",new Latlng(48.379433,31.16558));
        coords.put("United Arab Emirates",new Latlng(23.424076,53.847818));
        coords.put("United Kingdom",new Latlng(55.378051,-3.435973));
        coords.put("US",new Latlng(37.09024,-95.712891));
        coords.put("Uruguay",new Latlng(-32.522779,-55.765835));
        coords.put("Uzbekistan",new Latlng(41.377491,64.585262));
        coords.put("Vanuatu",new Latlng(-15.376706,166.959158));
        coords.put("Vatican City",new Latlng(41.902916,12.453389));
        coords.put("Venezuela",new Latlng(6.42375,-66.58973));
        coords.put("Vietnam",new Latlng(14.058324,108.277199));
        coords.put("Wallis and Futuna",new Latlng(-13.768752,-177.156097));
        coords.put("Western Sahara",new Latlng(24.215527,-12.885834));
        coords.put("West Bank and Gaza",new Latlng(31.9522,35.2332));
        coords.put("Yemen",new Latlng(15.552727,48.516388));
        coords.put("Zambia",new Latlng(-13.133897,27.849332));
        coords.put("Zimbabwe",new Latlng(-19.015438,29.154857));

        client = HttpClient.newHttpClient();

        try {
            getTotals();
            //getStats();
            getStatsCountry();
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
    public List<LocationStats> getStatsCountry_by_country(String country) throws IOException, InterruptedException, ParseException{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(DATA_URL_STATS+"?region_name="+country))
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

        List<LocationStats> regionStats =new ArrayList();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()==200){
            String body=String.valueOf(response.body()).replace("\n","");
            JSONObject obj = new JSONObject(body);
            JSONArray data=obj.getJSONArray("data");
            Region lastCountry=null;
            String lastCountryDate="";
            String lastCountryUpdated="";
            int regions_count=1;
            int t_deaths=0;
            int t_confirmed=0;
            int t_recovered=0;
            int t_active=0;
            int t_active_diff=0;
            int t_deaths_diff=0;
            int t_confirmed_diff=0;
            int t_recovered_diff=0;
            double t_fatality_rate=0;
            for(int i = 0 ; i < data.length() ; i++){
                JSONObject row=data.getJSONObject(i);//getString("interestKey");
                JSONObject regionJson=row.getJSONObject("region");
                //System.out.println(regionJson.getString("lat"));
                if(lastCountry!=null && regionJson.getString("name").equals(lastCountry.getName())){
                    regions_count++;
                    t_deaths+=row.getInt("deaths");
                    t_confirmed+=row.getInt("confirmed");
                    t_recovered+=row.getInt("recovered");
                    t_active+=row.getInt("active");
                    t_active_diff+=row.getInt("active_diff");
                    t_deaths_diff+=row.getInt("deaths_diff");
                    t_confirmed_diff+=row.getInt("confirmed_diff");
                    t_recovered_diff+=row.getInt("recovered_diff");
                    t_fatality_rate+=row.getDouble("fatality_rate");
                } else {

                    if(lastCountry!=null){

                        regionStats.add(new LocationStats(lastCountry,
                                null,
                                simpleDateFormat.parse(lastCountryDate),//row.getString("date")),
                                simpleDateFormatHours.parse(lastCountryUpdated),//row.getString("last_update")),
                                t_deaths,
                                t_confirmed,
                                t_recovered,
                                t_active,
                                t_active_diff,
                                t_deaths_diff,
                                t_confirmed_diff,
                                t_recovered_diff,
                                t_fatality_rate/regions_count
                        ));

                        regions_count=1;
                        try{
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    coords.get(regionJson.getString("name")).getLat(),
                                    coords.get(regionJson.getString("name")).getLon());
                        }
                        catch (Exception e){
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    0,
                                    0);
                        }
                        lastCountryDate=row.getString("date");
                        lastCountryUpdated=row.getString("last_update");
                        t_deaths=row.getInt("deaths");
                        t_confirmed=row.getInt("confirmed");
                        t_recovered=row.getInt("recovered");
                        t_active=row.getInt("active");
                        t_active_diff=row.getInt("active_diff");
                        t_deaths_diff=row.getInt("deaths_diff");
                        t_confirmed_diff=row.getInt("confirmed_diff");
                        t_recovered_diff=row.getInt("recovered_diff");
                        t_fatality_rate=row.getDouble("fatality_rate");
                    }
                    else{
                        regions_count=1;
                        try{
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    coords.get(regionJson.getString("name")).getLat(),
                                    coords.get(regionJson.getString("name")).getLon());
                        }
                        catch (Exception e){
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    0,
                                    0);
                        }
                        lastCountryDate=row.getString("date");
                        lastCountryUpdated=row.getString("last_update");

                        t_deaths=row.getInt("deaths");
                        t_confirmed=row.getInt("confirmed");
                        t_recovered=row.getInt("recovered");
                        t_active=row.getInt("active");
                        t_active_diff=row.getInt("active_diff");
                        t_deaths_diff=row.getInt("deaths_diff");
                        t_confirmed_diff=row.getInt("confirmed_diff");
                        t_recovered_diff=row.getInt("recovered_diff");
                        t_fatality_rate=row.getDouble("fatality_rate");
                    }
                }

            }

            regionStats.add(new LocationStats(lastCountry,
                    null,
                    simpleDateFormat.parse(lastCountryDate),//row.getString("date")),
                    simpleDateFormatHours.parse(lastCountryUpdated),//row.getString("last_update")),
                    t_deaths,
                    t_confirmed,
                    t_recovered,
                    t_active,
                    t_active_diff,
                    t_deaths_diff,
                    t_confirmed_diff,
                    t_recovered_diff,
                    t_fatality_rate/regions_count
            ));

        }
        return regionStats;
    }


    public List<LocationStats> getStatsCountry_by_continent(String continent) throws IOException, InterruptedException, ParseException{

        String europe= "Albania;Andorra;Armenia;Austria;Azerbaijan;Belarus;Belgium;Bosnia and Herzegovina;Bulgaria;Croatia;Cyprus;Channel Islands;Czechia;Denmark;Estonia;Faroe Islands;Finland;France;Georgia;Germany;Gibraltar;Guernsey;Greece;Holy See;Hungary;Iceland;Ireland;Italy;Jersey;Kosovo;Latvia;Liechtenstein;Lithuania;Luxembourg;Mayotte;Malta;Moldova;MS Zaandam;Monaco;Montenegro;Netherlands;North Macedonia;Norway;Poland;Portugal;Romania;Russia;San Marino;Serbia;Slovakia;Slovenia;Spain;Sweden;Switzerland;Turkey;Ukraine;United Kingdom;";
        String asia="Afghanistan;Bahrain;Bangladesh;Bhutan;Brunei;Burma;Cambodia;China;India;Indonesia;Iran;Iraq;Israel;West Bank and Gaza;Japan;Jordan;Kazakhstan;Korea, South;Kyrgyzstan;Kuwait;Laos;Lebanon;Malaysia;Macao SAR;Maldives;Mongolia;Nepal;Oman;Pakistan;Philippines;Qatar;Saudi Arabia;Singapore;Sri Lanka;Syria;Taiwan;Tajikistan;Thailand;Timor-Leste;Turkmenistan;Taipei and environs;United Arab Emirates;Uzbekistan;Vietnam;Yemen;";
        String africa="Algeria;Angola;Benin;Botswana;Tanzania;Burkina Faso;Western Sahara;Burundi;Cabo Verde;Cameroon;Central African Republic;Comoros;Chad;Malawi;Sierra Leone;Sao Tome and Principe;Seychelles;Congo (Brazzaville);Congo (Kinshasa);Cote d'Ivoire;Djibouti;Egypt;Equatorial Guinea;Guinea;Guinea-Bissau;Eritrea;Eswatini;South Sudan;Reunion;Ethiopia;Gabon;Gambia;Ghana;Kenya;Liberia;Libya;Lesotho;Madagascar;Mali;Mauritania;Mauritius;Morocco;Mozambique;Namibia;Niger;Nigeria;Rwanda;Senegal;Somalia;South Africa;Sudan;Togo;Tunisia;Uganda;Zambia;Zimbabwe;";
        String oceania="Australia;Fiji;Guam;New Zealand;Papua New Guinea;Kiribati;Marshall Islands;Solomon Islands;Vanuatu;";
        String north_america="Antigua and Barbuda;Aruba;Bahamas;Barbados;Belize;Canada;Cayman Islands;Costa Rica;Cuba;Dominica;Dominican Republic;El Salvador;Greenland;Grenada;Guadeloupe;Guatemala;Haiti;Honduras;Jamaica;Martinique;Mexico;Nicaragua;Panama;Puerto Rico;Saint Kitts and Nevis;Saint Lucia;Saint Martin;Saint Vincent and the Grenadines;Saint Barthelemy;Trinidad and Tobago;US;";
        String south_america="Argentina;Bolivia;Brazil;Chile;Colombia;Curacao;Ecuador;French Guiana;Guyana;Paraguay;Peru;Suriname;Uruguay;Venezuela;";

        String selected_continent="";
        switch (continent){
            case "europe":
                selected_continent=europe;
                break;
            case "asia":
                selected_continent=asia;
                break;
            case "africa":
                selected_continent=africa;
                break;
            case "oceania":
                selected_continent=oceania;
                break;
            case "north_america":
                selected_continent=north_america;
                break;
            case "south_america":
                selected_continent=south_america;
                break;

        }

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

        List<LocationStats> regionStats =new ArrayList();

        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()==200){
            String body=String.valueOf(response.body()).replace("\n","");
            JSONObject obj = new JSONObject(body);
            JSONArray data=obj.getJSONArray("data");
            Region lastCountry=null;
            String lastCountryDate="";
            String lastCountryUpdated="";
            int regions_count=1;
            int t_deaths=0;
            int t_confirmed=0;
            int t_recovered=0;
            int t_active=0;
            int t_active_diff=0;
            int t_deaths_diff=0;
            int t_confirmed_diff=0;
            int t_recovered_diff=0;
            double t_fatality_rate=0;
            for(int i = 0 ; i < data.length() ; i++){

                JSONObject row=data.getJSONObject(i);//getString("interestKey");
                JSONObject regionJson=row.getJSONObject("region");

                if (!selected_continent.contains(regionJson.getString("name")+";"))
                    continue;
                //System.out.println(regionJson.getString("lat"));
                if(lastCountry!=null && regionJson.getString("name").equals(lastCountry.getName())){
                    regions_count++;
                    t_deaths+=row.getInt("deaths");
                    t_confirmed+=row.getInt("confirmed");
                    t_recovered+=row.getInt("recovered");
                    t_active+=row.getInt("active");
                    t_active_diff+=row.getInt("active_diff");
                    t_deaths_diff+=row.getInt("deaths_diff");
                    t_confirmed_diff+=row.getInt("confirmed_diff");
                    t_recovered_diff+=row.getInt("recovered_diff");
                    t_fatality_rate+=row.getDouble("fatality_rate");
                } else {

                    if(lastCountry!=null){

                        regionStats.add(new LocationStats(lastCountry,
                                null,
                                simpleDateFormat.parse(lastCountryDate),//row.getString("date")),
                                simpleDateFormatHours.parse(lastCountryUpdated),//row.getString("last_update")),
                                t_deaths,
                                t_confirmed,
                                t_recovered,
                                t_active,
                                t_active_diff,
                                t_deaths_diff,
                                t_confirmed_diff,
                                t_recovered_diff,
                                t_fatality_rate/regions_count
                        ));

                        regions_count=1;
                        try{
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    coords.get(regionJson.getString("name")).getLat(),
                                    coords.get(regionJson.getString("name")).getLon());
                        }
                        catch (Exception e){
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    0,
                                    0);
                        }
                        lastCountryDate=row.getString("date");
                        lastCountryUpdated=row.getString("last_update");
                        t_deaths=row.getInt("deaths");
                        t_confirmed=row.getInt("confirmed");
                        t_recovered=row.getInt("recovered");
                        t_active=row.getInt("active");
                        t_active_diff=row.getInt("active_diff");
                        t_deaths_diff=row.getInt("deaths_diff");
                        t_confirmed_diff=row.getInt("confirmed_diff");
                        t_recovered_diff=row.getInt("recovered_diff");
                        t_fatality_rate=row.getDouble("fatality_rate");
                    }
                    else{
                        regions_count=1;
                        try{
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    coords.get(regionJson.getString("name")).getLat(),
                                    coords.get(regionJson.getString("name")).getLon());
                        }
                        catch (Exception e){
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    0,
                                    0);
                        }
                        lastCountryDate=row.getString("date");
                        lastCountryUpdated=row.getString("last_update");

                        t_deaths=row.getInt("deaths");
                        t_confirmed=row.getInt("confirmed");
                        t_recovered=row.getInt("recovered");
                        t_active=row.getInt("active");
                        t_active_diff=row.getInt("active_diff");
                        t_deaths_diff=row.getInt("deaths_diff");
                        t_confirmed_diff=row.getInt("confirmed_diff");
                        t_recovered_diff=row.getInt("recovered_diff");
                        t_fatality_rate=row.getDouble("fatality_rate");
                    }
                }

            }

            regionStats.add(new LocationStats(lastCountry,
                    null,
                    simpleDateFormat.parse(lastCountryDate),//row.getString("date")),
                    simpleDateFormatHours.parse(lastCountryUpdated),//row.getString("last_update")),
                    t_deaths,
                    t_confirmed,
                    t_recovered,
                    t_active,
                    t_active_diff,
                    t_deaths_diff,
                    t_confirmed_diff,
                    t_recovered_diff,
                    t_fatality_rate/regions_count
            ));

        }
        return regionStats;
    }

    private void getStatsCountry() throws IOException, InterruptedException, ParseException{
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
            Region lastCountry=null;
            String lastCountryDate="";
            String lastCountryUpdated="";
            int t_deaths=0;
            int t_confirmed=0;
            int t_recovered=0;
            int t_active=0;
            int t_active_diff=0;
            int t_deaths_diff=0;
            int t_confirmed_diff=0;
            int t_recovered_diff=0;
            double t_fatality_rate=0;
            for(int i = 0 ; i < data.length() ; i++){
                JSONObject row=data.getJSONObject(i);//getString("interestKey");
                JSONObject regionJson=row.getJSONObject("region");
                //System.out.println(regionJson.getString("lat"));
                if(lastCountry!=null && regionJson.getString("name").equals(lastCountry.getName())){
                    t_deaths+=row.getInt("deaths");
                    t_confirmed+=row.getInt("confirmed");
                    t_recovered+=row.getInt("recovered");
                    t_active+=row.getInt("active");
                    t_active_diff+=row.getInt("active_diff");
                    t_deaths_diff+=row.getInt("deaths_diff");
                    t_confirmed_diff+=row.getInt("confirmed_diff");
                    t_recovered_diff+=row.getInt("recovered_diff");
                    t_fatality_rate=row.getDouble("fatality_rate");
                } else {
                    if(lastCountry!=null){

                        if(t_confirmed>max_stat){
                            max_stat=t_confirmed;
                        }
                        regionStatsCountry.add(new LocationStats(lastCountry,
                                null,
                                simpleDateFormat.parse(lastCountryDate),//row.getString("date")),
                                simpleDateFormatHours.parse(lastCountryUpdated),//row.getString("last_update")),
                                t_deaths,
                                t_confirmed,
                                t_recovered,
                                t_active,
                                t_active_diff,
                                t_deaths_diff,
                                t_confirmed_diff,
                                t_recovered_diff,
                                t_fatality_rate
                        ));


                        try{
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    coords.get(regionJson.getString("name")).getLat(),
                                    coords.get(regionJson.getString("name")).getLon());
                        }
                        catch (Exception e){
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    0,
                                    0);
                        }
                        lastCountryDate=row.getString("date");
                        lastCountryUpdated=row.getString("last_update");
                        t_deaths=row.getInt("deaths");
                        t_confirmed=row.getInt("confirmed");
                        t_recovered=row.getInt("recovered");
                        t_active=row.getInt("active");
                        t_active_diff=row.getInt("active_diff");
                        t_deaths_diff=row.getInt("deaths_diff");
                        t_confirmed_diff=row.getInt("confirmed_diff");
                        t_recovered_diff=row.getInt("recovered_diff");
                        t_fatality_rate=row.getDouble("fatality_rate");
                    }
                    else{

                        try{
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    coords.get(regionJson.getString("name")).getLat(),
                                    coords.get(regionJson.getString("name")).getLon());
                        }
                        catch (Exception e){
                            lastCountry=new Region(regionJson.getString("name"),
                                    regionJson.getString("iso"),
                                    "",
                                    0,
                                    0);
                        }
                        lastCountryDate=row.getString("date");
                        lastCountryUpdated=row.getString("last_update");

                        t_deaths=row.getInt("deaths");
                        t_confirmed=row.getInt("confirmed");
                        t_recovered=row.getInt("recovered");
                        t_active=row.getInt("active");
                        t_active_diff=row.getInt("active_diff");
                        t_deaths_diff=row.getInt("deaths_diff");
                        t_confirmed_diff=row.getInt("confirmed_diff");
                        t_recovered_diff=row.getInt("recovered_diff");
                        t_fatality_rate=row.getDouble("fatality_rate");
                    }
                }

            }

            regionStatsCountry.add(new LocationStats(lastCountry,
                    null,
                    simpleDateFormat.parse(lastCountryDate),//row.getString("date")),
                    simpleDateFormatHours.parse(lastCountryUpdated),//row.getString("last_update")),
                    t_deaths,
                    t_confirmed,
                    t_recovered,
                    t_active,
                    t_active_diff,
                    t_deaths_diff,
                    t_confirmed_diff,
                    t_recovered_diff,
                    t_fatality_rate
            ));

        }
    }

    public Map<String, Integer> getStatsTotal_14days() throws IOException, InterruptedException, ParseException{
        Calendar cal = new GregorianCalendar();

        Map<String,Integer> result=new HashMap();
        Date date=null;
        for(int j=0;j<14;j++){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            date=cal.getTime();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DATA_URL_TOTALS+"?date="+simpleDateFormat.format(date)))
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
                try {
                    JSONObject data=obj.getJSONObject("data");
                    if(data.getInt("confirmed_diff")>0) {
                        result.put(simpleDateFormat2.format(date), data.getInt("confirmed_diff"));
                    }
                    else{
                        result.put(simpleDateFormat2.format(date),0);
                    }
                } catch (Exception e){
                    result.put(simpleDateFormat2.format(date),0);
                }

            }}
        return result;
    }

    public Map<String, Integer> getStatsTotal_14days_country(String country) throws IOException, InterruptedException, ParseException{
        Calendar cal = new GregorianCalendar();

        Map<String,Integer> result=new HashMap();
        Date date=null;
        for(int j=0;j<14;j++){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            date=cal.getTime();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DATA_URL_STATS+"?date="+simpleDateFormat.format(date)+"&region_name="+country))
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
                JSONArray data=obj.getJSONArray("data");




                Region lastCountry=null;
                String lastCountryDate="";
                String lastCountryUpdated="";
                int t_confirmed_diff=0;

                for(int i = 0 ; i < data.length() ; i++){
                    JSONObject row=data.getJSONObject(i);//getString("interestKey");
                    JSONObject regionJson=row.getJSONObject("region");
                    //System.out.println(regionJson.getString("lat"));
                    if(lastCountry!=null && regionJson.getString("name").equals(lastCountry.getName())){


                        t_confirmed_diff+=row.getInt("confirmed_diff");
                    } else {

                        if(lastCountry!=null){

                            result.put(simpleDateFormat2.format(date),t_confirmed_diff);

                            try{
                                lastCountry=new Region(regionJson.getString("name"),
                                        regionJson.getString("iso"),
                                        "",
                                        coords.get(regionJson.getString("name")).getLat(),
                                        coords.get(regionJson.getString("name")).getLon());
                            }
                            catch (Exception e){
                                lastCountry=new Region(regionJson.getString("name"),
                                        regionJson.getString("iso"),
                                        "",
                                        0,
                                        0);
                            }
                            lastCountryDate=row.getString("date");
                            lastCountryUpdated=row.getString("last_update");

                            t_confirmed_diff=row.getInt("confirmed_diff");
                        }
                        else{
                            try{
                                lastCountry=new Region(regionJson.getString("name"),
                                        regionJson.getString("iso"),
                                        "",
                                        coords.get(regionJson.getString("name")).getLat(),
                                        coords.get(regionJson.getString("name")).getLon());
                            }
                            catch (Exception e){
                                lastCountry=new Region(regionJson.getString("name"),
                                        regionJson.getString("iso"),
                                        "",
                                        0,
                                        0);
                            }
                            lastCountryDate=row.getString("date");
                            lastCountryUpdated=row.getString("last_update");
                            t_confirmed_diff=row.getInt("confirmed_diff");
                        }
                    }

                }

                result.put(simpleDateFormat2.format(date),t_confirmed_diff);

            }

        }
        return result;
    }
    public Map<String, Integer> getStatsTotal_14days_continents(String continent) throws IOException, InterruptedException, ParseException{
        Calendar cal = new GregorianCalendar();

        Map<String,Integer> result=new HashMap();
        Date date=null;

        String europe= "Albania;Andorra;Armenia;Austria;Azerbaijan;Belarus;Belgium;Bosnia and Herzegovina;Bulgaria;Croatia;Cyprus;Channel Islands;Czechia;Denmark;Estonia;Faroe Islands;Finland;France;Georgia;Germany;Gibraltar;Guernsey;Greece;Holy See;Hungary;Iceland;Ireland;Italy;Jersey;Kosovo;Latvia;Liechtenstein;Lithuania;Luxembourg;Mayotte;Malta;Moldova;MS Zaandam;Monaco;Montenegro;Netherlands;North Macedonia;Norway;Poland;Portugal;Romania;Russia;San Marino;Serbia;Slovakia;Slovenia;Spain;Sweden;Switzerland;Turkey;Ukraine;United Kingdom;";
        String asia="Afghanistan;Bahrain;Bangladesh;Bhutan;Brunei;Burma;Cambodia;China;India;Indonesia;Iran;Iraq;Israel;West Bank and Gaza;Japan;Jordan;Kazakhstan;Korea, South;Kyrgyzstan;Kuwait;Laos;Lebanon;Malaysia;Macao SAR;Maldives;Mongolia;Nepal;Oman;Pakistan;Philippines;Qatar;Saudi Arabia;Singapore;Sri Lanka;Syria;Taiwan;Tajikistan;Thailand;Timor-Leste;Turkmenistan;Taipei and environs;United Arab Emirates;Uzbekistan;Vietnam;Yemen;";
        String africa="Algeria;Angola;Benin;Botswana;Tanzania;Burkina Faso;Western Sahara;Burundi;Cabo Verde;Cameroon;Central African Republic;Comoros;Chad;Malawi;Sierra Leone;Sao Tome and Principe;Seychelles;Congo (Brazzaville);Congo (Kinshasa);Cote d'Ivoire;Djibouti;Egypt;Equatorial Guinea;Guinea;Guinea-Bissau;Eritrea;Eswatini;South Sudan;Reunion;Ethiopia;Gabon;Gambia;Ghana;Kenya;Liberia;Libya;Lesotho;Madagascar;Mali;Mauritania;Mauritius;Morocco;Mozambique;Namibia;Niger;Nigeria;Rwanda;Senegal;Somalia;South Africa;Sudan;Togo;Tunisia;Uganda;Zambia;Zimbabwe;";
        String oceania="Australia;Fiji;Guam;New Zealand;Papua New Guinea;Kiribati;Marshall Islands;Solomon Islands;Vanuatu;";
        String north_america="Antigua and Barbuda;Aruba;Bahamas;Barbados;Belize;Canada;Cayman Islands;Costa Rica;Cuba;Dominica;Dominican Republic;El Salvador;Greenland;Grenada;Guadeloupe;Guatemala;Haiti;Honduras;Jamaica;Martinique;Mexico;Nicaragua;Panama;Puerto Rico;Saint Kitts and Nevis;Saint Lucia;Saint Martin;Saint Vincent and the Grenadines;Saint Barthelemy;Trinidad and Tobago;US;";
        String south_america="Argentina;Bolivia;Brazil;Chile;Colombia;Curacao;Ecuador;French Guiana;Guyana;Paraguay;Peru;Suriname;Uruguay;Venezuela;";

        String selected_continent="";
        switch (continent){
            case "europe":
                selected_continent=europe;
                break;
            case "asia":
                selected_continent=asia;
                break;
            case "africa":
                selected_continent=africa;
                break;
            case "oceania":
                selected_continent=oceania;
                break;
            case "north_america":
                selected_continent=north_america;
                break;
            case "south_america":
                selected_continent=south_america;
                break;

        }
        for(int j=0;j<14;j++){
            cal.add(Calendar.DAY_OF_MONTH, -1);
            date=cal.getTime();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DATA_URL_STATS+"?date="+simpleDateFormat.format(date)))
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
                JSONArray data=obj.getJSONArray("data");
                int t_confirmed_diff=0;

                for(int i = 0 ; i < data.length() ; i++) {
                    JSONObject row = data.getJSONObject(i);//getString("interestKey");
                    JSONObject regionJson = row.getJSONObject("region");

                    if (!selected_continent.contains(regionJson.getString("name") + ";"))
                        continue;

                    t_confirmed_diff += row.getInt("confirmed_diff");

                }
                result.put(simpleDateFormat2.format(date),t_confirmed_diff);

            }

        }
        return result;
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
            mTotals=new Totals(
                    simpleDateFormat.parse(date),
                    data.getInt("confirmed_diff"),
                    data.getInt("deaths_diff"),
                    data.getInt("active_diff"),
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
    public List<LocationStats> getRegionStatsCountry() {
        return regionStatsCountry;
    }

}

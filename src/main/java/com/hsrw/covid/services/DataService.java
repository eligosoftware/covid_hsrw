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

    private HashMap<String, Latlng> coords;
    private HashMap<String, Integer> population;
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

        population=new HashMap();
        population.put("Afghanistan",38928346);
        population.put("Albania",2877797);
        population.put("Algeria",43851044);
        population.put("American Samoa",55191);
        population.put("Andorra",77265);
        population.put("Angola",32866272);
        population.put("Anguilla",15003);
        population.put("Antigua and Barbuda",97929);
        population.put("Argentina",45195774);
        population.put("Armenia",2963243);
        population.put("Aruba",106766);
        population.put("Australia",25499884);
        population.put("Austria",9006398);
        population.put("Azerbaijan",10139177);
        population.put("Bahamas",393244);
        population.put("Bahrain",1701575);
        population.put("Bangladesh",164689383);
        population.put("Barbados",287375);
        population.put("Belarus",9449323);
        population.put("Belgium",11589623);
        population.put("Belize",397628);
        population.put("Benin",12123200);
        population.put("Bermuda",62278);
        population.put("Bhutan",771608);
        population.put("Bolivia",11673021);
        population.put("Bosnia and Herzegovina",3280819);
        population.put("Botswana",2351627);
        population.put("Bouvet Island",0);
        population.put("Brazil",212559417);
        population.put("British Indian Ocean Territory",0);
        population.put("British Virgin Islands",30231);
        population.put("Brunei",437479);
        population.put("Bulgaria",6948445);
        population.put("Burkina Faso",20903273);
        population.put("Burma",54409800);
        population.put("Burundi",11890784);
        population.put("Cabo Verde",555987);
        population.put("Cambodia",16718965);
        population.put("Cameroon",26545863);
        population.put("Canada",37742154);
        population.put("Cape Verde",0);
        population.put("Cayman Islands",65722);
        population.put("Central African Republic",4829767);
        population.put("Chad",16425864);
        population.put("Channel Islands",173863);
        population.put("Chile",19116201);
        population.put("China",1439323776);
        population.put("Christmas Island",0);
        population.put("Cocos [Keeling] Islands",0);
        population.put("Colombia",50882891);
        population.put("Comoros",869601);
        population.put("Congo (Brazzaville)",5518087);
        population.put("Congo (Kinshasa)",89561403);
        population.put("Cook Islands",17564);
        population.put("Costa Rica",5094118);
        population.put("Cote d'Ivoire",26378274);
        population.put("Croatia",4105267);
        population.put("Cuba",11326616);
        population.put("Curacao",164093);
        population.put("Cyprus",1207359);
        population.put("Czechia",10708981);
        population.put("Denmark",5792202);
        population.put("Diamond Princess",0);
        population.put("Djibouti",988000);
        population.put("Dominica",71986);
        population.put("Dominican Republic",10847910);
        population.put("Ecuador",17643054);
        population.put("Egypt",102334404);
        population.put("El Salvador",6486205);
        population.put("Equatorial Guinea",1402985);
        population.put("Eritrea",3546421);
        population.put("Estonia",1326535);
        population.put("Eswatini",1160164);
        population.put("Ethiopia",114963588);
        population.put("Falkland Islands [Islas Malvinas]",3480);
        population.put("Faroe Islands",48863);
        population.put("Fiji",896445);
        population.put("Finland",5540720);
        population.put("France",65273511);
        population.put("French Guiana",298682);
        population.put("French Polynesia",280908);
        population.put("French Southern Territories",0);
        population.put("Gabon",2225734);
        population.put("Gambia",2416668);
        population.put("Gaza Strip",0);
        population.put("Georgia",3989167);
        population.put("Germany",83783942);
        population.put("Ghana",31072940);
        population.put("Gibraltar",33691);
        population.put("Greece",10423054);
        population.put("Greenland",56770);
        population.put("Grenada",112523);
        population.put("Guadeloupe",400124);
        population.put("Guam",168775);
        population.put("Guatemala",17915568);
        population.put("Guernsey",0);
        population.put("Guinea",13132795);
        population.put("Guinea-Bissau",1968001);
        population.put("Guyana",786552);
        population.put("Haiti",11402528);
        population.put("Heard Island and McDonald Islands",0);
        population.put("Holy See ",801);
        population.put("Honduras",9904607);
        population.put("Hong Kong",7496981);
        population.put("Hungary",9660351);
        population.put("Iceland",341243);
        population.put("India",1380004385);
        population.put("Indonesia",273523615);
        population.put("Iran",83992949);
        population.put("Iraq",40222493);
        population.put("Ireland",4937786);
        population.put("Isle of Man",85033);
        population.put("Israel",8655535);
        population.put("Italy",60461826);
        population.put("Jamaica",2961167);
        population.put("Japan",126476461);
        population.put("Jersey",0);
        population.put("Jordan",10203134);
        population.put("Kazakhstan",18776707);
        population.put("Kenya",53771296);
        population.put("Kiribati",119449);
        population.put("Korea,South",51269185);
        population.put("Kosovo",0);
        population.put("Kuwait",4270571);
        population.put("Kyrgyzstan",6524195);
        population.put("Laos",7275560);
        population.put("Latvia",1886198);
        population.put("Lebanon",6825445);
        population.put("Lesotho",2142249);
        population.put("Liberia",5057681);
        population.put("Libya",6871292);
        population.put("Liechtenstein",38128);
        population.put("Lithuania",2722289);
        population.put("Luxembourg",625978);
        population.put("Macao SAR",0);
        population.put("Macau",649335);
        population.put("Madagascar",27691018);
        population.put("Malawi",19129952);
        population.put("Malaysia",32365999);
        population.put("Maldives",540544);
        population.put("Mali",20250833);
        population.put("Malta",441543);
        population.put("Marshall Islands",59190);
        population.put("Martinique",375265);
        population.put("Mauritania",4649658);
        population.put("Mauritius",1271768);
        population.put("Mayotte",272815);
        population.put("Mexico",128932753);
        population.put("Micronesia",548914);
        population.put("Moldova",4033963);
        population.put("Monaco",39242);
        population.put("Mongolia",3278290);
        population.put("Montenegro",628066);
        population.put("Montserrat",4992);
        population.put("Morocco",36910560);
        population.put("Mozambique",31255435);
        population.put("MS Zaandam",0);
        population.put("Namibia",2540905);
        population.put("Nauru",10824);
        population.put("Nepal",29136808);
        population.put("Netherlands",17134872);
        population.put("Netherlands Antilles",0);
        population.put("New Caledonia",285498);
        population.put("New Zealand",4822233);
        population.put("Nicaragua",6624554);
        population.put("Niger",24206644);
        population.put("Nigeria",206139589);
        population.put("Niue",1626);
        population.put("Norfolk Island",0);
        population.put("North Korea",25778816);
        population.put("North Macedonia",2083374);
        population.put("Northern Mariana Islands",57559);
        population.put("Norway",5421241);
        population.put("Oman",5106626);
        population.put("Pakistan",220892340);
        population.put("Palau",18094);
        population.put("Palestinian Territories",5101414);
        population.put("Panama",4314767);
        population.put("Papua New Guinea",8947024);
        population.put("Paraguay",7132538);
        population.put("Peru",32971854);
        population.put("Philippines",109581078);
        population.put("Pitcairn Islands",0);
        population.put("Poland",37846611);
        population.put("Portugal",10196709);
        population.put("Puerto Rico",2860853);
        population.put("Qatar",2881053);
        population.put("Reunion",895312);
        population.put("Romania",19237691);
        population.put("Russia",145934462);
        population.put("Rwanda",12952218);
        population.put("Saint Barthelemy",9877);
        population.put("Saint Helena",6077);
        population.put("Saint Kitts and Nevis",53199);
        population.put("Saint Lucia",183627);
        population.put("Saint Martin",38666);
        population.put("Saint Pierre and Miquelon",5794);
        population.put("Saint Vincent and the Grenadines",110940);
        population.put("Samoa",198414);
        population.put("San Marino",33931);
        population.put("Sao Tome and Principe ",219159);
        population.put("Saudi Arabia",34813871);
        population.put("Senegal",16743927);
        population.put("Serbia",8737371);
        population.put("Seychelles",98347);
        population.put("Sierra Leone",7976983);
        population.put("Singapore",5850342);
        population.put("Sint Maarten",42876);
        population.put("Slovakia",5459642);
        population.put("Slovenia",2078938);
        population.put("Solomon Islands",686884);
        population.put("Somalia",15893222);
        population.put("South Africa",59308690);
        population.put("South Georgia and the South Sandwich Islands",0);
        population.put("South Sudan",11193725);
        population.put("Spain",46754778);
        population.put("Sri Lanka",21413249);
        population.put("Sudan",43849260);
        population.put("Suriname",586632);
        population.put("Svalbard and Jan Mayen",0);
        population.put("Sweden",10099265);
        population.put("Switzerland",8654622);
        population.put("Syria",17500658);
        population.put("Taipei and environs",0);
        population.put("Taiwan",23816775);
        population.put("Tajikistan",9537645);
        population.put("Tanzania",59734218);
        population.put("Thailand",69799978);
        population.put("Timor-Leste",1318445);
        population.put("Togo",8278724);
        population.put("Tokelau",1357);
        population.put("Tonga",105695);
        population.put("Trinidad and Tobago",1399488);
        population.put("Tunisia",11818619);
        population.put("Turkey",84339067);
        population.put("Turkmenistan",6031200);
        population.put("Turks and Caicos Islands",38717);
        population.put("Tuvalu",11792);
        population.put("U.S. Minor Outlying Islands",0);
        population.put("U.S. Virgin Islands",104425);
        population.put("Uganda",45741007);
        population.put("Ukraine",43733762);
        population.put("United Arab Emirates",9890402);
        population.put("United Kingdom",67886011);
        population.put("Uruguay",3473730);
        population.put("US",331002651);
        population.put("Uzbekistan",33469203);
        population.put("Vanuatu",307145);
        population.put("Vatican City",0);
        population.put("Venezuela",28435940);
        population.put("Vietnam",97338579);
        population.put("Wallis and Futuna",11239);
        population.put("West Bank and Gaza",0);
        population.put("Western Sahara",597339);
        population.put("Yemen",29825964);
        population.put("Zambia",18383955);
        population.put("Zimbabwe",14862924);


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
            double cp1k;
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
                        if(population.get(lastCountry.getName())==null || population.get(lastCountry.getName())==0)
                            cp1k=-88888;
                        else
                            cp1k=Math.round((t_confirmed*1.0/population.get(lastCountry.getName())*100000)*100.0)/100.0;

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
                                t_recovered_diff,cp1k,
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
            if(population.get(lastCountry.getName())==null || population.get(lastCountry.getName())==0)
                cp1k=-88888;
            else
                cp1k=Math.round((t_confirmed*1.0/population.get(lastCountry.getName())*100000)*100.0)/100.0;

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
                    t_recovered_diff,cp1k
                    ,
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
            double cp1k=0;
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
                        if(population.get(lastCountry.getName())==null || population.get(lastCountry.getName())==0)
                            cp1k=-88888;
                        else
                            cp1k=Math.round((t_confirmed*1.0/population.get(lastCountry.getName())*100000)*100.0)/100.0;

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
                                t_recovered_diff,cp1k,
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
            if(population.get(lastCountry.getName())==null || population.get(lastCountry.getName())==0)
                cp1k=-88888;
            else
                cp1k=Math.round((t_confirmed*1.0/population.get(lastCountry.getName())*100000)*100.0)/100.0;

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
                    t_recovered_diff,cp1k,
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
            double cp1k;
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
                        if(population.get(lastCountry.getName())==null || population.get(lastCountry.getName())==0)
                            cp1k=-88888;
                        else
                            cp1k=Math.round((t_confirmed*1.0/population.get(lastCountry.getName())*100000)*100.0)/100.0;
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
                                t_recovered_diff,cp1k,
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
            if(population.get(lastCountry.getName())==null || population.get(lastCountry.getName())==0)

                cp1k=-88888;
            else
                cp1k=Math.round((t_confirmed*1.0/population.get(lastCountry.getName())*100000)*100.0)/100.0;

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
                    t_recovered_diff,cp1k,
                    t_fatality_rate
            ));

        }
    }

    public LineChartResult getStatsTotal_14days() throws IOException, InterruptedException, ParseException{
        Calendar cal = new GregorianCalendar();
        LineChartResult result=new LineChartResult();
        Map<String,Integer> result1=new HashMap();
        Map<String,Double> result2=new HashMap();
        Date date=null;
        cal.add(Calendar.DAY_OF_MONTH, -1);
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
                        result1.put(simpleDateFormat2.format(date), data.getInt("confirmed_diff"));
                        if(data.getInt("recovered_diff")==0)
                            result2.put(simpleDateFormat2.format(date), 0.0);
                        else
                            result2.put(simpleDateFormat2.format(date), Math.round(data.getInt("confirmed_diff")*1.0/data.getInt("recovered_diff")*100.0)/100.0);

                    }
                    else{
                        result1.put(simpleDateFormat2.format(date),0);
                        result2.put(simpleDateFormat2.format(date), 0.0);


                    }
                } catch (Exception e){
                    result1.put(simpleDateFormat2.format(date),0);
                    result2.put(simpleDateFormat2.format(date), 0.0);

                }

            }}
        result.setMap_cases(result1);
        result.setMap_ratio(result2);

        return result;
    }
    public LineChartResult getDeaths_14days_continent(String continent) throws IOException, InterruptedException, ParseException{
        Calendar cal = new GregorianCalendar();
        LineChartResult result=new LineChartResult();
        Map<String,Integer> result1=new HashMap();


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

        Date date=null;
        cal.add(Calendar.DAY_OF_MONTH, -1);
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

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode()==200){

                String body=String.valueOf(response.body()).replace("\n","");
                JSONObject obj = new JSONObject(body);
                JSONArray data=obj.getJSONArray("data");
                int deaths=0;
                for(int i = 0 ; i < data.length() ; i++) {
                    JSONObject row = data.getJSONObject(i);//getString("interestKey");
                    JSONObject regionJson = row.getJSONObject("region");

                    if (!selected_continent.contains(regionJson.getString("name") + ";"))
                        continue;
                    try {
                        if(row.getInt("deaths_diff")>0) {
                              deaths+= row.getInt("deaths_diff");
                        }
                    } catch (Exception e){


                    }
                }
                result1.put(simpleDateFormat.format(date), deaths);
                deaths=0;
            }}
        result.setMap_cases(result1);

        return result;
    }

    public LineChartResult getDeaths_14days_country(String country) throws IOException, InterruptedException, ParseException{
        Calendar cal = new GregorianCalendar();
        LineChartResult result=new LineChartResult();
        Map<String,Integer> result1=new HashMap();

        Date date=null;
        cal.add(Calendar.DAY_OF_MONTH, -1);
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

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode()==200){

                String body=String.valueOf(response.body()).replace("\n","");
                JSONObject obj = new JSONObject(body);
                JSONArray data=obj.getJSONArray("data");
                int deaths=0;
                for(int i = 0 ; i < data.length() ; i++) {
                    JSONObject row = data.getJSONObject(i);//getString("interestKey");

                    try {
                        if(row.getInt("deaths_diff")>0) {
                            deaths+= row.getInt("deaths_diff");
                        }
                    } catch (Exception e){


                    }
                }
                result1.put(simpleDateFormat.format(date), deaths);
            }}
        result.setMap_cases(result1);

        return result;
    }


    public LineChartResult getDeaths_14days() throws IOException, InterruptedException, ParseException{
        Calendar cal = new GregorianCalendar();
        LineChartResult result=new LineChartResult();
        Map<String,Integer> result1=new HashMap();
        Date date=null;
        cal.add(Calendar.DAY_OF_MONTH, -1);
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

            HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode()==200){

                String body=String.valueOf(response.body()).replace("\n","");
                //body=body.replaceAll(" ","");
                JSONObject obj = new JSONObject(body);
                try {
                    JSONObject data=obj.getJSONObject("data");
                    if(data.getInt("confirmed_diff")>0) {
                        result1.put(simpleDateFormat.format(date), data.getInt("deaths_diff"));
                    }
                    else{
                        result1.put(simpleDateFormat.format(date),0);
                    }
                } catch (Exception e){
                    result1.put(simpleDateFormat.format(date),0);

                }

            }}
        result.setMap_cases(result1);

        return result;
    }

    public LineChartResult getStatsTotal_14days_country(String country) throws IOException, InterruptedException, ParseException{
        Calendar cal = new GregorianCalendar();

        Date date=null;
        LineChartResult result=new LineChartResult();
        Map<String,Integer> result1=new HashMap();
        Map<String,Double> result2=new HashMap();
        cal.add(Calendar.DAY_OF_MONTH, -1);
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
                int t_recovered_diff=0;

                for(int i = 0 ; i < data.length() ; i++){
                    JSONObject row=data.getJSONObject(i);//getString("interestKey");
                    JSONObject regionJson=row.getJSONObject("region");
                    //System.out.println(regionJson.getString("lat"));
                    if(lastCountry!=null && regionJson.getString("name").equals(lastCountry.getName())){


                        t_confirmed_diff+=row.getInt("confirmed_diff");
                        t_recovered_diff+=row.getInt("recovered_diff");
                    } else {

                        if(lastCountry!=null){

                            result1.put(simpleDateFormat2.format(date),t_confirmed_diff);
                            if(t_recovered_diff==0) {
                                result2.put(simpleDateFormat2.format(date),0.0);
                            }else {
                                result2.put(simpleDateFormat2.format(date), Math.round(t_confirmed_diff * 1.0 / t_recovered_diff * 100.0) / 100.0);
                            }
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
                            t_recovered_diff=row.getInt("recovered_diff");
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
                            t_recovered_diff=row.getInt("recovered_diff");
                        }
                    }

                }

                result1.put(simpleDateFormat2.format(date),t_confirmed_diff);
                if(t_recovered_diff==0) {
                    result2.put(simpleDateFormat2.format(date),0.0);
                }else {
                    result2.put(simpleDateFormat2.format(date), Math.round(t_confirmed_diff * 1.0 / t_recovered_diff * 100.0) / 100.0);
                }
            }

        }
        result.setMap_cases(result1);
        result.setMap_ratio(result2);
        return result;
    }
    public LineChartResult getStatsTotal_14days_continents(String continent) throws IOException, InterruptedException, ParseException{
        Calendar cal = new GregorianCalendar();

        LineChartResult result=new LineChartResult();
        Map<String,Integer> result1=new HashMap();
        Map<String,Double> result2=new HashMap();
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
        cal.add(Calendar.DAY_OF_MONTH, -1);
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
                int t_recovered_diff=0;
                double ratio=0;
                for(int i = 0 ; i < data.length() ; i++) {
                    JSONObject row = data.getJSONObject(i);//getString("interestKey");
                    JSONObject regionJson = row.getJSONObject("region");

                    if (!selected_continent.contains(regionJson.getString("name") + ";"))
                        continue;

                    t_confirmed_diff += row.getInt("confirmed_diff");
                    t_recovered_diff += row.getInt("recovered_diff");
                }
                result1.put(simpleDateFormat2.format(date),t_confirmed_diff);
                if(t_recovered_diff==0) {
                    result2.put(simpleDateFormat2.format(date),0.0);
                }else{
                    result2.put(simpleDateFormat2.format(date),Math.round(t_confirmed_diff*1.0/t_recovered_diff*100.0)/100.0);
                }
             }

        }
        result.setMap_cases(result1);
        result.setMap_ratio(result2);

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

//                regionStats.add(new LocationStats(region,
//                        null,
//                        simpleDateFormat.parse(row.getString("date")),
//                        simpleDateFormatHours.parse(row.getString("last_update")),
//                        row.getInt("deaths"),
//                        row.getInt("confirmed"),
//                        row.getInt("recovered"),
//                        row.getInt("active"),
//                        row.getInt("active_diff"),
//                        row.getInt("deaths_diff"),
//                        row.getInt("confirmed_diff"),
//                        row.getInt("recovered_diff"),
//                        row.getDouble("fatality_rate")
//                ));
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
//                    cityStats.add(new LocationStats(region,
//                            city,
//                            simpleDateFormat.parse(row_city.getString("date")),
//                            simpleDateFormatHours.parse(row_city.getString("last_update")),
//                            row_city.getInt("deaths"),
//                            row_city.getInt("confirmed"),
//                            0,//row_city.getInt("recovered"),
//                            0,//row_city.getInt("active"),
//                            0,//row_city.getInt("active_diff"),
//                            row_city.getInt("deaths_diff"),
//                            row_city.getInt("confirmed_diff"),
//                            0,//row_city.getInt("recovered_diff"),
//                            0//row_city.getDouble("fatality_rate")
//                    ));
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

    public String getContinentByCountry(String country){
        String europe= "Albania;Andorra;Armenia;Austria;Azerbaijan;Belarus;Belgium;Bosnia and Herzegovina;Bulgaria;Croatia;Cyprus;Channel Islands;Czechia;Denmark;Estonia;Faroe Islands;Finland;France;Georgia;Germany;Gibraltar;Guernsey;Greece;Holy See;Hungary;Iceland;Ireland;Italy;Jersey;Kosovo;Latvia;Liechtenstein;Lithuania;Luxembourg;Mayotte;Malta;Moldova;MS Zaandam;Monaco;Montenegro;Netherlands;North Macedonia;Norway;Poland;Portugal;Romania;Russia;San Marino;Serbia;Slovakia;Slovenia;Spain;Sweden;Switzerland;Turkey;Ukraine;United Kingdom;";
        String asia="Afghanistan;Bahrain;Bangladesh;Bhutan;Brunei;Burma;Cambodia;China;India;Indonesia;Iran;Iraq;Israel;West Bank and Gaza;Japan;Jordan;Kazakhstan;Korea, South;Kyrgyzstan;Kuwait;Laos;Lebanon;Malaysia;Macao SAR;Maldives;Mongolia;Nepal;Oman;Pakistan;Philippines;Qatar;Saudi Arabia;Singapore;Sri Lanka;Syria;Taiwan;Tajikistan;Thailand;Timor-Leste;Turkmenistan;Taipei and environs;United Arab Emirates;Uzbekistan;Vietnam;Yemen;";
        String africa="Algeria;Angola;Benin;Botswana;Tanzania;Burkina Faso;Western Sahara;Burundi;Cabo Verde;Cameroon;Central African Republic;Comoros;Chad;Malawi;Sierra Leone;Sao Tome and Principe;Seychelles;Congo (Brazzaville);Congo (Kinshasa);Cote d'Ivoire;Djibouti;Egypt;Equatorial Guinea;Guinea;Guinea-Bissau;Eritrea;Eswatini;South Sudan;Reunion;Ethiopia;Gabon;Gambia;Ghana;Kenya;Liberia;Libya;Lesotho;Madagascar;Mali;Mauritania;Mauritius;Morocco;Mozambique;Namibia;Niger;Nigeria;Rwanda;Senegal;Somalia;South Africa;Sudan;Togo;Tunisia;Uganda;Zambia;Zimbabwe;";
        String oceania="Australia;Fiji;Guam;New Zealand;Papua New Guinea;Kiribati;Marshall Islands;Solomon Islands;Vanuatu;";
        String north_america="Antigua and Barbuda;Aruba;Bahamas;Barbados;Belize;Canada;Cayman Islands;Costa Rica;Cuba;Dominica;Dominican Republic;El Salvador;Greenland;Grenada;Guadeloupe;Guatemala;Haiti;Honduras;Jamaica;Martinique;Mexico;Nicaragua;Panama;Puerto Rico;Saint Kitts and Nevis;Saint Lucia;Saint Martin;Saint Vincent and the Grenadines;Saint Barthelemy;Trinidad and Tobago;US;";
        String south_america="Argentina;Bolivia;Brazil;Chile;Colombia;Curacao;Ecuador;French Guiana;Guyana;Paraguay;Peru;Suriname;Uruguay;Venezuela;";

        if (europe.contains(country+";"))
            return "europe";
        else if (asia.contains(country+";"))
            return "asia";
        else if (africa.contains(country+";"))
            return "africa";
        else if (oceania.contains(country+";"))
            return "oceania";
        else if (north_america.contains(country+";"))
            return "north_america";
        else if (south_america.contains(country+";"))
            return "south_america";

        return "continent_not_found";
    }
}

package com.hsrw.covid.database;

import com.hsrw.covid.models.LocationStats;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class db_ops {
    public static final DateFormat SQL_DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat MYSQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
    public static int max_stat;
    public static void registerRow(String country,String province,double lat, double lon,Date date, int number_of_cases){
        config conf=config.newInstance();
        Connection con=conf.getCon();
        if(con!=null) {
            long country_id = 0;
            if (!country.trim().equals("")) {
                country_id = registerCountryProvince(1, country, con);
            }
            long province_id = 0;
            if (!province.trim().equals("")) {
                province_id = registerCountryProvince(2, province, con);
            }
            registerLocation(country_id,province_id,lat,lon,con);
            registerstat(date,country_id,province_id,number_of_cases,con);
        }
    }

//    public static ArrayList<LocationStats> returnStats(){
//        ArrayList<LocationStats> result=new ArrayList();
//        config conf=config.newInstance();
//        Connection con=conf.getCon();
//        if(con!=null) {
//            String sql="select c.name as country,p.name as province,l.lat, l.lon,sum(s.n_cases) as total,s2.last_day from stats as s " +
//                    "left join countries as c " +
//                    "on s.country_id=c.id " +
//                    "left join provinces as p " +
//                    "on s.province_id=p.id " +
//                    "left join locations as l " +
//                    "on s.country_id=l.country_id and (l.province_id is null or s.province_id=l.province_id) " +
//                    "left join (select country_id, province_id,n_cases as last_day from stats " +
//                    "where stat_date=(select max(stat_date) from stats) " +
//                    ") as s2 " +
//                    "on s.country_id=s2.country_id " +
//                    " and ((s2.province_id is null) or s.province_id=s2.province_id) " +
//                    "group by c.name, p.name,s2.last_day, l.lat, l.lon";
//
//            try{
//                PreparedStatement stmt = con.prepareStatement(sql);
//                ResultSet rs = stmt.executeQuery();
//                while(rs.next()){
//                    LocationStats stat=new LocationStats(rs.getString(2),rs.getString(1));
//                    stat.setLat(rs.getDouble(3));
//                    stat.setLong(rs.getDouble(4));
//                    stat.setLatestTotalCases(rs.getInt(5));
//                    stat.setDiffFromPreviousDay(rs.getInt(6));
//
//
//                    result.add(stat);
//
//                    if(rs.getInt(5)>max_stat)
//                        max_stat=rs.getInt(5);
//
//                }
//
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }


    public static int getMax_stat() {
        return max_stat;
    }

    private static void registerstat(Date date, long country_id, long province_id, int number_of_cases, Connection con) {
        String sql;
        if(province_id==0){
            sql="select * from stats where stat_date = ? and country_id = ? and " +
                    "province_id is NULL";
        } else {
            sql="select * from  stats where stat_date = ? and country_id = ? and " +
                    "province_id = ?";
        }
        try{
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, MYSQL_DATE_FORMAT.format(date));
            stmt.setInt(2, (int) country_id);
            if(province_id!=0)
                stmt.setInt(3, (int) province_id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                return;


        String SQL_INSERT;
        if(province_id==0)
            SQL_INSERT="insert into stats(stat_date,country_id, province_id,n_cases) values(?,?,NULL,?)";
        else
            SQL_INSERT="insert into stats(stat_date,country_id, n_cases,province_id) values(?,?,?,?)";

        PreparedStatement insertStatement=con.prepareStatement(SQL_INSERT);
        insertStatement.setString(1, MYSQL_DATE_FORMAT.format(date));
        insertStatement.setInt(2, (int) country_id);
        insertStatement.setInt(3, number_of_cases);
        if(province_id!=0)
            insertStatement.setInt(4, (int) province_id);


        insertStatement.executeUpdate();} catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void registerLocation(long country_id, long province_id, double lat, double lon,Connection con) {
        String sql;
        if(province_id==0){
            sql="select * from  locations where country_id = ? and " +
                    "province_id is NULL";
        } else {
            sql="select * from  locations where country_id = ? and " +
                    "province_id = ?";
        }
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, (int) country_id);
            if(province_id!=0)
                stmt.setInt(2, (int) province_id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next())
                return;
            // come here if you couldnt find anything
            String SQL_INSERT="insert into locations(country_id, province_id,lat,lon) values(?,?,?,?)";
            PreparedStatement insertStatement=con.prepareStatement(SQL_INSERT);
            insertStatement.setInt(1, (int) country_id);
            if(province_id!=0){
            insertStatement.setInt(2, (int) province_id);}
            else {
                insertStatement.setNull(2, Types.INTEGER);
            }
            insertStatement.setDouble(3, lat);
            insertStatement.setDouble(4, lon);

            insertStatement.executeUpdate();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static int registerCountryProvince(int type,String value,Connection con){
        String table_name;
        switch (type){
            case 1://country
                table_name="countries";
                break;
            case 2://province
                table_name="provinces";
                break;
            default:
                return -1;
        }
        try {
            PreparedStatement stmt = con.prepareStatement("select id from "+table_name+" where name = ?");
            stmt.setString(1,value);
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                return rs.getInt(1); // return id of first found name
            }

            // come here if you couldnt find anything
            String SQL_INSERT="insert into "+table_name+"(name  ) values(?)";
            PreparedStatement insertStatement=con.prepareStatement(SQL_INSERT,Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1,value);

            int affectedRows = insertStatement.executeUpdate();

            if (affectedRows != 0) {
                try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return (int) generatedKeys.getLong(1);
                    }
                }
            }
        }
        catch (SQLException sqlException){
            return -1;
        }
        return -1;
    }

    public static int getStartIndex(Date startDate){
        config conf=config.newInstance();
        Connection con=conf.getCon();
        if(con!=null){


            PreparedStatement stmt = null;
            try {
                stmt = con.prepareStatement("select datediff((select coalesce(max(stat_date),?) from stats),?)");

            stmt.setString(1,SQL_DATE_FORMAT.format(startDate));
            stmt.setString(2,SQL_DATE_FORMAT.format(startDate));

            ResultSet rs = stmt.executeQuery();
            while (rs.next())
            {
                return rs.getInt(1); // return id of first found name
            }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

        }
        return 0;
    }
}

package com.hsrw.covid.database;

import org.springframework.util.ResourceUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.sql.*;

public class config {
    private static config connection;

    private String url;
    private String username;
    private String password;

    public Connection getCon() {
        return con;
    }

    private Connection con;

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void connect(){
        try{
            //Class.forName("com.mysql.jdbc.Driver");

            disconnect();
            con=DriverManager.getConnection(
                    url,username,password);
////here sonoo is database name, root is username and password
//            Statement stmt=con.createStatement();
//            ResultSet rs=stmt.executeQuery("select * from test");
//            while(rs.next())
//                System.out.println(rs.getInt(1));

        }catch (Exception e){
          //  System.out.println(e.getMessage());
        }
    }

    public void disconnect(){
        if(con!=null){
            try {
                con.close();
                con=null;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    private void setFromConfig() {
        try {
            File file = ResourceUtils.getFile("classpath:config.xml");
            //InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.xml");
            InputStream input = new FileInputStream(file);
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(input));
            XPath xpath = XPathFactory.newInstance().newXPath();
            String url = (String) xpath.compile("//config//jdbc//url").evaluate(document, XPathConstants.STRING);
            //String driver = (String) xpath.compile("//config//jdbc//driver").evaluate(document, XPathConstants.STRING);
            String username = (String) xpath.compile("//config//jdbc//username").evaluate(document, XPathConstants.STRING);
            String password = (String) xpath.compile("//config//jdbc//password").evaluate(document, XPathConstants.STRING);
            this.setUrl(url);
            this.setUsername(username);
            this.setPassword(password);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private config(){
    }
    public static config newInstance(){
        if(connection==null){
            connection=new config();
            try{
            connection.setFromConfig();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return connection;
    }
}

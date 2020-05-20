/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.test.staffapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jane G
 */
public class Staff{
    private String name;
    private String membership;
    private String email;
    private String phone;
    private String address;
    private String profileURL;
    private String status;
    private String star;
    private Connection con;
    private Statement st;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(Staff.class);

    public Staff(String n, String m, String e, String p, String a, String url, String star, String status) throws SQLException{
        name = n;
        membership = m;
        email = e;
        phone = p;
        address = a;
        profileURL = url;
        this.status = status;
        this.star = star;
        String DBurl = "jdbc:derby://localhost:1527/FinalPractice";
        String user = "JANEG";
        String password = "JANEG";
        con = DriverManager.getConnection(DBurl, user, password);
        st = con.createStatement();
        
        ResultSet rs = con.getMetaData().getTables(null,null, "staffList", null);
    }
    public Staff(String n, String m, String e, String p, String a, String url) throws SQLException{
        name = n;
        membership = m;
        email = e;
        phone = p;
        address = a;
        profileURL = url;
        this.status = "pending";
        this.star = "no";
        String DBurl = "jdbc:derby://localhost:1527/FinalPractice";
        String user = "JANEG";
        String password = "JANEG";
        con = DriverManager.getConnection(DBurl, user, password);
        st = con.createStatement();
        
        ResultSet rs = con.getMetaData().getTables(null,null, "staffList", null);
    }

    public String getStatus(){
        return status;
    }
    public String getStar(){
        return star;
    }
    public String getName(){
        return name;
    }
    public String getMembership(){
        return membership;
    }
    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
    }
    public String getAddress(){
        return address;
    }
    public String getProfileURL(){
        return profileURL;
    }
    public String getJSON(){
        return "{\"name\":\""+name+"\","
                + "\"membership\":\""+membership+"\","
                + "\"phone\":\""+phone+"\","
                + "\"email\":\""+email+"\","
                + "\"profileURL\":\""+profileURL+"\","
                + "\"star\":\""+star+"\","
                + "\"status\":\""+status+"\","
                + "\"address\":\""+address+"\"}";
    }
    
    public void deleteFromDB() throws SQLException{
        int res = st.executeUpdate("DELETE FROM STAFFLIST WHERE name=\'"+ name + "\'");
        logger.info("delete " + name);
    }
}

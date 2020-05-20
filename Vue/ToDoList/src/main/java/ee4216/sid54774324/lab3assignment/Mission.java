/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.sid54774324.lab3assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Jane G
 */
public class Mission {
    private String title;
    private String deadline;
    private String star;
    private String status;
    
    public Mission(String title, String deadline, String star, String status){
        this.title = title;
        this.deadline = deadline;
        this.star = star;
        this.status = status;
    }
    
    public String getJson(){
        String result = "{\"title\":\"";
        result += this.title;
        result += "\", \"deadline\":\"";
        result += this.deadline;
        result += "\", \"star\":\"";
        result += this.star;
        result += "\", \"status\":\"";
        result += this.status;
        result += "\"},\n";

        return result;
    }
    
    public String getJsonEnd(){
        String result = "{\"title\":\"";
        result += this.title;
        result += "\", \"deadline\":\"";
        result += this.deadline;
        result += "\", \"star\":\"";
        result += this.star;
        result += "\", \"status\":\"";
        result += this.status;
        result += "\"}\n";

        return result;
    }
    
    public boolean checkDBExistence() throws SQLException{
        String url = "jdbc:derby://localhost:1527/missions";
        String user = "JANEG";
        String password = "JANEG";    
        Connection con = DriverManager.getConnection(url, user, password);
        Statement st = con.createStatement();
        
        ResultSet rs = con.getMetaData().getTables(null,null, "MISSION", null);
        if(!rs.next()){
            int res = st.executeUpdate("CREATE TABLE MISSION("
                    + "title VARCHAR(8000) NOT NULL,"
                    + "deadline VARCHAR(8000) NOT NULL,"
                    + "star VARCHAR(8000) NOT NULL,"
                    + "status VARCHAR(8000) NOT NULL"
                    + ")");
            return false;
        }
        
        rs = st.executeQuery("SELECT * FROM MISSION WHERE title = \'"+ this.title + "\'");
        if(!rs.next()){
            return false;
        }
        return true;
    }
    
    public boolean checkDBMatch() throws SQLException{
        String url = "jdbc:derby://localhost:1527/missions";
        String user = "JANEG";
        String password = "JANEG";    
        Connection con = DriverManager.getConnection(url, user, password);
        Statement st = con.createStatement();
        
        ResultSet rs = con.getMetaData().getTables(null,null, "MISSION", null);
        if(!rs.next()){
            int res = st.executeUpdate("CREATE TABLE MISSION("
                    + "title VARCHAR(8000) NOT NULL,"
                    + "deadline VARCHAR(8000) NOT NULL,"
                    + "star VARCHAR(8000) NOT NULL,"
                    + "status VARCHAR(8000) NOT NULL"
                    + ")");
            return false;
        }
        
        rs = st.executeQuery("SELECT * FROM MISSION "
                + "WHERE title = \'"+ this.title + "\'"
                + "AND deadline =\'" + this.deadline + "\'"
                + "AND star = \'" + this.star + "\'"
                + "AND status = \'" + this.status + "\'");
        if(!rs.next()){
            return false;
        }
        return true;
    }
    
    public void deleteFromDB() throws SQLException{
        String url = "jdbc:derby://localhost:1527/missions";
        String user = "JANEG";
        String password = "JANEG";    
        Connection con = DriverManager.getConnection(url, user, password);
        Statement st = con.createStatement();
        
        ResultSet rs = con.getMetaData().getTables(null,null, "MISSION", null);
        if(!rs.next()){
            int res = st.executeUpdate("CREATE TABLE MISSION("
                    + "title VARCHAR(8000) NOT NULL,"
                    + "deadline VARCHAR(8000) NOT NULL,"
                    + "star VARCHAR(8000) NOT NULL,"
                    + "status VARCHAR(8000) NOT NULL"
                    + ")");
        }
        
        int res = st.executeUpdate("DELETE FROM MISSION WHERE title = \'" + this.title + "\'");
    }
    
    public void storeToDB() throws SQLException{
        String url = "jdbc:derby://localhost:1527/missions";
        String user = "JANEG";
        String password = "JANEG";    
        Connection con = DriverManager.getConnection(url, user, password);
        Statement st = con.createStatement();
        
        ResultSet rs = con.getMetaData().getTables(null,null, "MISSION", null);
        if(!rs.next()){
            int res = st.executeUpdate("CREATE TABLE MISSION("
                    + "title VARCHAR(8000) NOT NULL,"
                    + "deadline VARCHAR(8000) NOT NULL,"
                    + "star VARCHAR(8000) NOT NULL,"
                    + "status VARCHAR(8000) NOT NULL"
                    + ")");
        }
        String sql = "INSERT INTO MISSION("
                + "title,"
                + "deadline,"
                + "star,"
                + "status)"
                + " VALUES (?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, this.title);
        pst.setString(2, this.deadline);
        pst.setString(3, this.star);
        pst.setString(4, this.status);
        
        int res = pst.executeUpdate();
    }
}

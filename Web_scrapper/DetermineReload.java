/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jane G
 */
public class DetermineReload {
    private static Timestamp DBTimeStamp;
    private static Timestamp curTime;

    public DetermineReload(Timestamp cur) throws SQLException, ClassNotFoundException{
        getDBTimeStamp();
        DetermineReload.curTime = cur;
    }

    // public static void main(String[] args) throws SQLException, ClassNotFoundException{
    //     Timestamp t = new Timestamp(System.currentTimeMillis());
    //     DetermineReload d = new DetermineReload(t);
    //     System.out.print("result = ");
    //     if(d.isExpired())
    //         System.out.println(true);
    //     else
    //         System.out.println(false);
    // }

    public boolean isExpired(){
        long storedTime = DBTimeStamp.getTime() + 2 * 60 * 1000;    // 2 min, 60 sec, 1000 millisec
        long cur = curTime.getTime();
        if(cur >= storedTime)
            return true;
        return false;
    }

    private static void getDBTimeStamp() throws SQLException, ClassNotFoundException{
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
    
        String url = "jdbc:derby://localhost:1527/bookRecord";
        String user = "JaneG";
        String password = "JaneG";

        con = DriverManager.getConnection(url, user, password);

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM LASTUPDATE");
            
            while(rs.next())
                DetermineReload.DBTimeStamp = rs.getTimestamp("t");

        } catch (SQLException ex) {
            Logger.getLogger(DetermineReload.class.getName()).log(Level.SEVERE, ex.getMessage());
        }

        con.close();
    }
}

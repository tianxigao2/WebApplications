/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConnection {
    private static ArrayList<ArrayList<String>> book_record_list;
    static int size;
    static int attributeCount;
    // private static String t;
    private static Timestamp t;

    public DBConnection(ArrayList<ArrayList<String>> book_record_list) throws SQLException {
        DBConnection.book_record_list = book_record_list;
        DBConnection.size = book_record_list.size();
        DBConnection.attributeCount = book_record_list.get(0).size();
        System.out.println("DB connection established, "+size+" records in total.");
        
        insert();
    }

    public static void insert() throws SQLException {

        Connection con = null;
        PreparedStatement pst = null;
        Savepoint save = null;
        // ResultSet rs = null;

        String url = "jdbc:derby://localhost:1527/bookRecord";
        String user = "JaneG";
        String password = "JaneG";

        con = DriverManager.getConnection(url, user, password);
        con.setAutoCommit(false);               //turn off auto-commit
        
        try {
            save = con.setSavepoint();          //create a save point
            
            for(int i = 0; i < DBConnection.size; i ++){
                String sql = "INSERT INTO BOOK (ranking, img_src, title, author, rating, price) VALUES (?, ?, ?, ?, ?, ?)";

                pst = con.prepareStatement(sql);
                for(int j = 0; j < DBConnection.attributeCount; j ++){
                    pst.setString(j+1, DBConnection.book_record_list.get(i).get(j));
                }

                int res = pst.executeUpdate();
                // if(res>0)
                //     System.out.println("DB insertion success.");
            }

            Timestamp ts = new Timestamp(System.currentTimeMillis());
            DBConnection.t = ts;
            // System.out.println(DBConnection.t.toString());

            String sql = "INSERT INTO LASTUPDATE (t) VALUES (?)";
            pst = con.prepareStatement(sql);
            pst.setTimestamp(1, DBConnection.t);
            pst.executeUpdate();

            pst.close();

        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, ex.getMessage());
            //rollback if exception occurs
            con.rollback(save);
            //If no save point is provided, the rollback method roll back to the last committed state.
        } finally {
            //release the save point after use
            con.releaseSavepoint(save);
            con.commit();
        }

        con.close();
    }
}

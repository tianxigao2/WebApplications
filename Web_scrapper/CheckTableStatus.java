/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Jane G
 */

public class CheckTableStatus{
    public CheckTableStatus() throws SQLException, Exception{
        Connection con = null;
        Statement st = null;
        ResultSet rs;

        String url = "jdbc:derby://localhost:1527/bookRecord";
        String user = "JaneG";
        String password = "JaneG";

        con = DriverManager.getConnection(url, user, password);
        st = con.createStatement();
        try{
            rs = st.executeQuery("SELECT COUNT(*) FROM BOOK");
            System.out.println("running try successfully");
        }
        catch (SQLException ex){
            // if the table does not exist
            // similarly, the BOOK table will not exist as well
            
            System.out.println("running exception");

            st.executeUpdate(
                "CREATE TABLE BOOK("
                + "ranking VARCHAR(32) NOT NULL PRIMARY KEY,"
                + "title VARCHAR(8000) NOT NULL,"
                + "img_src VARCHAR(8000) NOT NULL,"
                + "author VARCHAR(8000) DEFAULT NULL,"
                + "rating VARCHAR(32) NOT NULL,"
                + "price VARCHAR(32) NOT NULL"
                +")"
                );
            st.executeUpdate(
                "CREATE TABLE LASTUPDATE(t TIMESTAMP NOT NULL)"
                );

            WebScrapper w = new WebScrapper();
            ArrayList<ArrayList<String>> book_record_list = w.getBookRecordList();
            System.out.println("Web scrapper successfully established. " + book_record_list.size() + " records in total.");
    
            DBConnection db = new DBConnection(book_record_list);
        }

        con.close();
    }

//        public static void main(String[] args) throws SQLException {
//            CheckTableStatus initializer = new CheckTableStatus();
//        }
}

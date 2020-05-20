/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.test.staffapi;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jane G
 */
public class StaffScraper {
    private ArrayList<String> urlList;
    private ArrayList<Staff> staffList;
    private Connection con;
    
    public StaffScraper(String indexURL) throws IOException, SQLException{
        urlList = new ArrayList();
        listPageURLs(indexURL);
        
        staffList = new ArrayList();
        for(String url: urlList){
            listStaffInfo(url);
        }
        
        String url = "jdbc:derby://localhost:1527/FinalPractice";
        String user = "JANEG";
        String password = "JANEG";
        con = DriverManager.getConnection(url, user, password);
        storeToDB();
    }
    
    public void listPageURLs(String indexURL) throws IOException{
        Document doc = Jsoup.connect(indexURL).get();
        Elements pages = doc.select("ul.list-group a.is-inactive");
        for(Element page: pages){
            urlList.add(page.attr("abs:href"));
        }
    }
    
    public void listStaffInfo(String url) throws IOException, SQLException{
//        System.out.println("new page---------------------------------------------------------");
        Document doc = Jsoup.connect(url).get();
        Elements staffs = doc.select("div.views-row div.scholar-views-right");
        for(Element staff: staffs){
            String name = staff.select(".name .en").text();
            String membership = staff.select(".scholar-positions").text();
            String email = staff.select(".email a").attr("href");
            String phone = staff.select(".tels a").attr("href");
            String address = staff.select(".address-desc").text();
            String profileURL = staff.select(".view-profile a").attr("abs:href");
            
            Staff s = new Staff(name, membership, email, phone, address, profileURL);
            staffList.add(s);
        }
    }
    
    public void storeToDB() throws SQLException{
        Statement st = con.createStatement();
        ResultSet rs = con.getMetaData().getTables(null,null, "staffList", null);
        if(!rs.next()){
            int res = st.executeUpdate(""
                    + "CREATE TABLE staffList("
                    + "name VARCHAR(8000),"
                    + "membership VARCHAR(8000),"
                    + "phone VARCHAR(8000),"
                    + "email VARCHAR(8000),"
                    + "profileURL VARCHAR(8000),"
                    + "address VARCHAR(8000),"
                    + "star VARCHAR(8000),"
                    + "status VARCHAR(8000)"
                    + ")");
        }
        
        String sql = "INSERT INTO staffList("
                + "name, membership, phone, email, profileURL, address, star, status)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement pst = con.prepareStatement(sql);
        for(int i = 0; i <staffList.size(); i ++){
            Staff s = staffList.get(i);
            pst.setString(1, s.getName());
            pst.setString(2, s.getMembership());
            pst.setString(3, s.getPhone());
            pst.setString(4, s.getEmail());
            pst.setString(5, s.getProfileURL());
            pst.setString(6, s.getAddress());
            pst.setString(7, s.getStar());
            pst.setString(8, s.getStatus());
            
            int res = pst.executeUpdate();
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        StaffScraper ss = new StaffScraper("https://www.cityu.edu.hk/directories/people/academic");
    }
    
}

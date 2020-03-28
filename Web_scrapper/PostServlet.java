/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Jane G
 */
@WebServlet(name = "PostServlet", urlPatterns = {"/PostServlet"})
public class PostServlet extends HttpServlet {
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            CheckTableStatus initializer = new CheckTableStatus();
        } catch (SQLException ex) {
            Logger.getLogger(PostServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PostServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
                        
        try {           

            response.setContentType("text/html");
            PrintWriter output = response.getWriter();
            
            output.println("<!DOCTYPE html>");
            output.println("<title>Amazon Best Sellers</title>");
            output.println("<link rel='stylesheet' href='style.css'/>");
            output.println("<script>function closeModal(){document.getElementById('myModal').style.display='none';}</script>");
            output.println("</head><body><header>Amazon Best Sellers (Top 100)<br/><br/>");
            output.println("<form action='/54774324/PostServlet' method='POST'><input type='submit' value='Reload the List' class='reloadBtn'></form>");
            output.println("<form action='/54774324/GetServlet' method='GET' class='searchArea'>Enter the ranking here and search: <input type='number' name='searchingRank'></form>");
            output.println("</header>");
            output.println("<div class='row'><div id='unorderedList' class='center'>");
            output.println("<div class='center'>");


            // determine whether to reload or not
            Timestamp cur = new Timestamp(System.currentTimeMillis());
            DetermineReload d = new DetermineReload(cur);
            if(d.isExpired()){
                // output.println("<p class='container'>Over 2 min -> DB data reload!</p>");

                // clear all the information stored in DB now
                Connection con = null;
                Statement st = null;
                int rs;

                String url = "jdbc:derby://localhost:1527/bookRecord";
                String user = "JaneG";
                String password = "JaneG";

                con = DriverManager.getConnection(url, user, password);

                // output.println("<p class='container'>Connection for delete is constructed!</p>");

                st = con.createStatement();
                rs = st.executeUpdate("TRUNCATE TABLE LASTUPDATE");
                rs = st.executeUpdate("TRUNCATE TABLE BOOK");

                con.close();
                
                // scrap the data from Amazon again
                WebScrapper w = null;
                try {
                    w = new WebScrapper();
                } catch (Exception ex) {
                    Logger.getLogger(PostServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                ArrayList<ArrayList<String>> list = w.getBookRecordList();
                // output.println("<p class='container'>List size = " + list.size() + "</p>");

                // store the new data to DB
                DBConnection db = new DBConnection(list);
            }
            else{
                // output.println("<p class='container'>Within 2 min -> DB data not changed!</p>");
            }
            
            // extract data from the DB
            LoadFromDB l;
            ArrayList<ArrayList<String>> list_db = new ArrayList();
            try {
                l = new LoadFromDB();
                list_db = l.get();
            } catch (SQLException ex) {
                Logger.getLogger(PostServlet.class.getName()).log(Level.SEVERE, null, ex);
            }



            // information list
            if(list_db.size() == 0){
                output.println("<p>DATABASE LOADING ERROR</p>");
            }
            for(int i = 0; i < list_db.size(); i ++){
                output.println("<div class='sec-ranking'> Ranking: " + list_db.get(i).get(0) + "</div>");
                output.println("<div class='container'>");
                output.println("<img class='sec-img' src='" + list_db.get(i).get(1) + "'></img>");
                output.println("<div class='sec-right'>");
                output.println("<div class='sec-title'> <br/>" + list_db.get(i).get(2) + "</div>");
                output.println("<div class='sec-author'> <br/><br/>" + list_db.get(i).get(3)+"</div>");
                output.println("<div class='sec-price'> <br/>" + list_db.get(i).get(4) + "</div>");
                output.println("<div class='sec-rating'> <br/>Rating: " + list_db.get(i).get(5) + "</div>");
                output.println("</div><br/><br/></div>");
            }

            output.println("</div></div>");
            output.println("<div id='myModal' class='modal'><div class='modal-content'><span class='close' onclick='closeModal();'>&times;");
            output.println("</span><p style='font-size:large;'>This page will display the Amazon best sellers, the list updated hourly.</p></div></div></body></html>");
            
            output.close();

            
        } catch (SQLException ex) {
            Logger.getLogger(PostServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PostServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

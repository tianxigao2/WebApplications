/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.sid54774324.lab3assignment;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
@WebServlet(name = "LoadPreviousMission", urlPatterns = "/missions")
public class LoadPreviousMission extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("application/json; charset=utf-8");
        
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

        String JsonOutput = "[";
        
        ArrayList<Mission> m_list = new ArrayList();
        rs = st.executeQuery("SELECT * FROM MISSION");
            while(rs.next()){
                String title = rs.getString("title");
                String deadline = rs.getString("deadline");
                String star = rs.getString("star");
                String status = rs.getString("status");

                Mission m = new Mission(title, deadline, star, status);
                m_list.add(m);
            }
            
        if(m_list.size() > 0){
            for(int i = 0; i < m_list.size() - 1; i ++){
                JsonOutput += m_list.get(i).getJson();
            }

            JsonOutput += m_list.get(m_list.size()-1).getJsonEnd();
        }
        
        JsonOutput += "]";
        
        try (PrintWriter out = response.getWriter()) {
            out.println(JsonOutput);
            out.flush();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LoadPreviousMission.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LoadPreviousMission.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

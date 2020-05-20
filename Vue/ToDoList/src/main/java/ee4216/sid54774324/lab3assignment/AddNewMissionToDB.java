/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.sid54774324.lab3assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
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
@WebServlet(name = "AddNewMissionToDB", urlPatterns = {"/newMission"})
public class AddNewMissionToDB extends HttpServlet {

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
        BufferedReader br = request.getReader();
        String str, wholeStr = "";
        while((str = br.readLine()) != null){
            wholeStr += str;
        }
//        System.out.println(wholeStr);
        
        int title_idx = wholeStr.indexOf("title");
        int deadline_idx = wholeStr.indexOf("deadline");
        int star_idx = wholeStr.indexOf("star");
        int status_idx = wholeStr.indexOf("status");
        
        String title = wholeStr.substring(title_idx + 8, deadline_idx - 3);
        String deadline = wholeStr.substring(deadline_idx + 11, star_idx - 3);
        String star = wholeStr.substring(star_idx + 7, status_idx - 3);
        String status = wholeStr.substring(status_idx + 9, wholeStr.length() - 2);
        
        Mission m = new Mission(title, deadline, star, status);
        System.out.print(m.getJson());
        
        if(!m.checkDBExistence()){
            m.storeToDB();
            System.out.println("---------------------stored new-----------------------------------");
        }
        else{
            if(!m.checkDBMatch()){
                m.deleteFromDB();
                m.storeToDB();
            }
        }
//        response.setContentType("text/html;charset=UTF-8");        
//        try (PrintWriter out = response.getWriter()) {
//        }
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
            Logger.getLogger(AddNewMissionToDB.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(AddNewMissionToDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

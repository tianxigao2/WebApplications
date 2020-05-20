/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.sql.SQLException;
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
@WebServlet(name = "GetServlet", urlPatterns = {"/GetServlet"})
public class GetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        PrintWriter output;
        // content type
        response.setContentType("text/html");
        output = response.getWriter();

        String ranking = request.getParameter("searchingRank");
        int key = Integer.parseInt(ranking);
        ranking = "#" + ranking;
        
        output.println("<!DOCTYPE html>");
        output.println("<title>Amazon Best Sellers</title>");
        output.println("<link rel='stylesheet' href='style.css'/>");
        output.println("<script>function closeModal(){document.getElementById('myModal').style.display='none';}</script>");
        output.println("</head>"+
                "<body>"+
                    "<header>Amazon Best Sellers (Top 100)<br/><br/>");
        output.println("<form action='/54774324/PostServlet' method='POST'>"
                            +"<input type='submit' value='Reload the List' class='reloadBtn'>"
                        +"</form>");
        output.println("<form action='/54774324/GetServlet' method='GET' class='search-container'>"
                            +"Enter the ranking here and search: <input type='number' name='searchingRank'>"
                        +"</form>");
        output.println("</header>");
        output.println("<div class='row'><div id='unorderedList' class='center'>");
        output.println("<div class='center'>");

        
        // extract data from the DB
        LoadFromDB l;
        ArrayList<ArrayList<String>> list_db = new ArrayList();
        try {
            l = new LoadFromDB();
            list_db = l.get();
        } catch (SQLException ex) {
            Logger.getLogger(PostServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        // add the search result box before all
        output.println("<div class='search-result'> Searching Result: "+ ranking + "</div>");
        output.println("<div class='container'>");
        output.println("    <img class='sec-img' src='"+list_db.get(key).get(1)+"'></img>");
        output.println("    <div class='sec-right'>");
        output.println("        <div class='sec-title'>"+list_db.get(key).get(2)+"</div>");
        output.println("        <div class='sec-author'>"+list_db.get(key).get(2)+"</div>");
        output.println("    </div></br></br>");
        output.println("</div>");

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
    }
}

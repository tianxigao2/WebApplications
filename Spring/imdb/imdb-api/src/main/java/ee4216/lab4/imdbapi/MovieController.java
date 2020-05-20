/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.lab4.imdbapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jane G
 */
@RestController
@RequestMapping(path="/api")                // base URL for all handlers
public class MovieController  implements CommandLineRunner{
    private ArrayList<Movie> movie_list = new ArrayList();
    
    @GetMapping("/status")                  // map to /api/status
    public String status() {
        return "working";
    }
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        jdbcTemplate.query(
                "SELECT * FROM MOVIES",
                (rs, rowNum) -> new Movie(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("year"),
                        rs.getString("rank")
                )
        ).forEach(movie -> movie_list.add(movie));
    }
    
    @GetMapping(path="/movies", produces="application/json")
    public String displayMovies() throws SQLException {
        String result = "[";
        for(int i = 0; i < movie_list.size() - 1; i ++){
            result += movie_list.get(i).getJson();
        }
        result += movie_list.get(movie_list.size() - 1).getJsonEnd();
        
        result += "]";
        
        return result;
    }
}

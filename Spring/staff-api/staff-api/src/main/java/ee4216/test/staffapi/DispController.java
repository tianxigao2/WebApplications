/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.test.staffapi;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jane G
 */
@RestController
@RequestMapping(path="/api")
public class DispController implements CommandLineRunner{
    private org.slf4j.Logger logger = LoggerFactory.getLogger(DispController.class);
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(String... strings) throws Exception {}

    @GetMapping(path="/staffs", produces="application/json")
    public String displayStaffs() throws SQLException {
        ArrayList<String> jsonList = new ArrayList();
        String result = "[";
        jdbcTemplate.query(
                "SELECT * FROM STAFFLIST",
                (rs, rowNum) -> new Staff(
                        rs.getString("name"),
                        rs.getString("membership"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("profileURL"),
                        rs.getString("star"),
                        rs.getString("status")
                ).getJSON()
        ).forEach(s -> jsonList.add(s));
        for(int i = 0; i < jsonList.size() - 1; i ++){
            result += jsonList.get(i);
            result += ",";
        }
        result += jsonList.get(jsonList.size() - 1);
        result += "]";
        return result;
    }
    
    @PostMapping(path="/register", produces="application/json")
    public String registerAccount(@RequestBody User u) throws SQLException{
        System.out.println(u.getAccount());
        System.out.println(u.getPassword());
        if(u.storeToDB())
            return "[{\"msg\":\"Successful\"}]";
        else
            return "[{\"msg\":\"Error\"}]";
    }
    
    @PostMapping(path="/verify", produces="application/json")
    public String verifyAccount(@RequestBody User u) throws SQLException{
        if(u.checkMatchInDB())
            return "[{\"msg\":\"Successful\"}]";
        else
            return "[{\"msg\":\"Error\"}]";
    }
    
    @GetMapping(path="/delete/{name}", produces="application/json")
    public String deleteStaff(@PathVariable String name){
        jdbcTemplate.query("SELECT * FROM STAFFLIST WHERE name = \'"+name+"\'",
                (rs, rowNum) -> new Staff(
                        rs.getString("name"),
                        rs.getString("membership"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("profileURL"),
                        rs.getString("star"),
                        rs.getString("status")
                ))
                .forEach(staff -> {
            try {
                staff.deleteFromDB();
            } catch (SQLException ex) {
                Logger.getLogger(DispController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return "[{\"msg\":\"Successful\"}]";
    }
    
    @GetMapping(path="/edit/{name}/{star}/{status}", produces="application/json")
    public String editStarOrStatus(@PathVariable String name, @PathVariable String star, @PathVariable String status){
        logger.info("name = "+name + ", star = "+ star);
        jdbcTemplate.update("UPDATE staffList SET star = \'" + star + "\' WHERE name = \'" + name + "\'");
        return "[{\"name\":\"" + name+"\",\"star\":\"" + star+"\",\"status\":\"" + status+"\"}]";
    }
}

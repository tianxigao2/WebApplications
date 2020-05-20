/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.test.staffapi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jane G
 */
public class User{
    private String account;
    private String password;
    private Connection con;
    private Statement st;
    private org.slf4j.Logger logger = LoggerFactory.getLogger(User.class);

    public User(String acc, String pwd) throws SQLException{
        this.account = acc;
        this.password = pwd;
        
        String url = "jdbc:derby://localhost:1527/FinalPractice";
        String user = "JANEG";
        String password = "JANEG";
        con = DriverManager.getConnection(url, user, password);
        st = con.createStatement();
        
        ResultSet rs = con.getMetaData().getTables(null,null, "USERLIST", null);
        if(!rs.next()){
            int res = st.executeUpdate(""
                    + "CREATE TABLE userList("
                    + "account VARCHAR(8000) PRIMARY KEY,"
                    + "password VARCHAR(8000) NOT NULL"
                    + ")");
        }
    }
    
    public String getAccount(){
        return account;
    }
    
    public String getPassword(){
        return password;
    }

    public boolean storeToDB() throws SQLException{
        if(!checkExistenceInDB()){
            String sql = "INSERT INTO userList (account, password) VALUES ("
                    + "\'" + account + "\', "
                    + "\'" + password + "\')";
            logger.info(sql);
            int res = st.executeUpdate(sql); 
            return true;
        }else{
            return false;
        }
    }
    
    public boolean checkExistenceInDB() throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM userList WHERE account = \'" + account + "\'");
        if(rs.next())
            return true;
        return false;
    }
    
    public boolean checkMatchInDB() throws SQLException{
        ResultSet rs = st.executeQuery("SELECT * FROM userList "
                + "WHERE account = \'" + account + "\' AND password = \'" + password + "\'");
        if(rs.next())
            return true;
        return false;
    }
}

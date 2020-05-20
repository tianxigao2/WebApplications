/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ee4216.lab4.imdbapi;

/**
 *
 * @author Jane G
 */
public class Movie {
    private String id;
    private String name;
    private String year;
    private String rank;
    
    public Movie(String id, String name, String year, String rank){
        this.id = id;
        this.name = name;
        this.year = year;
        this.rank = rank;
    }
    
    public String getJson(){
        String result = "{\"id\":\"";
        result += this.id;
        result += "\", \"name\":\"";
        result += this.name;
        result += "\", \"rank\":\"";
        result += this.rank;
        result += "\", \"year\":\"";
        result += this.year;
        result += "\"},\n";

        return result;
    }
    
    public String getJsonEnd(){
        String result = "{\"id\":\"";
        result += this.id;
        result += "\", \"name\":\"";
        result += this.name;
        result += "\", \"rank\":\"";
        result += this.rank;
        result += "\", \"year\":\"";
        result += this.year;
        result += "\"}\n";

        return result;
    }
}

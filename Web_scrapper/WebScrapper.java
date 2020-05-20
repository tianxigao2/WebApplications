/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.ws.rs.core.HttpHeaders.USER_AGENT;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Jane G
 */
public class WebScrapper {
    private static String webAddress_p1;
    private static String webAddress_p2;
    private static ArrayList<ArrayList<String>> book_record_list;

    public WebScrapper() throws IOException, Exception{
        WebScrapper.book_record_list = new ArrayList();
        WebScrapper.webAddress_p1 = "https://www.amazon.com/gp/bestsellers/books/";
        WebScrapper.webAddress_p2 = "https://www.amazon.com/best-sellers-books-Amazon/zgbs/books/ref=zg_bs_pg_2?_encoding=UTF8&pg=2";

        // construct the ArrayList of book information
        connectAmazonRanking(webAddress_p1);
        connectAmazonRanking(webAddress_p2);
            // System.out.println(book_record_list.size());
            // System.out.println(book_record_list.get(0).get(0));
    }

    public ArrayList<ArrayList<String>> getBookRecordList(){
        return WebScrapper.book_record_list;
    }

    public static void connectAmazonRanking(String webAddress) throws IOException, Exception {
        SslUtils.ignoreSsl();
        
        Document doc = Jsoup.connect(webAddress).get();
        
        Elements book_records = doc.select("span.a-list-item>div");
        book_records.forEach((book) -> {
            ArrayList<String> record_for_one_book = new ArrayList();
            
            String ranking = book.getElementsByClass("zg-badge-text").first().text();
            record_for_one_book.add(ranking);
            
            String img_src = book.getElementsByTag("img").first().attr("src");
            record_for_one_book.add(img_src);
            
            String title = book.getElementsByClass("p13n-sc-truncate").first().text();
            record_for_one_book.add(title);
            
            String author_text = "";
            Elements authorList = book.getElementsByClass("a-link-child");
            if(!authorList.isEmpty()){
                Element author = authorList.first();
                author_text =  author.text();
            }
            record_for_one_book.add(author_text);
            
            // String rating = book.getElementsByClass("a-icon-alt").first().text();
            // record_for_one_book.add(rating);
            String rating = "";
            Elements rateList = book.getElementsByClass("a-icon-alt");
            if(!rateList.isEmpty()){
                rating = rateList.first().text();
            }
            record_for_one_book.add(rating);
            
            String price = book.getElementsByClass("p13n-sc-price").first().text();
            record_for_one_book.add(price);
            
            
            book_record_list.add(record_for_one_book);
        });
    }

//     public static void main(String[] args) {
//         WebScrapper w = new WebScrapper();
//     }
}

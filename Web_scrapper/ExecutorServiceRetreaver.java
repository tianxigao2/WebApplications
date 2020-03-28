/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 *
 * @author Jane G
 */

public class ExecutorServiceRetreaver{
    private int parallism;
    private ExecutorService pool;
    private ArrayList<String> listOfWebAddress;


    public ExecutorServiceRetreaver() {
        parallism = Runtime.getRuntime().availableProcessors(); // # of CPU kernels
        pool = Executors.newFixedThreadPool(parallism);
        listOfWebAddress = retreaveAllWebAddress();
        retreave();
        pool.shutdown();
    }

    public ArrayList<String> retreaveAllWebAddress(){
        ArrayList<String> listOfWebAddress = new ArrayList<String>();

        try {
            Document doc = Jsoup.connect("https://cityu.edu.hk/directories/people/academic").get();            
            Element LN_block = doc.getElementsByClass("block-facet-blockcityu-glossary-az-content-scholar-last-name").first();
            Elements lastName = LN_block.select(".facet-item");
            System.out.println("lastName.size = "+lastName.size());
            lastName.forEach((LN_address) -> {
                // System.out.println("LN_address.children.size = "+LN_address.childrenSize());
                if(LN_address.childrenSize() > 0)
                    listOfWebAddress.add(LN_address.children().first().attr("abs:href"));
            });
        }
        catch (IOException e) {
            Logger.getLogger(ExecutorServiceRetreaver.class.getName()).log(Level.SEVERE, null, e);
        }

        return listOfWebAddress;
    }

    public class PrintTask implements Runnable {
        // for each page, retreave and print the data
        private String webAddress;

        public PrintTask(String webAddress) {
            this.webAddress = webAddress;
        }

        public synchronized void infoProcessing(Element name){
            // before the relative email address printed, no other thread print information
            System.out.print(name.text() + ", ");
            
            Elements infoList = name.parent().nextElementSiblings();
            infoList.forEach((info) -> {
                if(info.is(".sub-left")){
                    Elements emails = info.getElementsByClass("email");
                    if(emails != null){
                        System.out.print(emails.text());
                    }
                }
            });
            
            System.out.println();
        }

        @Override
        public void run(){
            try {
                Document doc = Jsoup.connect(webAddress).get();
    
                Elements names = doc.select("div.name > div.en");
                
                for(Element name: names){
                    infoProcessing(name);
                }
//                System.out.println("this thread should be finished---------------------------------------------------------------------------------------------------------------");
            }
            catch (IOException e) {
                Logger.getLogger(ExecutorServiceRetreaver.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void retreave(){
        // separate pages into n groups and assign a thread for each
        int pages = this.listOfWebAddress.size();

        System.out.println("There are "+pages+" pages in total!");
        System.out.println("Launching "+parallism+" threads ...");

        for (int i = 0; i < pages; i++) {
            String webAddress = listOfWebAddress.get(i);
            pool.execute(new PrintTask(webAddress));
        }
    }

    public static void main(String[] args){
        ExecutorServiceRetreaver e = new ExecutorServiceRetreaver();
    }
}

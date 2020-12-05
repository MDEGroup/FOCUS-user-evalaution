package jsoup_example.jsoup_example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {

 /*
  * Parsing a Ansa news
  */
 public static void main(String[] args) throws IOException {
  
  System.out.println("Number of all news belonging to 'calciomercato' category: "+getCalciomercatoNews());
  ArrayList<String> arr=new ArrayList<String>();
  arr=(ArrayList<String>) getWinningAwayTeams();
  System.out.println("List of winnig away teams:\n"+arr.toString());
  System.out.println("Day with greater temperature difference: "+getDayWithBiggerTemperatureDifference());
 }
 

 /*
  * Scrape a simple mobile website page and count all news belong to "calciomercato" category
  * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
  */
 public static int getCalciomercatoNews() throws IOException {
  String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
  Document document = Jsoup.connect(url).get();
  Elements images = document.select(".list-item");
  images.parents();
  int count = 0;
  
  Elements span = document.getElementsByClass("small date upper list-date-data");
  Iterator<Element> iterator = span.iterator();
  
  while(iterator.hasNext()) {
   Element s = iterator.next();
   if(s.toString().contains("calciomercato"))count++;
  }
  
  return count; 
 }

 /*
  * Scrape livescore web site: extract the list of
  * away teams that won the match and print them to the console.
  * Use "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)" as user agent string.
  * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/livescore.html
  */
 public static List<String> getWinningAwayTeams() throws IOException {
  String url = "https://mdegroup.github.io/FOCUS-Appendix/livescore.html";
  Document document = Jsoup.connect(url)/*.userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)")*/.get();
  Elements scores = document.getElementsByClass("sco");
  scores.parents();
  
  Elements awayTeams = document.getElementsByClass("ply name");
  //List<String> result = new ArrayList();

  int i=0;
  List<String> result = new ArrayList<String>();
  Iterator<Element> iterator = scores.iterator();
  
  while(iterator.hasNext()) {
   try {
    Element s = iterator.next();
    int homeScore,awayScore; //punteggio casa/ospiti
    homeScore=Integer.parseInt(s.getElementsByClass("hom").text());
    awayScore=Integer.parseInt(s.getElementsByClass("awy").text());
    
    if(homeScore < awayScore) {
     Element awy = awayTeams.get(i);
     //aggiungo alla lista la squadra che ha vinto
     result.add(awy.text());
    }
    }catch (NumberFormatException e) {
     // TODO: handle exception
    }
   i++;
  }
  
  return result;
 }
 
 /*
  * Scrape a weather web site: returns the name of the day (i.e., "Martedì 29")
  * that has the bigger temperature difference.
  * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/meteo.html
  */
 public static String getDayWithBiggerTemperatureDifference() throws IOException {
  String url = "https://mdegroup.github.io/FOCUS-Appendix/meteo.html";
  Document document = Jsoup.connect(url).get();
  Elements listItem = document.getElementsByClass("sc-kGXeez dBcWUR");
  Elements weathers = document.select("span.dayDate");
  String name = "";
  
  Elements weathers = document.select(".icons .temperature");
  Elements DaysName = document.select(".dayDate");

  return(name);

  int indexDays = 0;
  int max=0;
  String dayMax = null;
  for(int i = 0; i< weathers.size(); i+=2) {
   int tempMax = Integer.parseInt(weathers.get(i+1).text().substring(0, 2));
   int tempMin = Integer.parseInt(weathers.get(i).text().substring(0, 2));
   int diff = tempMax-tempMin;
   if( diff > max ) {
    max = diff;
    dayMax = DaysName.get(indexDays).text();
   }
     indexDays++; 
  }

  return(dayMax);
 }

}


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
		System.out.println("Elements containing calciomercato: "+getCalciomercatoNews());
		List<String> r = getWinningAwayTeams();
		System.out.println("List of winnig away teams:\n"+r+"\n");
		System.out.println(getDayWithBiggerTemperatureDifference());
	
	}
	 

	/*
	 * Scrape a simple mobile website page and count all news belong to "calciomercato" category
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
	 */
	public static int getCalciomercatoNews() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
		Document document = Jsoup.connect(url).get();
		Elements images = document.select(".list-item");
		Iterator<Element> iterator = images.iterator();
		images.parents();
		int count = 0;
		while(iterator.hasNext()) {
			Element el = iterator.next();
			if(el.toString().contains("calciomercato"))
				count++;
			
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
		Elements teamNameAway = document.getElementsByClass("ply Name");
		scores.parents();
		
		Iterator<Element> iterator = scores.iterator();
		
		int i = 0 ;
		
		List<String> result = new ArrayList();
		
		while(iterator.hasNext()){
			Element e = iterator.next();
			Element teamHome = e.getElementsByClass("hom").first();
			Element teamAway = e.getElementsByClass("awy").first();
			try {
				if(teamHome != null && teamAway != null) {
					int home = Integer.parseInt(teamHome.ownText());
					int away  = Integer.parseInt(teamAway.ownText());
					Elements tl2 = teamNameAway.get(i).getElementsByTag("span");
					if(away > home) {
						result.add(tl2.text());
						result.add("\n");
					}
					
				}
			}catch(NumberFormatException r) {
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
		Elements weathers = document.select(".icons .temperature");
		Elements DaysName = document.select(".dayDate");
		
		int indexDays = 0;
		int max=0;
		String Daymax = null;
		for(int i = 0; i< weathers.size(); i+=2) {
			int top = Integer.parseInt(weathers.get(i+1).text().substring(0, 2));
			int down = Integer.parseInt(weathers.get(i).text().substring(0, 2));
			int diff = top-down;
			if( diff > max ) {
				max = diff;
				Daymax = DaysName.get(indexDays).text();
			}
	   indexDays++;	
		}
		
		return(Daymax);
	}
}

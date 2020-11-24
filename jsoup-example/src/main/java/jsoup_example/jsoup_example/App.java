package jsoup_example.jsoup_example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	}
	
	/*
	 * Scrape a simple mobile website page and count all news belong to "calciomercato" category
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
	 */
	public static int getCalciomercatoNews() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
		Document document = Jsoup.connect(url).get();
		 int count = 0;
		Elements images = document.select(".list-item  .small.date.upper.list-date-data");

		for(Element el : images) 
			if(el.text().contains("Calciomercato")) count++;			

		
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
		Elements teamsAway = document.getElementsByClass("ply name");
				
		List<String> result = new ArrayList();

		int indextemas = 0;
		for(Element el: scores) {
			Element away = el.getElementsByClass("awy").first();
			Element home = el.getElementsByClass("hom").first();
			try {
				if(away != null && home!= null) {
					
				
				int awayNumber = Integer.parseInt(away.text());
				
				int homeNumber = Integer.parseInt(home.text());
				Elements el2 =teamsAway.get(indextemas).getElementsByTag("span");

				if(awayNumber > homeNumber) 
					result.add(el2.text());
				
				}
			
			}catch (NumberFormatException e) {
				// TODO: handle exception
			}

			indextemas++;
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

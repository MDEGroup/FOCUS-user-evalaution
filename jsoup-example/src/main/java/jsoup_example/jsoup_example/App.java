package jsoup_example.jsoup_example;

import java.io.IOException;
import java.util.ArrayList;
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
		System.out.println("Number of all news belonging to 'calciomercato' category: " + getCalciomercatoNews());
		
		System.out.println("\n" +"---------------------------------------------------------------------------------------------" + "\n");

		System.out.println("List of all teams that won the match: ");
		for(String team: getWinningAwayTeams()) System.out.println(team);
		
		System.out.println("\n" +"---------------------------------------------------------------------------------------------" + "\n");

		System.out.println("Name of the day that has the bigger temperature difference: " + getDayWithBiggerTemperatureDifference());
	}
	

	/*
	 * Scrape a simple mobile website page and count all news belong to "calciomercato" category
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
	 */
	public static int getCalciomercatoNews() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
		Document document = Jsoup.connect(url).get();
		Elements images = document.select(".list-item");
		
		int count = 0;
		for(Element e: images) { 
			if(e.attr("href").toString().contains("calciomercato")) 			
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
		
		List<String> result = new ArrayList();
		
		for(Element e: scores) { 
			String hom = e.select("span[class=hom]").text();
			String awy = e.select("span[class=awy]").text();
			
			if((hom.matches("[0-9]+") && awy.matches("[0-9]+")) && awy.compareTo(hom)>0) {
				String winning = e.parent().select("div[class=\"ply name\"]").text();
				if(!result.contains(winning))
					result.add(winning);
			}		
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
		Elements weathers = document.select("span.dayDate");
		
		String name = "";
		int maxTempDiff = 0;
				
		for(Element e: weathers) { 
			
			Element parent = e.parent().parent().parent();
			Elements temp = parent.select("span.temperature");
			
			int min = Integer.parseInt(temp.get(0).text().split("°")[0]);
			int max = Integer.parseInt(temp.get(1).text().split("°")[0]);
			
			int diff = max - min;
			
			if(diff>maxTempDiff) {
				maxTempDiff = diff;
				name = e.text();
			}			
		}	
		
		return(name);
	}
	

}

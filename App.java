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
		System.out.println("\nNumbers of calciomercato news :"+getCalciomercatoNews());
		System.out.println("Contenuto array :"+getWinningAwayTeams());
		System.out.println("Contenuto array :"+getDayWithBiggerTemperatureDifference());
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
	for(Element e : images) {
		String href = e.attr("href");
		if(href.contains("/calciomercato/")) {
			count++;
		}
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
		Elements matches = document.select("a[href].match-row div.ply.name span");
		Elements scores = document.select("a[href].match-row .sco .hom, a[href].match-row .sco .awy");
		scores.parents();
		
		List<String> result = new ArrayList();
for(int i=0; i<scores.size(); i=i+2) {
	String home_score = scores.get(i).text();
	String away_score = scores.get(i+1).text();
	String home=matches.get(i).text();
	String away=matches.get(i+1).text();
	//System.out.println("\n"+home_score);
	
	if(!home_score.equals("?")) {
	if(Integer.parseInt(home_score)< Integer.parseInt(away_score)) {
		result.add(away);
	}
	if(Integer.parseInt(home_score)> Integer.parseInt(away_score)) {
		result.add(home);
	}
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
		Elements weathers = document.select("a[href]:has(span.dayDate)");
		String name = "";
		int max_diff = 0;
		for(Element e: weathers) {
			Elements temperatures = e.getElementsByClass("temperature");
			
			int min = Integer.parseInt(temperatures.get(0).text().replace("°", ""));
			int max=Integer.parseInt(temperatures.get(1).text().replace("°", ""));
			int differenza =  Math.abs(max - min);
			if(max_diff<differenza) {
				max_diff=differenza;
				name= e.getElementsByClass("dayDate").text();
			}
		
		}
		

		return(name);
	}
	

}

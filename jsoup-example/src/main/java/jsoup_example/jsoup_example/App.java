package jsoup_example.jsoup_example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.chainsaw.Main;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
public class App {

	private static final String String = null;

	/*
	 * Parsing a Ansa news
	 */
	public static void main(String[] args) throws IOException {
		
		System.out.println("elements contains "+getCalciomercatoNews());
		List<String> r = getWinningAwayTeams();
		System.out.println("List of winnig away teams:\n"+r);
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
		
	
		Iterator<Element> iterator=images.iterator();
		int count = 0;
		while(iterator.hasNext()) {
			Element e = iterator.next();
			if(e.toString().contains("CALCIOMERCATO"));
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
		Elements awayteam = document.getElementsByClass("ply name");
		scores.parents();
		Iterator<Element> iterator = scores.iterator();
		int c = 0;
		
		List<String> result = new ArrayList();
		while(iterator.hasNext()) {
			Element e = iterator.next(); // sposto l'oggetto su un elemento di tipo element
			Element away = e.getElementsByClass("awy").first();
			Element home = e.getElementsByClass("hom").first();
			//System.out.println(away.ownText());
			String s;
			int awayS = 0;
			int homeS = 0;
			if(away!=null ) {
				if((s = away.ownText())!= null) {
					try {
						awayS = Integer.parseInt(s);
					}catch(NumberFormatException err) {
						awayS = -1;
					}
				}
				if((s = home.ownText())!= null) {
					try {
						homeS = Integer.parseInt(s);
					}catch(NumberFormatException err) {
						homeS = -1;
					}
				}
				if(homeS != -1 && awayS != -1) {
					if(awayS > homeS) {
						if(awayteam.get(c)!=null)result.add(awayteam.get(c).children().first().ownText());
					}
				}
				
			}
			c++;
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
		Elements temp = document.select("span.temperature");
		int c = 0;
		Iterator<Element> iterator = temp.iterator();
		int previous_max = 0;
		int max_pos = 0;
		while(iterator.hasNext()) {
			Element temp1 = iterator.next();
			Element temp2 = iterator.next();
			String temp1S = temp1.ownText().replace("°", "");
			String temp2S = temp2.ownText().replace("°", "");
			int t1 = 0;
			int t2 = 0;
			try {
				t1 = Integer.parseInt(temp1S);
				t2 = Integer.parseInt(temp2S);
			}catch(NumberFormatException err) {};
			if(c == 0) {
				max_pos = 0;
				previous_max = t2-t1;
			}else {
				if((t2-t1) > previous_max) {
					max_pos = c;
					previous_max = t2-t1;
				}
			}
			c++;
		}
		String name = weathers.get(max_pos).ownText();
		

		return(name);
	}
	

}

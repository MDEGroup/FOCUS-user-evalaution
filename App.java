package jsoup_example.jsoup_example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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
		
		App uno=new App();
		System.out.println("Numero di notizie sul calciomercato : "+uno.getCalciomercatoNews());
		System.out.println("Squadre che hanno vinto in trasferta : " +uno.getWinningAwayTeams());
		System.out.println("Giornata con la maggior differenza di T° : "+uno.getDayWithBiggerTemperatureDifference());
		
		
	}
		
		
	/*
	 * Scrape a simple mobile website page and count all news belong to "calciomercato" category
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
	 */
	public static int getCalciomercatoNews() throws IOException {
		
		int count = 0;
		
		String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
		Document document = Jsoup.connect(url).userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)").get();
		Elements images = document.select(".list-item");
		images.parents();
		
	
		for(Element image : images) {
			if (image.toString().contains("calciomercato")) {
		count++;}
			 
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
		
		List<String> result = new ArrayList();


		for (Element sc: scores) {
	 

		        String casa = (sc.getElementsByClass("hom").html());
		        String trasferta = (sc.getElementsByClass("awy").html());
		        
		        if (!casa.equals("?") &&  !trasferta.equals("?")&& !casa.equals("") &&  !trasferta.equals("")) {
		        
		        	int nc= Integer.parseInt(casa);
		        	int nt = Integer.parseInt(trasferta);
		        	
		        	if (nc<nt) {
		        		
		        		String team= sc.nextElementSibling().select("span").first().html();
		        		result.add(team);
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
		Elements weathers = document.select("span.dayDate");
		String name = "";
		
		List<Integer> list_T = new ArrayList();
	 
	
		for(Element weather : weathers) {
			
			String min=weather.parents().select("span.temperature").first().html().replace("°", "");
			String max= weather.parents().select("span.temperature").first().nextElementSibling().html().replace("°", "");
			

        	int minT= Integer.parseInt(min);
        	int maxT = Integer.parseInt(max);
			
        	int difference = maxT-minT;
        	
			list_T.add(difference);
			
			int indix;
			for(indix=0;indix<list_T.size();indix++) {
					
				if(difference == Collections.max(list_T)) {;
				String day = weather.html();
				name=day;
				}	 
			}
			
		}
		return(name);
	}
	
	
	

}

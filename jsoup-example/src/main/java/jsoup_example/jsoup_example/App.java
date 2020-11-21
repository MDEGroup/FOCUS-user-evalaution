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

	static final String category = "calciomercato";
	/*
	 * Parsing a Ansa news
	 */
	public static void main(String[] args) throws IOException {
//		int newsCalcioMercato = getCalciomercatoNews();
//		System.out.println("News di mercato: " + newsCalcioMercato);
//		 
//		List<String> winners = getWinningAwayTeams();
//		System.out.println("Squadre vincitrici:" + winners);
		
		String day = getDayWithBiggerTemperatureDifference();
		System.out.println(day);
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
		
		for(Element image: images) {
			// Get https url
			String href = image.attr("href");
			
			// count++ if category = "calciomercato"
			if(href.contains(category)) {
				count++;
			}
		}
		
		//int count = 0;
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
		
		for(Element score: scores) {
			
			try {
				// Get Home Team Score
				String homeScoreString = score.getElementsByClass("hom").first().ownText();
				int homeScore = Integer.parseInt(homeScoreString);
								
				// Get Away Team Score
				String awayScoreString = score.getElementsByClass("awy").first().ownText();
				int awayScore = Integer.parseInt(awayScoreString);
				
				// Aggiungo alla lista la squadra che ha segnato più goals
				if(homeScore > awayScore) {
					result.add(score.parent().getElementsByClass("ply tright name").first().children().first().ownText());
				}
				
				else if(homeScore < awayScore) {
					result.add(score.parent().getElementsByClass("ply name").first().children().first().ownText());
				}
								
			}
			catch(NullPointerException ex) {
//				System.err.println("Elemento scartato:\n" + score);
			}
			catch(NumberFormatException ex) {
//				System.err.println(ex.getMessage());
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
		int maxDifference = 0;
		int difference = 0;
		
		for(Element weather: weathers) {
			
			try {
				// Get minTemperature
				String minTemperatureString = (weather.parents().get(2).getElementsByClass("temperature").get(0).ownText().replace("°", ""));
				int minTemperature = Integer.parseInt(minTemperatureString);

				// Get maxTemperature
				String maxTemperatureString = (weather.parents().get(2).getElementsByClass("temperature").get(1).ownText().replace("°", ""));
				int maxTemperature = Integer.parseInt(maxTemperatureString);
				
				// Calcolo differenza
				difference = maxTemperature - minTemperature;
				
				// Setto una nuova differenza massima se ne riscontro una più alta della precedente
				if(difference > maxDifference) {
					maxDifference = difference;
					name = weather.ownText();
				}
				
			}
			catch(NumberFormatException ex) {
				
			}
		}
				
		return(name);
	}
	

}

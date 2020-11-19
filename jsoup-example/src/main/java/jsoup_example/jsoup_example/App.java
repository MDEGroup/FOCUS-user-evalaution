package jsoup_example.jsoup_example;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

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
		System.out.println("Numero di news appartenenti alla categoria \"Calciomercato\": " + getCalciomercatoNews());
		System.out.println();
		
		System.out.println("Lista di tutte le squadre che hanno vinto un match in trasferta:");
		for (String player : getWinningAwayTeams()) {
			System.out.println("	" + player);
		}
		System.out.println();

		System.out.println("Giorno con la maggior differenza tra le temperature massima e minima: " + getDayWithBiggerTemperatureDifference());
		System.out.println();
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
		Iterator<Element> elementsIterator = images.listIterator();
		while (elementsIterator.hasNext()) {
			if (elementsIterator.next().attr("href").contains("calciomercato")) count ++;
		}
		return count;	
	}

	/*
	 * Scrape livescore web site: extract the list of
	 * away teams that won the match and print them to the console.
	 * Use "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)" as user agent string.
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/livescore.html
	 */
	public static List<String> getWinningAwayTeams() throws IOException{
		String url = "https://mdegroup.github.io/FOCUS-Appendix/livescore.html";
		Document document = Jsoup.connect(url).get();
		Elements scores = document.getElementsByClass("sco");
		Elements playersAway = scores.next();

		List<String> results = new ArrayList<String>();
		for (int i = 0; i < scores.size(); i++) {
			try {
				int scoreAway = Integer.parseInt(scores.get(i).getElementsByClass("awy").text());
				int scoreHome = Integer.parseInt(scores.get(i).getElementsByClass("hom").text());

				if(scoreAway > scoreHome) {
					String playerWinner = playersAway.get(i).getElementsByClass("ply name").text();
					
					if(!results.contains(playerWinner)) results.add(playerWinner);
				} 


			}catch(NumberFormatException e) {}
		}

		return results;
	}
	
	/*
	 * Scrape a weather web site: returns the name of the day (i.e., "Martedì 29")
	 * that has the bigger temperature difference.
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/meteo.html
	 */
	public static String getDayWithBiggerTemperatureDifference() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/meteo.html";
		Document document = Jsoup.connect(url).get();
		Elements days = document.getElementsByClass("dayDate");
		Elements minAndMaxTemperatures = document.getElementsByClass("temperature");
		
		String dayWithBiggerTemperatureDifference = "";
		int biggerTemperatureDifference = 0;
		for (int i = 0; i < days.size(); i++) {
			int minTemp = Integer.parseInt(minAndMaxTemperatures.get(i*2).text().replace("°", ""));
			int maxTemp = Integer.parseInt(minAndMaxTemperatures.get((i*2) + 1).text().replace("°", ""));
			int currentDifference = maxTemp - minTemp;

			if(currentDifference > biggerTemperatureDifference) {
				biggerTemperatureDifference = currentDifference;
				dayWithBiggerTemperatureDifference = days.get(i).text();
			}
		}

		return dayWithBiggerTemperatureDifference;
	}
	

}

package jsoup_example.jsoup_example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
		int news = getCalciomercatoNews();
		if(news > 0)
			System.out.println("There are " + news + " calciomercato news!\n");
		else
			System.out.println("There is no calciomercato news\n");
		
		
		Map<String, String> winning_away =  getWinningAwayTeams();
		System.out.println("There are " + winning_away.get("not_played") + " matches still to be played.");
		winning_away.remove("not_played");
		System.out.println("All away teams that have won a match are:");
		for(String away: winning_away.keySet())
			System.out.println("\t" + winning_away.get(away) + "\t" + away);
		
		
		System.out.print("\nThe day with the highest temperature gradient is " + getDayWithBiggerTemperatureDifference());
	}
	

	/*
	 * Scrape a simple mobile website page and count all news belong to "calciomercato" category
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
	 */
	public static int getCalciomercatoNews() throws IOException {
		int count = 0;
		String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
		Document document = Jsoup.connect(url).get();
		/*
		Elements images = document.select(".list-item");
		images.parents();

		for(Element e: images) {
			String href = e.attr("href");
			if(href.contains("/calciomercato/"))
				count++;
		}
		*/
		count = document.select("span.small.date.upper.list-date-data:contains(Calciomercato)").size();
		
		return count;	
	}

	/*
	 * Scrape livescore web site: extract the list of
	 * away teams that won the match and print them to the console.
	 * Use "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)" as user agent string.
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/livescore.html
	 */
	public static Map<String, String> getWinningAwayTeams() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/livescore.html";
		Document document = Jsoup.connect(url)/*.userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)")*/.get();
		/*Elements scores = document.getElementsByClass("sco");
		scores.parents();
		
		List<String> result = new ArrayList();*/
		
		int not_played = 0;
		Map<String, String> results = new HashMap<String, String>(); 
		
		Elements matches = document.select("a[href].match-row div.ply.name span");
		Elements scores = document.select("a[href].match-row .sco .hom, a[href].match-row .sco .awy");
		
		for(int i=0; i<scores.size(); i=i+2) {
			//String home = matches.get(i).text();
			String home_score = scores.get(i).text();
			String away = matches.get(i+1).text();
			String away_score = scores.get(i+1).text();
			
			//System.out.println(home + "\t\t\t" + home_score + "\t\t\t" + away_score + "\t\t\t" + away);
			if(home_score.equals("?"))
				not_played++;
			else {
				if(Integer.parseInt(home_score) < Integer.parseInt(away_score))
					results.put(away, home_score+"-"+away_score);
			}
		}
		//System.out.println("\nMatches: " +matches.size() + "\t\tScores: " + scores.size()+"\n");
				
		results.put("not_played", Integer.toString(not_played));
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
		Elements weathers = document.select("a[href]:has(span.dayDate)");
		
		String name = "";
		int max_diff = 0;
		for(Element e: weathers) {
			Elements temperatures = e.getElementsByClass("temperature");
			//System.out.println(temperatures+"\n\n");
			int min_temp = Integer.parseInt(temperatures.get(0).text().replace("°", ""));
			int max_temp = Integer.parseInt(temperatures.get(1).text().replace("°", ""));
			
			int diff = Math.abs(max_temp - min_temp);
			if(max_diff < diff) {
				max_diff = diff;
				name = e.getElementsByClass("dayDate").text();
			}
		}
		return(name);
	}
	

}

package jsoup_example.jsoup_example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
		System.out.println("Calciomercato");
		System.out.println(getCalciomercatoNews());
		System.out.println("\n");
		System.out.println("Away Winners");
		List<String> awayWinner = getWinningAwayTeams();
		for(String s : awayWinner) {
			System.out.println(s);
		}
		System.out.println("\n");
		System.out.println("Day With Bigger Temperature Difference");
		System.out.println(getDayWithBiggerTemperatureDifference());
	}
	

	/*
	 * Scrape a simple mobile website page and count all news belong to "calciomercato" category
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
	 */
	public static int getCalciomercatoNews() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
		final List<String> news = new ArrayList<String>();
		Document document = Jsoup.connect(url).get();
		Elements titles = document.select(".list-titolo");
		titles.forEach(new Consumer<Element>() {
			@Override
			public void accept(Element title) {
			    Element el = title.children().get(0);
			    if(el.text().matches("(?i)[0-9][0-9]:[0-9][0-9] CALCIOMERCATO")) news.add("found");
			}
		});
		int count = news.size();
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
		Document document = Jsoup.connect(url).userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)").get();
		final List<String> result = new ArrayList<String>();
		Elements scores = document.getElementsByClass("sco");
		scores.forEach(new Consumer<Element>() {
			@Override
			public void accept(Element s) {
				Elements els = s.children();
				String home = els.select(".hom").text();
				String away = els.select(".awy").text();
				int homeScore = 0, awayScore = 0;
				if(home != null && home.matches("[-+]?\\d*\\.?\\d+")) homeScore = Integer.parseInt(home);
				if(away != null && away.matches("[-+]?\\d*\\.?\\d+")) awayScore = Integer.parseInt(away);
				if(homeScore < awayScore) {
					result.add(s.nextElementSibling().children().get(0).text());
				}
			}
		});
		return result;
	}
	
	/*
	 * Scrape a weather web site: returns the name of the day (i.e., "Martedì 29")
	 * that has the bigger temperature difference.
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/meteo.html
	 */
	public static String getDayWithBiggerTemperatureDifference() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/meteo.html";
		String name = "";
		Document document = Jsoup.connect(url).get();
		Elements uls = document.getElementsByTag("ul");
		Iterator<Element> ituls = uls.iterator();
		while(ituls.hasNext()) {
			Element ul = ituls.next();
			if(ul.select("span.dayDate").size() > 0) {
				Elements lis = ul.children();
				Iterator<Element> itils = lis.iterator();
				int maxDiff = 0;
				while(itils.hasNext()) {
					Element li = itils.next();
					int minTemp = 0, maxTemp = 0;
					String min = li.select(".temperature").get(0).text().substring(0, 2);
					String max = li.select(".temperature").get(1).text().substring(0, 2);
					if(min != null && min.matches("[-+]?\\d*\\.?\\d+") && max != null && max.matches("[-+]?\\d*\\.?\\d+")) {
						minTemp = Integer.parseInt(min);
						maxTemp = Integer.parseInt(max);
						int diff = maxTemp - minTemp;
						if(diff > maxDiff) {
							maxDiff = diff;
							name = li.select("span.dayDate").text();
						}
					}	
				}
			}
		}
		return(name);
	}
	

}

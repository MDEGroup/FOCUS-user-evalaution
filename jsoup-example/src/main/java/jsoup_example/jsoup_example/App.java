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

		System.out.println(getCalciomercatoNews());
		System.out.println(getWinningAwayTeams());
		System.out.println(getDayWithBiggerTemperatureDifference());
	}

	/*
	 * Scrape a simple mobile website page and count all news belong to
	 * "calciomercato" category Extract the required information from
	 * https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
	 */
	public static int getCalciomercatoNews() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
		Document document = Jsoup.connect(url).get();
		Elements images = document.select(".list-item");

		int count = 0;

		for (Element i : images) {
			if (i.attr("href").toString().contains("calciomercato"))
				count++;
		}

		return count;
	}

	/*
	 * Scrape livescore web site: extract the list of away teams that won the match
	 * and print them to the console. Use
	 * "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)" as user agent string.
	 * Extract the required information from
	 * https://mdegroup.github.io/FOCUS-Appendix/livescore.html
	 */
	public static List<String> getWinningAwayTeams() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/livescore.html";
		Document document = Jsoup.connect(url)
				/* .userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)") */.get();
		Elements scores = document.getElementsByClass("sco");
		int count = 0;
		Elements awayTeams = document.getElementsByClass("ply name");
		List<String> result = new ArrayList<>();

		Iterator<Element> iterator = scores.iterator();
		while (iterator.hasNext()) {
			try {
				Element s = iterator.next();

				String home = s.getElementsByClass("hom").text();
				String away = s.getElementsByClass("awy").text();

				int homeN = Integer.parseInt(home);
				int awayN = Integer.parseInt(away);

				if (awayN > homeN) {
					Element awayE = awayTeams.get(count);
					result.add(awayE.text());
				}
			} catch (NumberFormatException e) {
				// nothing
			}
			count++;
		}
		return result;
	}

	/*
	 * Scrape a weather web site: returns the name of the day (i.e., "Martedì 29")
	 * that has the bigger temperature difference. Extract the required information
	 * from https://mdegroup.github.io/FOCUS-Appendix/meteo.html
	 */
	public static String getDayWithBiggerTemperatureDifference() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/meteo.html";
		Document document = Jsoup.connect(url).get();
		Elements days = document.select(".dayDate");
		Elements weathers = document.select(".icons .temperature");

		int numMax = 0;
		String nameDay = null;
		int count = 0;
		for (int i = 0; i < weathers.size(); i += 2) {
			
			String max= weathers.get(i + 1).text().split("°")[0];
			String min= weathers.get(i).text().split("°")[0];
			
			int maxN = Integer.parseInt(max);
			int minN = Integer.parseInt(min);
			int dif = maxN - minN;
			
			if (dif > numMax) {
				numMax = dif;
				nameDay = days.get(count).text();
			}
			count++;
		}

		return nameDay;
	}

}



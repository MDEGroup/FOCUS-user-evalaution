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
		ArrayList<String> arr=new ArrayList<String>();
		arr=(ArrayList<String>) getWinningAwayTeams();
		System.out.println(arr.toString());
		System.out.println(getDayWithBiggerTemperatureDifference());
	}
	

	/*
	 * Scrape a simple mobile website page and count all news belong to "calciomercato" category
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
	 */
	public static int getCalciomercatoNews() throws IOException {
		String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
		Document document = Jsoup.connect(url).get();

		int count=0;
		Elements span = document.getElementsByClass("small date upper list-date-data");
		Iterator<Element> iterator = span.iterator();
		while(iterator.hasNext()) {
			Element s = iterator.next();
			if(s.toString().contains("Calciomercato"))count++;
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
		Elements awayTeams = document.getElementsByClass("ply name");
		
		
		int i=0;
		List<String> result = new ArrayList<String>();
		Iterator<Element> iterator = scores.iterator();
		while(iterator.hasNext()) {
			try {
			Element s = iterator.next();
			int h,a;
			h=Integer.parseInt(s.getElementsByClass("hom").text());
			a=Integer.parseInt(s.getElementsByClass("awy").text());
			if(h < a) {
				Element awy = awayTeams.get(i);
				result.add(awy.text());
			}
			}catch (NumberFormatException e) {}
			i++;
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
		Elements listItem = document.getElementsByClass("sc-kGXeez dBcWUR");
		Elements weathers = document.select("span.dayDate");
		
		Iterator<Element> iterator = listItem.iterator();
		int index=0, max=0, i=0;
		while(iterator.hasNext()) {
			Element s = iterator.next();
			String t1=s.getElementsByClass("temperature").text();
			int num1=Integer.parseInt((String) t1.subSequence(4, 6));
			int num2=Integer.parseInt((String) t1.subSequence(0, 2));
			int diff=num1-num2;
			
			if (max<(diff)) {
				index=i;
				max=diff;
			} 
			i++;
		}
		String name = weathers.get(index).text();
		return(name);
	}
}

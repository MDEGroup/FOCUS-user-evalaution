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
public class App 
{

	/*
	 * Parsing a Ansa news
	 */
	
	/* Dichiaro la costante calciomercato */
	static final String CATEGORIA = "calciomercato";
	/* Dichiaro le costanti per ospiti e casa */
	static final String OSPITE = "awy";
	static final String CASA = "hom";
	static final String NOMEOSPITE = "ply name";
	static final String NOMECASA = "ply tright name";
	/* Dichiaro la costante temperatura */
	static final String TEMPERATURA = "temperature";
	
	public static void main(String[] args) throws IOException
	{
		/* Richiamo il metodo getCalciomercatoNews */
		int countCategoria = getCalciomercatoNews();
		System.out.println("Esercizio 1.1");
		System.out.println("Ci sono: "+countCategoria+ " news nella categoria calciomercato");
		System.out.println("-------------");
		System.out.println("Esercizio 1.2");
		List<String> squadreVincitrici = getWinningAwayTeams();
		System.out.println("Lista squadre vincitrici: "+squadreVincitrici);
		System.out.println("-------------");
		System.out.println("Esercizio 1.3");
		String giorno = getDayWithBiggerTemperatureDifference();
		System.out.println("Il giorno in cui si è verificata la massima differenza di temperatura tra max e min è: "+giorno);
		System.out.println("-------------");
	}
	
	/*
	 * Scrape a simple mobile website page and count all news belong to "calciomercato" category
	 * Extract the required information from https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm
	 */
	public static int getCalciomercatoNews() throws IOException 
	{
		String url = "https://mdegroup.github.io/FOCUS-Appendix/tuttojuve.htm";
		Document document = Jsoup.connect(url).get();
		Elements images = document.select(".list-item");
		images.parents();
		int count = 0;
		/* Ciclo con il for generalizzato ogni elemento della lista */
		for(Element elementoLista : images)
		{
			/* Recupero il link */
			String link = elementoLista.attr("href");
			/* Se il link contiene la parola calciomercato allora incrementa il contatore perchè stiamo nella categoria calciomercato */
			if(link.contains(CATEGORIA))
			{
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
	public static List<String> getWinningAwayTeams() throws IOException 
	{
		String url = "https://mdegroup.github.io/FOCUS-Appendix/livescore.html";
		Document document = Jsoup.connect(url)/*.userAgent("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)")*/.get();
		Elements scores = document.getElementsByClass("sco");
		
		List<String> result = new ArrayList<String>();
		/* Ciclo con il for generalizzato ogni elemento della lista */
		for(Element risultato : scores) 
		{
			/* Recuperiamo il valore degli SPAN */
			try
			{
				/* Recupero il risultato della casa */
				String sRisultatoCasa = risultato.getElementsByClass(CASA).first().ownText();
				/* Recupero il risultato degli ospiti */
				String sRisultatoOpsite = risultato.getElementsByClass(OSPITE).first().ownText();
				/* Converto il risultato casa*/
				int risultatoCasa = Integer.parseInt(sRisultatoCasa);
				/* Converto il risultato casa*/
				int risultatoOpsite = Integer.parseInt(sRisultatoOpsite);
				/* Controlla i risultati, escludendo il pareggio */
				if(risultatoCasa > risultatoOpsite)
				{
					result.add(risultato.parent().getElementsByClass(NOMECASA).first().children().first().ownText());
				}
				else if(risultatoCasa < risultatoOpsite)
				{
					result.add(risultato.parent().getElementsByClass(NOMEOSPITE).first().children().first().ownText());
				}
				else
				{
					
					/* Comentato per favorire la leggibilità
					 * System.err.println("Partita finita in pareggio");
					 */
				}
				
			}
			/* Gestione eccezioni */
			catch(NullPointerException erroreNullo)
			{
				/* Comentato per favorire la leggibilità
				 * System.err.println("Elemento scarcato perchè non contiene risultati");
				 */
			}
			catch(NumberFormatException erroreConversione)
			{
				/* Comentato per favorire la leggibilitàs
				 * System.err.println("Elemento scarcato perchè non contiene un numero valido");
				 */
			}
			
			
			//String risultatoOspite = risultato.getElementsByClass(CASA).first().ownText();
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
		int differenzaMax = 0;
		/* Ciclo con il for generalizzato ogni elemento della lista */
		for(Element weather : weathers)
		{
			try
			{
				/* Mi prendo la temperatura massima e quella minima ed elimino il simbolo dei gradi */
				String sMax = (weather.parents().get(2).getElementsByClass(TEMPERATURA).get(1).ownText()).replace("°", "");
				String sMin = (weather.parents().get(2).getElementsByClass(TEMPERATURA).get(0).ownText()).replace("°", "");
				/* Converto la stringa in intero*/
				int max = Integer.parseInt(sMax);
				int min = Integer.parseInt(sMin);
				/* Se la differenza è più grande del valore massimo */
				if((max-min)> differenzaMax)
				{
					/* Allora setta la variabile nome con il giorno in cui si è verificata */
					name = weather.ownText();
					/* E setta la differenza massima su questa differenza */
					differenzaMax = max-min;
				}
				
			}
			catch(NumberFormatException erroreConversione)
			{
				/* Comentato per favorire la leggibilitàs
				 * System.err.println("Elemento scarcato perchè non contiene un numero valido");
				 */
			}
		}
		
		return(name);
	}
	

}

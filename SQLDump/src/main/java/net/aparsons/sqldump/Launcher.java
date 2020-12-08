package net.aparsons.sqldump;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.aparsons.sqldump.db.Connectors.Connector;
import net.aparsons.sqldump.db.SQLDump;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Launcher {

	/**
	 * Create apache-cli options for the following elements:
	 *  url (Mandatory),
	 *  username (Mandatory): -user <user>
	 *  password (Mandatory): -pass <password>
	 *  query (Mandatory): -sql <query>
	 *  CSV file path: -f -file <filepath>
	 *  includeHeaders: -headers
	 *  All the options contains and argument, with the exception of includeHeaders   
	 * @return Available command line options
	 */
	public static Options getOptions() 
	{
		final Options options = new Options();
		
		final Option urlOption = new Option("url", true, "A database url of the form jdbc:subprotocol:subname");
		final Option usernameOption = new Option("user", true, "Username per accedere al db");
		final Option passwordOption = new Option("pass", true, "Password per accedere al db");
		final Option queryOption = new Option("sql", true, "Query di interrogazione");
		final Option csvOption = new Option("f", true, "Percorso file csv");
		final Option headersOption = new Option("headers", false, "Headers devl file csv");
	
		final OptionGroup urlGroup = new OptionGroup();
		urlGroup.setRequired(true);
		urlGroup.addOption(urlOption).addOption(usernameOption).addOption(passwordOption).addOption(queryOption).addOption(csvOption).addOption(headersOption);
		options.addOptionGroup(urlGroup);
		
		return options;
	}

	/**
	 * Prints the command line options to the console and return print usage as string
	 */
	public static String printUsage() 
	{
		HelpFormatter printer = new HelpFormatter();
		printer.printHelp("Help", getOptions());

		return "java -jar [nomeFileJar.jar] -url [jdbc:oracle:thin:@hostname:port:sid] -user [username]" + 
				"-pass [password] -sql [query]";
	}

	
	/**
	 * This method parse the command line parameters and put to a hash map where the parameter name is key point and the argument is the value.
	 * The hash map must contain all the required paramenter. If a mandory one is missed, it prints the parameter usage and throws an exception. 
	 * Use the parameter names defined in getOption method.
	 * @param consoleParams Command line arguments
	 * @throws ParseException 
	 * @throws Exception 
	 */
	public static Map<String, String> parse(String[] consoleParams) throws ParseException {
		Map<String, String> result = new HashMap<String, String>();
		try 
		{
			CommandLineParser parser = new GnuParser();
			CommandLine cmdLine = parser.parse(getOptions(), consoleParams);
			if(!cmdLine.hasOption("url")) throw new ParseException("No url is specifified");
			String url = cmdLine.getOptionValue("url");
			result.put("url",url);
			if(!cmdLine.hasOption("user")) throw new ParseException("No user is specifified");
			String user = cmdLine.getOptionValue("user");
			result.put("user",user);
			if(!cmdLine.hasOption("pass")) throw new ParseException("No pass is specifified");
			String pass = cmdLine.getOptionValue("pass");
			result.put("pass", pass);
			if(!cmdLine.hasOption("sql")) throw new ParseException("No query is specifified");
			String sql = cmdLine.getOptionValue("sql");
			result.put("sql",sql);
			if(!cmdLine.hasOption("f")) 
			{
				result.put("f", cmdLine.getOptionValue("f"));
				throw new ParseException("No file path  is specifified");
			}
			else
			{
				result.put("f", "true");
			}
			if(!cmdLine.hasOption("headers")) 
			{
				result.put("headers", cmdLine.getOptionValue("headers"));
				throw new ParseException("No  include column headers  is specifified");
			}
			else
			{
				result.put("headers", "true");
			}
		} 
		catch (ParseException pe) 
		{
			System.out.println(printUsage());
			throw pe;
		}
		return result;
	}
	

	
	
	/**
	 * COMPLETED METHOD
	 */
	private static void businessLogic(String protocol, String url, String username, String password, String sql, String file, boolean includeHeaders) 
	{
		SQLDump dump = new SQLDump(Connector.valueOf(protocol), url, username, password, sql);
		if (!file.isEmpty())
			dump.setFile(new File(file));
		if (includeHeaders) 
			dump.setHeaders(true);
		dump.run();
	}
	
	/**
	 * COMPLETED METHOD
	 */
	public static void main(String[] args) 
	{
		try 
		{
			Map<String, String> param = parse(args);
			businessLogic(param.get("protocol"), param.get("url"), 
					param.get("username"), param.get("password"), param.get("sql"), param.get("file"), false);
		} catch (ParseException pe) 
		{
			printUsage();
		
		}
	}

}

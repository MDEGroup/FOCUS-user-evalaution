package net.aparsons.sqldump;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
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
	public static Options getOptions() {
		final Options options = new Options();
		
		 
		final Option urlOption = new Option("url", true, "A database url of the form jdbc:subprotocol:subname");
		final Option user = new Option("user", true, "Username used for database access");
		user.setArgName("username");
		final Option pass = new Option("pass", true, "Password used for database access");
		pass.setArgName("password");
		final Option query = new Option("sql", true, "SQL query used for database interrogation");
		query.setArgName("query");
		final Option file = new Option("f", "file", true, "Filepath for the csv file");
		file.setArgName("filepath");
		final Option headers = new Option("headers", false, "Headers option");
		
		OptionGroup mandatory = new OptionGroup().addOption(urlOption).addOption(user).addOption(pass).addOption(query);
		mandatory.setRequired(true);
		options.addOptionGroup(mandatory);
		options.addOption(file);
		options.addOption(headers);
		//COMPLETE THE METHOD
		return options;
	}

	/**
	 * Prints the command line options to the console and return print usage as string
	 */
	public static String printUsage() {
		HelpFormatter printer = new HelpFormatter();
		
		printer.printHelp("Help", getOptions());
		//COMPLETE THE METHOD
	  return "\"java -jar SQLDump-0.0.1-jar-with-dependencies.jar -url [jdbc:oracle:thin:@hostname:port:sid] -user [username] -pass [password] -sql [query]\"";
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
		try {
			CommandLineParser parser = new GnuParser();
			CommandLine cmdLine = parser.parse(getOptions(), consoleParams);
			if(!cmdLine.hasOption("url")) throw new ParseException("No url is specifified");
			String url = cmdLine.getOptionValue("url");
			if(!cmdLine.hasOption("user")) throw new ParseException("No user is specifified");
			String user = cmdLine.getOptionValue("user");
			if(!cmdLine.hasOption("pass")) throw new ParseException("No password is specifified");
			String pass = cmdLine.getOptionValue("pass");
			if(!cmdLine.hasOption("sql")) throw new ParseException("No query is specifified");
			String query = cmdLine.getOptionValue("sql");
			
			result.put("url", url);
			result.put("user", user);
			result.put("pass", pass);
			result.put("sql", query);
			if(cmdLine.hasOption("file"))
				result.put("file", cmdLine.getOptionValue("file"));
			
			if(cmdLine.hasOption("headers"))
				result.put("headers", cmdLine.getOptionValue("headers"));
			
			//COMPLETE THE METHOD
			
			
		} catch (ParseException pe) {
			System.out.println(printUsage());
			throw pe;
		}
		return result;
	}
	

	
	
	/**
	 * COMPLETED METHOD
	 */
	private static void businessLogic(String protocol, String url, String username, String password, String sql, String file, boolean includeHeaders) {
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
	public static void main(String[] args) {
		try {
			Map<String, String> param = parse(args);
			businessLogic(param.get("protocol"), param.get("url"), 
					param.get("username"), param.get("password"), param.get("sql"), param.get("file"), false);
		} catch (ParseException pe) {
			printUsage();
		}
	}

}

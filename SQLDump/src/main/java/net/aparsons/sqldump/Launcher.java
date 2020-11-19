package net.aparsons.sqldump;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import net.aparsons.sqldump.db.Connectors.Connector;
import net.aparsons.sqldump.db.Connectors;
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
		final Option usernameOption = new Option("user", true, "Database username");
		final Option passwordOption = new Option("pass", true, "Database password");
		final Option queryOption = new Option("sql", true, "The SQL query to be executed");
		final Option fileOptionShort = new Option("f", true, "The file path to display query results");		
		final Option fileOptionLong = new Option("file", true, "The file path to display query results");
		final Option headersOption = new Option("headers", false, "Include headers option; set to false unless otherwise specified");		

		options.addOption(urlOption);
		options.addOption(usernameOption);
		options.addOption(passwordOption);
		options.addOption(queryOption);
		
		OptionGroup fileOptionGroup = new OptionGroup();
		fileOptionGroup.addOption(fileOptionLong);
		fileOptionGroup.addOption(fileOptionShort);
		
		options.addOptionGroup(fileOptionGroup);
		options.addOption(headersOption);
		
		return options;
	}

	/**
	 * Prints the command line options to the console and return print usage as string
	 */
	public static void printUsage() {
		HelpFormatter printer = new HelpFormatter();
		printer.printHelp("Help", getOptions());
	}

	
	/**
	 * This method parse the command line parameters and put to a hash map where the parameter name is key point and the argument is the value.
	 * The hash map must contain all the required parameters. If a mandatory one is missed, it prints the parameter usage and throws an exception. 
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
			if(!cmdLine.hasOption("url")) throw new ParseException("No url is specified");
			String url = cmdLine.getOptionValue("url");
			
			if(!cmdLine.hasOption("user")) throw new ParseException("No user is specified");
			String user = cmdLine.getOptionValue("user");
			
			if(!cmdLine.hasOption("pass")) throw new ParseException("No password is specified");
			String pass = cmdLine.getOptionValue("pass");
			
			if(!cmdLine.hasOption("sql")) throw new ParseException("No query is specified");
			String query = cmdLine.getOptionValue("sql");
			
			String file = null;
			if(cmdLine.hasOption("f")) file = cmdLine.getOptionValue("f");	
			else if (cmdLine.hasOption("file")) file = cmdLine.getOptionValue("file");
			
			Boolean headers = cmdLine.hasOption("headers");

			String protocol=null;
			if(url.contains("mysql")) protocol = "MYSQL";
			if(url.contains("oracle")) protocol = "ORACLE";
			result.put("protocol", protocol);
			result.put("url", url);
			result.put("username", user);
			result.put("password", pass);
			result.put("sql", query);
			result.put("file", file);
			if(headers) 
				result.put("headers", "true");
			else
				result.put("headers", "false");
			
		} catch (ParseException pe) {
			System.out.println(pe.getMessage());
			throw pe;
		}
		
		return result;
	}
		
	
	
	/**
	 * COMPLETED METHOD
	 */
	private static void businessLogic(String protocol, String url, String username, String password, String sql, String file, boolean includeHeaders) {
		SQLDump dump = new SQLDump(Connector.valueOf(protocol), url, username, password, sql);
		if (file!=null)
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
			System.out.println(param.toString());
			businessLogic(param.get("protocol"), param.get("url"), 
					param.get("username"), param.get("password"), param.get("sql"), param.get("file"), param.get("headers").equalsIgnoreCase("true"));
		} catch (ParseException pe) {
			printUsage();
		}
	}

}

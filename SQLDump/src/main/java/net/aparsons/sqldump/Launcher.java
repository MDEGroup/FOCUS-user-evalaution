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
		final Option urlOption = new Option("url", true, "The database url");
		final Option userOption = new Option("user", true, "The database username");
		final Option passOption = new Option("pass", true, "The database password");
		final Option sqlOption = new Option("sql", true, "The query used to contact the database");
		final Option fOption = new Option("f", true, "The csv file in which the SQL data are exported");
		final Option fileOption = new Option("file", true, "The csv file in which the SQL data are exported");
		final Option headersOption = new Option("headers", false, "The headers option");

		final OptionGroup fileOptionGroup = new OptionGroup();
		fileOptionGroup.addOption(fOption);
		fileOptionGroup.addOption(fileOption);
		
		final Options options = new Options();

		options.addOption(urlOption);
		options.addOption(userOption);
		options.addOption(passOption);
		options.addOption(sqlOption);
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
	 * The hash map must contain all the required paramenter. If a mandory one is missed, it prints the parameter usage and throws an exception. 
	 * Use the parameter names defined in getOption method.
	 * @param consoleParams Command line arguments
	 * @throws ParseException 
	 * @throws Exception 
	 */
	public static Map<String, String> parse(String[] consoleParams) throws ParseException {
		Map<String, String> result = new HashMap<String, String>();
		CommandLineParser parser = new GnuParser();

		CommandLine cmdLine = parser.parse(getOptions(), consoleParams);

		String protocol = null, url = null, user = null, pass = null, sql = null, file = null, headers = null;
		
		if(!cmdLine.hasOption("url")) throw new ParseException("Warning: No url is specified");
		else {
			url = cmdLine.getOptionValue("url");

			if(url.contains("mysql")) protocol = "MYSQL";
			else if (url.contains("oracle")) protocol = "ORACLE";
		}

		if(!cmdLine.hasOption("user")) throw new ParseException("Warning: No user is specified");
		else user = cmdLine.getOptionValue("user");
		
		if(!cmdLine.hasOption("pass")) throw new ParseException("Warning: No password is specified");
		else pass = cmdLine.getOptionValue("pass");
		
		if(!cmdLine.hasOption("sql")) throw new ParseException("Warning: No query is specified");
		else sql = cmdLine.getOptionValue("sql");

		if(cmdLine.hasOption("f")) file = cmdLine.getOptionValue("f");
		else if(cmdLine.hasOption("file")) file = cmdLine.getOptionValue("file");
		
		if(!cmdLine.hasOption("headers")) headers = "false";
		else headers = "true";
		
		result.put("protocol", protocol);
		result.put("url", url);
		result.put("user", user);
		result.put("pass", pass);
		result.put("sql", sql);
		result.put("file", file);
		result.put("headers", headers);
		
		return result;
	}
	
	private static void businessLogic(String protocol, String url, String user, String pass, String sql, String file, boolean headers) {
		SQLDump dump = new SQLDump(Connector.valueOf(protocol), url, user, pass, sql);
		if (file != null)
			dump.setFile(new File(file));
		if (headers) 
			dump.setHeaders(true);
		dump.run();
	}
	
	
	public static void main(String[] args) {
		try {
			Map<String, String> param = parse(args);
			businessLogic(param.get("protocol"), param.get("url"), param.get("user"), param.get("pass"), param.get("sql"), param.get("file"), param.get("headers").equals("true"));
			
		} catch (ParseException pe) {
			System.out.println(pe.getMessage() + "\n");
			printUsage();
		}
	}

}

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
		final Option usernameOption = new Option("user", true, "username for database access");
		final Option passwordOption = new Option("pass", true, "password for database access");
		final Option queryOption = new Option("sql", true, "query for the database");
		final Option fileOption = new Option("f", "file", true, "filepath for the csv file");
		final Option headersOption = new Option("headers", false, "headers option");
		final Option protocolOption = new Option("protocol", true, "protocol option");
		
		final OptionGroup protocolGroup = new OptionGroup();
		protocolGroup.setRequired(true);
		protocolGroup.addOption(protocolOption);
		final OptionGroup urlGroup = new OptionGroup();
		urlGroup.setRequired(true);
		urlGroup.addOption(urlOption);
		final OptionGroup usernameGroup = new OptionGroup();
		usernameGroup.setRequired(true);
		usernameGroup.addOption(usernameOption);
		final OptionGroup passwordGroup = new OptionGroup();
		passwordGroup.setRequired(true);
		passwordGroup.addOption(passwordOption);
		final OptionGroup queryGroup = new OptionGroup();
		queryGroup.setRequired(true);
		queryGroup.addOption(queryOption);
		final OptionGroup fileGroup = new OptionGroup();
		fileGroup.setRequired(false);
		fileGroup.addOption(fileOption);
		final OptionGroup headersGroup = new OptionGroup();
		headersGroup.setRequired(false);
		headersGroup.addOption(headersOption);
		
		options.addOptionGroup(urlGroup);
		options.addOptionGroup(usernameGroup);
		options.addOptionGroup(passwordGroup);
		options.addOptionGroup(queryGroup);
		options.addOptionGroup(fileGroup);
		options.addOptionGroup(headersGroup);
		options.addOptionGroup(protocolGroup);
		
		return options;
	}

	/**
	 * Prints the command line options to the console and return print usage as string
	 */
	public static String printUsage() {
		HelpFormatter printer = new HelpFormatter();
		
		printer.printHelp("Help", getOptions());
		
		return "java -jar SQLDump-0.0.1-jar-with-dependencies.jar -url [jdbc:oracle:thin:@hostname:port:sid] -user [username] -pass [password] -sql [query]";
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
		if(!(consoleParams.length == 0)) {
			try {
				CommandLineParser parser = new GnuParser();
				CommandLine cmdLine = parser.parse(getOptions(), consoleParams);
				if(!cmdLine.hasOption("url")) throw new ParseException("No url is specifified");
				String url = cmdLine.getOptionValue("url");
				if(!cmdLine.hasOption("user")) throw new ParseException("No user is specifified");
				String user = cmdLine.getOptionValue("user");
				if(!cmdLine.hasOption("pass")) throw new ParseException("No password is specifified");
				String pass = cmdLine.getOptionValue("pass");
				if(!cmdLine.hasOption("query")) throw new ParseException("No query is specifified");
				String query = cmdLine.getOptionValue("query");
				if(!cmdLine.hasOption("protocol")) throw new ParseException("No protocol is specifified");
				String protocol = cmdLine.getOptionValue("protocol");
				String file = null;
				if(cmdLine.hasOption("file"))file = cmdLine.getOptionValue("file");
				boolean headers = false;
				if(cmdLine.hasOption("headers"))headers = true;
				result.put("url", url);
				result.put("username", user);
				result.put("password", pass);
				result.put("protocol", protocol);
				result.put("query", query);
				if(file!=null) {
					result.put("file", file);
				}
				if(headers) {
					result.put("headers", "true");
				}
				

			} catch (ParseException pe) {
				System.out.println();
				throw pe;
			}
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
			if(!param.isEmpty()) {
				businessLogic(param.get("protocol"), param.get("url"), 
						param.get("username"), param.get("password"), param.get("sql"), param.get("file"), false);
			}else throw new ParseException("No paramaters specified");
			
		} catch (ParseException pe) {
			System.out.println(printUsage());
		}
	}

}

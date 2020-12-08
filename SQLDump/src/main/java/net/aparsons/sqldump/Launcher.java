package net.aparsons.sqldump;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
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
		final Option usernameOption = new Option("user", true, "Username for database");
		final Option passwordOption = new Option("pass", true, "Password for database");
		final Option queryOption = new Option("sql", true, "Query to execute");
		final Option csvPathOption = new Option("f", "file", true, "CSV File path");
		final Option includeHeaderOption = new Option("headers", false, "Include headers");
		
		final OptionGroup urlGroup = new OptionGroup();
		urlGroup.setRequired(true);
		urlGroup.addOption(urlOption);
		options.addOptionGroup(urlGroup);
		
		final OptionGroup usernameGroup = new OptionGroup();
		usernameGroup.setRequired(true);
		usernameGroup.addOption(usernameOption);
		options.addOptionGroup(usernameGroup);
		
		final OptionGroup passwordGroup = new OptionGroup();
		passwordGroup.setRequired(true);
		passwordGroup.addOption(passwordOption);
		options.addOptionGroup(passwordGroup);
		
		final OptionGroup queryGroup = new OptionGroup();
		queryGroup.setRequired(true);
		queryGroup.addOption(queryOption);
		options.addOptionGroup(queryGroup);
		
		final OptionGroup csvPathGroup = new OptionGroup();
		csvPathGroup.setRequired(false);
		csvPathGroup.addOption(csvPathOption);
		options.addOptionGroup(csvPathGroup);
		
		final OptionGroup includeHeaderGroup = new OptionGroup();
		includeHeaderGroup.setRequired(false);
		includeHeaderGroup.addOption(includeHeaderOption);
		options.addOptionGroup(includeHeaderGroup);
		
		//COMPLETE THE METHOD
		return options;
	}

	/**
	 * Prints the command line options to the console and return print usage as string
	 */
	public static String printUsage() {
		//HelpFormatter printer = new HelpFormatter();
		//printer.printHelp("Help", getOptions());
		Iterator ir = getOptions().getOptions().iterator();
		String usage = "\nHelp:\n\nList of arguments and usage\n\n";
		while(ir.hasNext()) {
			Option o = (Option) ir.next();
			String longOpt = "";
			String arg = "";
			String desc = o.getDescription();
			String opt = o.getOpt();
			if(o.hasArg()) {
				arg = "<arg>";
			}
			if(o.getLongOpt() != null) {
				longOpt = ", --" + o.getLongOpt();
			}
			usage += "-" + opt + longOpt + " " + arg + "\t\t" + desc + "\n";
		}
		return usage;
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
			
			if(!cmdLine.hasOption("url")) throw new ParseException("No url is specified");
			String url = cmdLine.getOptionValue("url");
			
			if(!cmdLine.hasOption("user")) throw new ParseException("Username required");
			String username = cmdLine.getOptionValue("user");
			
			if(!cmdLine.hasOption("pass")) throw new ParseException("Password required");
			String password = cmdLine.getOptionValue("pass");
			
			if(!cmdLine.hasOption("sql")) throw new ParseException("No query provided");
			String query = cmdLine.getOptionValue("sql");
			
			String protocol=null;
			String file = "";
			String headers = "false";
			if(url.contains("mysql")) protocol = "MYSQL";
			if(url.contains("oracle")) protocol = "ORACLE";
			
			result.put("protocol", protocol);
			result.put("url", url);
			result.put("username", username);
			result.put("password", password);
			result.put("sql", query);
			
			if(cmdLine.hasOption("f")) {
				file = cmdLine.getOptionValue("f");
			}
			
			if(cmdLine.hasOption("file")) {
				file = cmdLine.getOptionValue("f");
			}
			
			if(cmdLine.hasOption("headers")) {
				headers = "true";
			}
			
			result.put("headers", headers);
			result.put("file", file);
			
			if(protocol == null) throw new ParseException("Bad protocol");
			
		} catch (ParseException pe) {
			//System.out.println("\n"+pe.getMessage()+"\n");
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
					param.get("username"), param.get("password"), param.get("sql"), param.get("file"), param.get("headers").equalsIgnoreCase("true"));
		} catch (ParseException pe) {
			System.out.println(printUsage());
		}
	}

}

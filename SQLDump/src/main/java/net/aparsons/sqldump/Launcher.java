package net.aparsons.sqldump;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.aparsons.sqldump.db.Connectors.Connector;
import net.aparsons.sqldump.db.SQLDump;
import oracle.net.aso.e;

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
	 * 
	 * 
	 */
	
	
	public static String printUsage() {
		
		Iterator ir= getOptions().getOptions().iterator();
		String usage ="\nHelp:\n\nList of options and descriptions of argumets \n\n";
		while(ir.hasNext()) {
			
			Option o = (Option) ir.next();
			String opt,desc;
			String longOpt="";
			String arg="   ";
			if(o.hasArg()) {
				arg= "<arg>";
				
			}
			opt= o.getOpt();
			if(o.getLongOpt() !=null) {
				longOpt= ", --"+o.getLongOpt();
			}
			desc =o.getDescription();
			usage += "-" +opt +longOpt + ""+ arg + "\t\t" + desc +"\n";
		}
		return usage;
	}
	public static Options getOptions() {
		
		
		final Options options = new Options();
		
		 
		final Option urlOption = new Option("url", true, "A database url of the form jdbc:subprotocol:subname");
		
		final Option usernameOption = new Option("user",true,"username");
		final Option passwordOption = new Option("pass",true,"password");
		final Option queryOption = new Option("sql",true,"query");
		final Option CsvOption = new Option("f",false,"filepath");
		final Option headerOption = new Option("headers",false,"headers");
		
		final OptionGroup urlGroup = new OptionGroup();
		urlGroup.setRequired(true);
		urlGroup.addOption(urlOption);
		options.addOptionGroup(urlGroup);
		
		final OptionGroup usernameGroup= new OptionGroup();
		usernameGroup.setRequired(true);
		usernameGroup.addOption(usernameOption);
		options.addOptionGroup(usernameGroup);
		
		final OptionGroup passwordGroup= new OptionGroup();
		passwordGroup.setRequired(true);
		passwordGroup.addOption(passwordOption);
		options.addOptionGroup(passwordGroup);
		
		final OptionGroup CsvGroup= new OptionGroup();
		CsvGroup.setRequired(false);
		CsvGroup.addOption(CsvOption);
		options.addOptionGroup(CsvGroup);
		
		final OptionGroup queryGroup= new OptionGroup();
		queryGroup.setRequired(false);
		queryGroup.addOption(queryOption);
		options.addOptionGroup(queryGroup);
		
		final OptionGroup headersGroup= new OptionGroup();
		headersGroup.setRequired(false);
		headersGroup.addOption(headerOption);
		options.addOptionGroup(headersGroup);
		
		
		
		
		
		
		//COMPLETE THE METHOD
		return options;
	}

	/**
	 * Prints the command line options to the console and return print usage as string
	 */
	

	
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
			String username = cmdLine.getOptionValue("user");
			if(!cmdLine.hasOption("pass")) throw new ParseException("No password is specifified");
			String password = cmdLine.getOptionValue("pass");
			if(!cmdLine.hasOption("sql")) throw new ParseException("No sql is specifified");
			String sql = cmdLine.getOptionValue("sql");
			if(!cmdLine.hasOption("f")) throw new ParseException("No file is specifified");
			String csv = cmdLine.getOptionValue("f");
		
			if(!cmdLine.hasOption("headers")) throw new ParseException("No headers is specifified");
			String headers = cmdLine.getOptionValue("headers");
			
			result.put(url, url);
			result.put(csv, csv);
			result.put(sql, sql);
			result.put(password, password);
			result.put(username, username);
			result.put(headers, headers);
			
			
			//COMPLETE THE METHOD
			
			
		}
		
		catch (ParseException pe) {
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
			System.out.println(printUsage());
		}
	}

}

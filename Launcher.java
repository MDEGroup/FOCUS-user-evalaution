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
		
		//COMPLETE THE METHOD
		 
		final Option urlOption = new Option("url", true, "A database url of the form jdbc:subprotocol:subname");	
		final Option usernameOption = new Option("user", true, "username option");
		final Option passwordOption= new Option("pass", true, "password option");
		final Option queryOption= new Option("sql", true, "query option");
		final Option filePathOption= new Option("f","file", true, "filepath option");
		final Option headersOption= new Option("headers", false, "headers option");
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
		
		final OptionGroup filePathGroup = new OptionGroup();
		filePathGroup.setRequired(true);
		filePathGroup.addOption(filePathOption);
		
		final OptionGroup headersGroup = new OptionGroup();
		headersGroup.setRequired(true);
		headersGroup.addOption(headersOption);
		
		
		options.addOptionGroup(urlGroup);
		options.addOptionGroup(usernameGroup);
		options.addOptionGroup(passwordGroup);
		options.addOptionGroup(queryGroup);
		options.addOptionGroup(filePathGroup);
		options.addOptionGroup(headersGroup);

		
		return options;
	}

	/**
	 * Prints the command line options to the console and return print usage as string
	 */
	public static String printUsage() {
		HelpFormatter printer = new HelpFormatter();
		printer.printHelp("Help", getOptions(),true);
		//COMPLETE THE METHOD
		  
			    final String version = SQLDump.VERSION;
			    
			    String line= "\nCommand Line\n: java -jar "
			    +" SQLDump-"+version+"jar -url [jdbc:oracle:thin:@hostname:port:sid]"
			    		+ " -user [username] pass [password] -sql [query]\n";
			    		
		  return line  ;
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
			
			//COMPLETE THE METHOD
			if(!cmdLine.hasOption("url")) throw new ParseException("No url is specifified");
			String url = cmdLine.getOptionValue("url");
			
			if(!cmdLine.hasOption("user")) throw new ParseException("No username is specifified");
			String user = cmdLine.getOptionValue("user");
		
			if(!cmdLine.hasOption("pass")) throw new ParseException("No password is specifified");
			String pass = cmdLine.getOptionValue("pass");
		
			if(!cmdLine.hasOption("protocol")) throw new ParseException("No protocol is specifified");
			String protocol = cmdLine.getOptionValue("protocol");
			

			if(!cmdLine.hasOption("sql")) throw new ParseException("No query is specifified");
			String sql = cmdLine.getOptionValue("sql");
			
			boolean headers = false;
			if(cmdLine.hasOption("headers"))headers = true;
			if(headers) {
				result.put("headers", "true");
			}
		
			String file = null;
			if(cmdLine.hasOption("file")||cmdLine.hasOption("f"))file = cmdLine.getOptionValue("file");
			if(file!=null) {
				result.put("file", file);
			}
			
			result.put("url", url);
			result.put("username", user);
			result.put("password", pass);
			result.put("protocol", protocol);
			result.put("sql",sql);
			 

			
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

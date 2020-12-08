
package net.aparsons.sqldump;

//import java.io.Console;
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
import org.apache.commons.cli.PosixParser;

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
  final Option usernameOption = new Option("username", true, "Username for database access");
  final Option passwordOption = new Option("password", true, "Password for database access");
  final Option queryOption = new Option("query",true, "A database query");
  final Option CSVOption = new Option("CSV", true, "File path for CSV file");
  final Option includeHeaderOption = new Option("includeHeader",true, "includeHeader");


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

  final OptionGroup csvGroup = new OptionGroup();
  csvGroup.setRequired(false);
  csvGroup.addOption(CSVOption);
  options.addOptionGroup(csvGroup);

  final OptionGroup headersGroup = new OptionGroup();
  headersGroup.setRequired(false);
  headersGroup.addOption(includeHeaderOption);
  options.addOptionGroup(headersGroup);
  
  //COMPLETE THE METHOD
  return options;
 }

 /**
  * Prints the command line options to the console and return print usage as string
  */
 public static String printUsage() {
  HelpFormatter printer = new HelpFormatter();
  
  printer.printHelp("Help", getOptions());
  Options ops = getOptions();

  printer.printHelp("Help", ops);
        Option urlOpt = ops.getOption("url") ;


        String OptDescrizioneString = urlOpt.getDescription() + " ";
        String OptUrlString = urlOpt.getOpt().concat("  ".concat(OptDescrizioneString)+"\n");


        Option UsernameOpt = ops.getOption("username");  
        String OptUserDescrizioneString = UsernameOpt.getDescription() + " ";
        String OptUserString = UsernameOpt.getOpt().concat("  ".concat(OptUserDescrizioneString)+"\n");

        Option PassOpt = ops.getOption("password");
        String OptPassDescrizioneString = PassOpt.getDescription() + " ";
        String OptPassString = PassOpt.getOpt().concat("  ".concat(OptPassDescrizioneString) +"\n");

        Option QueryOpt = ops.getOption("query"); 
        String OptQueryDescrizioneString = QueryOpt.getDescription() + " ";
        String OptQueryString = QueryOpt.getOpt().concat("  ".concat(OptQueryDescrizioneString)+"\n");

        Option CSVOpt = ops.getOption("CSV");
        String OptCSVFilePathOptDescrizioneString = CSVOpt.getDescription() + " ";
String OptCSVFilePathOptString = CSVOpt.getOpt().concat(" ".concat(OptCSVFilePathOptDescrizioneString)+"\n");

        Option includeHeaders = ops.getOption("includeHeader"); 
        String OptincludeHeadersDescrizioneString = includeHeaders.getDescription() + " ";
        String OptincludeHeadersString = includeHeaders.getOpt().concat("  ".concat(OptincludeHeadersDescrizioneString)+"\n");


        String finalOptString = new String(OptUrlString.concat(OptUserString).concat(OptPassString).concat(OptQueryString)).concat(OptCSVFilePathOptString).concat(OptincludeHeadersString);

  //COMPLETE THE METHOD
  return "";
  return finalOptString;
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
   
   //COMPLETE THE METHOD
   if(!cmdLine.hasOption("username")) throw new ParseException("No username is specifified");
   String username = cmdLine.getOptionValue("username");

   if(!cmdLine.hasOption("password")) throw new ParseException("No password is specifified");
   String password = cmdLine.getOptionValue("password");

   if(!cmdLine.hasOption("query")) throw new ParseException("No query is specifified");
   String query = cmdLine.getOptionValue("query");

   if(!cmdLine.hasOption("CSV")) throw new ParseException("No CSV is specifified");
   String CSV = cmdLine.getOptionValue("CSV");

   if(!cmdLine.hasOption("includeHeader")) throw new ParseException("No header is specifified");
   String includeHeader = cmdLine.getOptionValue("includeHeader");

   result.put("url",url);
   result.put("username", username);
   result.put("password", password);
   result.put("query", query);
   result.put("CSV", CSV);
   result.put("includeHeader", includeHeader);
   
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
   //Map<String, String> param = parse(args);
  
   Map<String, String> param = parse(args);
   if(!param.isEmpty()) {
    businessLogic(param.get("protocol"), param.get("url"), 
      param.get("username"), param.get("password"), param.get("sql"), param.get("file"), false);
   }else throw new ParseException("No paramaters specified");
  } catch (ParseException pe) {
   printUsage();
   System.out.println(printUsage());
  }
 }

}


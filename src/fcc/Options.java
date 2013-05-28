package fcc;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

public class Options {
	private static final String PROGRAM_NAME = "fcc";
	private static final String PROGRAM_VERSION = "2.0";
	private String sourceFile;
	private String outputFile;
	private boolean isDebugAst = false;
	private String arch = "arduino";
	public Options(String[] args)
	{
		parseArgs(args); 
	}
	private void parseArgs(String[] origArgs)
	{
		ArrayList<String> sourceFiles = new ArrayList<String>();
		ListIterator<String> args = Arrays.asList(origArgs).listIterator();
		while(args.hasNext()) {
			String arg = args.next();
			if(arg.startsWith("-")) {
				if(arg.equals("-o")) {
					outputFile = this.getOptArg(arg, args);
				}else if(arg.startsWith("-arch-")) {
					arch = arg.substring("-arch-".length());
				}else if(arg.equals("-version")) {
					System.out.printf("%s version %s", Options.PROGRAM_NAME, Options.PROGRAM_VERSION);
					System.out.println("");
					System.exit(0);
				}else if(arg.equals("-debug-ast")) {
					isDebugAst = true;
				}else if(arg.equals("-help")) {
					printUsage(System.out);
					System.exit(0);
				}else {
					System.err.println("Unknown option: " + arg);
					System.exit(0);
				}
			}else {
				sourceFiles.add(arg);
			}
		}
		if(sourceFiles.size() == 0) {
			System.err.println("No input file");
			System.exit(0);			
		}else if(sourceFiles.size() > 1) {
			System.err.println("Only 1 input file");
			System.exit(0);			
		}else
			sourceFile = sourceFiles.get(0);
		if(outputFile == null) outputFile = sourceFile.substring(0, sourceFile.lastIndexOf("."));
	}
	public boolean isDebugAst()
	{
		return isDebugAst;
	}
	public String sourceFile()
	{
		return sourceFile;
	}
	public String outputFile()
	{
		return outputFile;
	}
	public String arch()
	{
		return arch;
	}
	private String getOptArg(String opt, ListIterator<String> args)
	{
		String path = opt.substring(2);
		if(path.length() != 0) {
			return path;
		}else
			return nextArg(opt, args);
	}
	private String nextArg(String opt, ListIterator<String> args)
	{
		if(!args.hasNext()) {
			System.err.println("missing argument for " + opt);
			System.exit(0);
		}
		return args.next();
	}
	private void printUsage(PrintStream out)
	{
		out.printf("Usage: %s [options] file", Options.PROGRAM_NAME);
		out.println("");
		out.println(" -o PATH         Places output in file PATH.");
		out.println(" -arch-arduino");
		out.println(" -arch-tinyos");
		out.println(" -debug-tokens");
		out.println(" -version        Shows compiler version and quit.");
		out.println(" -help           Prints this message and quit.");
		out.println("");
	}
}

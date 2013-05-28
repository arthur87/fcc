package fcc;

import fcc.compiler.FCCompiler;

public class Main {
	public Main(String args[])
	{
		Options options = new Options(args);
		FCCompiler compiler = new FCCompiler(options);
	}
	public static void main(String[] args)
	{
		new Main(args);
		
	}
}

package fcc.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FCError {
	public FCError(String fileName, int lineNumber)
	{
		int i = 0;
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			while((line = reader.readLine()) != null) {
				if(++i == lineNumber) break;
			}
		}catch(Exception e) {
			System.err.println("No such file or directory");
			System.exit(0);
		}		
		
		System.err.println(new File(fileName).getName() + ":"+ lineNumber + ": error: " + line);
		System.exit(1);
	}
	public FCError(String s)
	{
		System.err.println(s);
		System.exit(1);		
	}
}

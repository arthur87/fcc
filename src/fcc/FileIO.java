package fcc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

public class FileIO {
	public static String fileReader(String fileName)
	{
		StringBuilder sb = new StringBuilder(""); 
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = reader.readLine()) != null)
				sb.append(line + System.getProperty("line.separator"));
		}catch(Exception e) {
			System.err.println("No such file or directory");
			System.exit(0);
		}
		return sb.toString();
	}
	public static boolean fileWriter(String fileName, String s, boolean isPostscript)
	{
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, isPostscript), "UTF-8"));
			writer.write(s);
			writer.close();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	public static String getPreffix(String fileName)
	{
		if(fileName == null) return null;
		int p = fileName.lastIndexOf(".");
		if(p != -1) return fileName.substring(0, p);
		return fileName;
	}
	public static String getPreffix(String fileName, String extension)
	{
		return FileIO.getPreffix(fileName).concat("." + extension);
	}
	public static String getName(String fileName)
	{
		return new File(fileName).getName();
	}
}

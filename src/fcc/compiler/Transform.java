package fcc.compiler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Transform {
	public String resourceReader(String fileName)
	{
		StringBuilder sb = new StringBuilder("");
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(fileName);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(input));
			while(true) {
				String line = br.readLine();
				if(line == null) break;
				else sb.append(line).append("\n");
			}
			br.close();
			input.close();
		}catch(Exception e) {
			System.err.println("No such file or directory");
			System.exit(0);
		}
		return sb.toString();
	}
}

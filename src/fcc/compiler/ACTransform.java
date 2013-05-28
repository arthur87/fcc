package fcc.compiler;

import java.util.ArrayList;
import java.util.HashMap;

import fcc.FileIO;
import fcc.Options;

public class ACTransform extends Transform {
	private FCSyntaxCheck syntaxCheck;
	public ACTransform(FCSyntaxCheck syntaxCheck, Options options)
	{
		this.syntaxCheck = syntaxCheck;
		String template = this.resourceReader("arduino.tpl");
		FileIO.fileWriter(options.outputFile()+".cpp",template.replaceFirst("<MAIN_CODE>", transform()), false);
	}
	private String transform()
	{
		FCTokenStream tokenStream = this.syntaxCheck.tokenStream();
		FCTable table = this.syntaxCheck.table();
		FCTokens realTokens = tokenStream.getRealTokens();
		FCToken token;
		String fname = null;
		StringBuilder codeGen = new StringBuilder("");
		codeGen.append(transformVars(table.getGlobalVar()));
		for(int i = 0; i < realTokens.size(); i++) {
			token = realTokens.get(i);
			if(token.tKind == FCTokenKind.FUNCTION) {
				/* 関数名の取得 */
				while(true) {
					if(token.tClass == FCTokenClass.ID && !token.isWhiteSpace()) { fname = token.tName; break;}
					token = realTokens.get(++i);
				}
				/* OBJECT_LEFTまでスキップ */
				while(true) {
					token = realTokens.get(++i);
					if(token.tKind == FCTokenKind.OBJECT_LEFT) break;
				}
				int depth = 1;
				codeGen.append(transformFunction(fname, table));
				while(true) {
					token = realTokens.get(++i);
					if(token.tKind == FCTokenKind.VAR) {
						while(true) {
							token = realTokens.get(++i);
							if(token.tKind == FCTokenKind.SEMICOLON) break;
						}
						token = realTokens.get(++i);
					}
					if(token.tKind == FCTokenKind.OBJECT_LEFT) depth++;
					if(token.tKind == FCTokenKind.OBJECT_RIGHT) {
						depth--;
						if(depth == 0) {
							codeGen.append(token.tName).append("\n");
							break;
						}
					}
					if(token.tClass == FCTokenClass.ID && token.tName.indexOf(".") > 0) {
						codeGen.append(transformAPI(token.tName));
					}else
						codeGen.append(token.tName);
				}
			}
		}
		return functionPrototype.append(codeGen.toString()).toString();
	}
	private StringBuilder functionPrototype = new StringBuilder("");
	private String transformFunction(String fname, FCTable table)
	{
		String s;
		String vars = transformVars(table.getLocalVar(fname));
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < table.getFunction(fname).size(); i++) {
			if(sb.length() > 0) sb.append(", ");
			sb.append("float").append(" ").append(table.getFunction(fname).get(i));
		}
		if(table.isFuncType(fname)) {
			s = "void " + fname + "(" + sb.toString() + ")";
		}else {
			s = "float " + fname + "(" + sb.toString() + ")";
		}
		functionPrototype.append(s).append(";\n");
		return s + " {" + vars;
	}
	private String transformAPI(String s) {
		HashMap<String, String> apis = new HashMap<String, String>();
		apis.put("Loop.start", "_Loop_start");
		apis.put("IO.digitalWrite", "_IO_digitalWrite");
		apis.put("IO.digitalRead", "_IO_digitalRead");
		apis.put("IO.analogWrite", "_IO_analogWrite");
		apis.put("IO.analogRead", "_IO_analogRead");
		if(apis.containsKey(s)) return apis.get(s);
		return s;
	}
	private String transformVars(ArrayList<String> s)
	{
		StringBuilder sb = new StringBuilder("\n");
		for(int i = 0; i < s.size(); i++) {
			sb.append("float").append(" ").append(s.get(i)).append(" = 0.0;\n");
		}
		return sb.toString();
	}
}

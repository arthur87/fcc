package fcc.compiler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import fcc.FileIO;
import fcc.Options;

public class NCTransform extends Transform {
	private FCSyntaxCheck syntaxCheck;
	public NCTransform(FCSyntaxCheck syntaxCheck, Options options)
	{
		this.syntaxCheck = syntaxCheck;
		/* ファイルの保存 */
		String template = this.resourceReader("tinyos_h.tpl");
		FileIO.fileWriter(options.outputFile()+".h", template, false);
		template = this.resourceReader("tinyos_c.tpl");
		FileIO.fileWriter(options.outputFile()+"C.nc", template.replaceFirst("<MAIN_CODE>", transform()).replaceAll("<OUTPUT>", new File(options.outputFile()).getName()), false);
		template = this.resourceReader("tinyos_appc.tpl");
		FileIO.fileWriter(options.outputFile()+"AppC.nc", template.replaceAll("<OUTPUT>", new File(options.outputFile()).getName()), false);		
		template = this.resourceReader("tinyos_make.tpl");
		FileIO.fileWriter(options.outputFile().substring(0, options.outputFile().lastIndexOf(new File(options.outputFile()).getName()))+"Makefile", template.replaceFirst("<OUTPUT>", new File(options.outputFile()).getName()), false);

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
					if(token.tClass == FCTokenClass.ID && !token.isWhiteSpace()) {fname = token.tName; break;}
					token = realTokens.get(++i);
				}
				/* OBJECT_LEFTまでステップ */
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
		if(fname.equals("Loop")) {
			return "event void Loop.fired() {";
		}else if(fname.equals("Read")) {
			return "event void Read.readDone(error_t _result, uint16_t _data) {";
		}else if(fname.equals("Receive")) {
			return "event message_t* Receive.receive(message_t* _bufPtr, void* _payload, uint8_t _len) {\n" +
					"radio_sense_msg_t* rsm = (radio_sense_msg_t*)_payload;\n";
		}
		String s;
		String vars = transformVars(table.getLocalVar(fname));
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < table.getFunction(fname).size(); i++) {
			if(sb.length() > 0) sb.append(", ");
			sb.append("uint16_t").append(" ").append(table.getFunction(fname).get(i));
		}
		if(table.isFuncType(fname)) {
			s = "void " + fname + "(" + sb.toString() + ")";
		}else {
			s = "uint16_t " + fname + "(" + sb.toString() + ")";
		}
		functionPrototype.append(s).append(";\n");
		return s + "{" + vars;
	}
	private String transformAPI(String s)
	{
		HashMap<String, String> apis = new HashMap<String, String>();
		apis.put("Loop.start", "_Loop_start");
		apis.put("Read.read", "_Read_read");
		apis.put("Send.send", "_Send_send");
		apis.put("Read.data", "_data");
		apis.put("Receive.data1", "rsm->data1");
		apis.put("Receive.data2", "rsm->data2");
		apis.put("Receive.data3", "rsm->data3");
		apis.put("Receive.data4", "rsm->data4");
		apis.put("Receive.data5", "rsm->data5");
		apis.put("Receive.id", "rsm->id");
		apis.put("Leds.led", "_Leds_led");
		if(apis.containsKey(s)) return apis.get(s);
		return s;
	}
	private String transformVars(ArrayList<String> s)
	{
		StringBuilder sb = new StringBuilder("\n");
		for(int i = 0; i < s.size(); i++)
			sb.append("uint16_t").append(" ").append(s.get(i)).append(" = 0;\n");
		return sb.toString();
	}
}

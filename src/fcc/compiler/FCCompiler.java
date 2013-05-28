package fcc.compiler;

import org.mozilla.javascript.*;
import org.mozilla.javascript.ast.AstRoot;

import fcc.FileIO;
import fcc.Options;

public class FCCompiler {
	public FCCompiler(Options options)
	{
		AstRoot astRoot = null;
		String source = FileIO.fileReader(options.sourceFile());
		/* 構文解析・意味解析の実行 */
		try {
			astRoot = new Parser().parse(source.toString(), FileIO.getName(options.sourceFile()), 1);
		}catch(org.mozilla.javascript.EvaluatorException e) {
			new FCError(options.sourceFile(), e.lineNumber());;
		}
		if(options.isDebugAst()) {
			debugAstPrint(astRoot.debugPrint());
		}
		FCSyntaxCheck syntaxCheck = new FCSyntaxCheck(options, source);
		
		if(options.arch().equals("tinyos")) {
			new NCTransform(syntaxCheck, options);
		}else {
			new ACTransform(syntaxCheck, options);
		}
	}
	/**
	 * 構文木を出力する
	 * @param ast
	 */
	private void debugAstPrint(String ast)
	{
		StringBuffer sb = new StringBuffer("");
		String[] lines = ast.split("\n");
		int i = 0;
		while(i < lines.length) {
			String[] line = lines[i].split(" ");
			for(int j = 0; j < Integer.valueOf(line[0]); j++) sb.append(" ");
			sb.append(line[1]);
			if(line.length == 4) sb.append(" ").append(line[3]);
			sb.append("\n");
			i++;
		}
		System.out.print(sb.toString());
		System.exit(0);					
	}

}

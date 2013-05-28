package fcc.compiler;

import java.util.LinkedList;

public class FCTokenStream {
	private String source;
	private int index;
	private char ch;
	private FCTokens tokens;
	private FCTokens realTokens;
	private boolean isCharLock = false;
		
	private int cl = 1; /* ソースコード ライン */
	private int ci = 1; /* ソースコード インデックス*/
	
	public FCTokenStream(String source)
	{
		new FCToken();
		this.source = deleteComment(source);
		this.index = 0;
		this.tokens = new FCTokens();
		this.realTokens = new FCTokens();
		init();
	}
	public FCTokens getTokens()
	{
		return this.tokens;
	}
	public FCTokens getRealTokens()
	{
		return this.realTokens;
	}

	/**
	 * 文字を返す
	 * @return
	 */
	private boolean nextChar()
	{
		if(this.isCharLock == true) {this.isCharLock = false; return true;}
		if(this.index < this.source.length()) {
			ch = this.source.charAt(this.index++);
			if(ch == System.getProperty("line.separator").charAt(0)) {
				this.cl++; this.ci = 1;
			}else this.ci++;
			return true;
		}
		return false;
	}
	/**
	 * 字句の登録
	 * @param s
	 */
	private void register(String s)
	{
		FCToken token = new FCToken();
		token.set(s, cl, ci);
		realTokens.add(token);
		if(s.equals("\n") || s.equals(" "))
			return;
		tokens.add(token); 
		
	}
	/**
	 * 字句解析
	 */
	private void init()
	{
		char c;
		while(this.nextChar()) {
			if(ch == '\n' || ch == ' ') {
				register(String.valueOf(ch));
			}
			if(ch == '+' || ch == '-' || ch == '<' || ch == '>' || ch == '&' || ch == '|') {
				c = ch;
				if(this.nextChar()) {
					if(ch == '=' || ch == c) {register(String.valueOf(c) + String.valueOf(ch)); this.nextChar();}
					else register(String.valueOf(c));
				}
			}
			if(ch == '/' || ch == '%' || ch == '*' || ch == '=' || ch == '=' || ch == '^' || ch == '!') {
				c = ch;
				if(this.nextChar()) {
					if(ch == '=') {register(String.valueOf(c) + String.valueOf(ch)); this.nextChar();}
					else register(String.valueOf(c));
				}
			}
			if(ch == '~' || ch == '{' || ch == '}' || ch == '(' || ch == ')' || ch == '[' || ch == ']' || ch == ';' || ch == ',' || ch == '@') {
				register(String.valueOf(ch));
			}
			if(Character.isLetter(ch)) {
				String name = String.valueOf(ch);
				while(this.nextChar()) {
					if(Character.isLetterOrDigit(ch) || ch == '_' || ch == '.') name += ch;
					else break;
				}
				register(name);
				this.isCharLock = true;
			}
			if(Character.isDigit(ch)) {
				String name = String.valueOf(ch);
				while(this.nextChar()) {
					if(Character.isDigit(ch)) {
						name += ch;
					}else if(ch == '.') {
						name += ch;
						while(this.nextChar() && Character.isDigit(ch)) {
							name += ch;
						}
						break;
					}else
						break;
				}
				register(name);
				this.isCharLock = true;
			}
		}
	}
	public static String deleteComment(String source)
	{
		StringBuilder dst = new StringBuilder("");
		boolean isLineComment = false;
		boolean isBlockComment = false;
		for(int i = 0; i < source.length(); i++) {
			char ch = source.charAt(i);
			if(isLineComment) {
				if(ch == System.getProperty("line.separator").charAt(0)) {
					isLineComment = false;
				}
			}else if(isBlockComment) {
				if(ch == '/' && source.charAt(i-1) == '*'){
					isBlockComment = false;
				}else if(ch == System.getProperty("line.separator").charAt(0)) {
					dst.append(System.getProperty("line.separator"));
				}
			}else if(ch == '/' && source.charAt(i+1) == '/') {
				isLineComment = true;
			}else if(ch == '/' && source.charAt(i+1) == '*') {
				isBlockComment = true;
			}else
				dst.append(ch);
		}
		return dst.toString();
	}
}
class FCTokens extends LinkedList<FCToken> {
	private static final long serialVersionUID = 1L;

	public FCTokens()
	{
	}
}

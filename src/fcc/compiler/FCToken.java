package fcc.compiler;

import java.util.Hashtable;

class CL {
	public FCTokenKind tKind;
	public FCTokenClass tClass;
	public CL(FCTokenKind i, FCTokenClass j)
	{
		tKind = i;
		tClass = j;
	}
}

public class FCToken {
	public FCTokenKind tKind = null;
	public FCTokenClass tClass = null;
	public int tLine = 0;
	public int tIndex = 0;
	public String tName;
	private static Hashtable<String, CL> keyWords;
	static {
		initHashtable();
	}
	public static void initHashtable()
	{
		keyWords = new Hashtable<String, CL>();
		keyWords.put("+", new CL(FCTokenKind.ADDITION, FCTokenClass.SYMBOL));
		keyWords.put("--", new CL(FCTokenKind.DECREMENT, FCTokenClass.SYMBOL));
		keyWords.put("/", new CL(FCTokenKind.DIVISION, FCTokenClass.SYMBOL));
		keyWords.put("++", new CL(FCTokenKind.INCREMENT, FCTokenClass.SYMBOL));
		keyWords.put("%", new CL(FCTokenKind.MODULO, FCTokenClass.SYMBOL));
		keyWords.put("*", new CL(FCTokenKind.MULTIPLICATION, FCTokenClass.SYMBOL));
		keyWords.put("-", new CL(FCTokenKind.SUBTRACTION, FCTokenClass.SYMBOL));
		
		keyWords.put("+=", new CL(FCTokenKind.ADDTION_ASSIGNMENT, FCTokenClass.SYMBOL));
		keyWords.put("/=", new CL(FCTokenKind.DIVISION_ASSIGNMENT, FCTokenClass.SYMBOL));
		keyWords.put("%=", new CL(FCTokenKind.MODULO_ASSIGNMENT, FCTokenClass.SYMBOL));
		keyWords.put("*=", new CL(FCTokenKind.MULTIPLICATION_ASSIGNMENT, FCTokenClass.SYMBOL));
		keyWords.put("-=", new CL(FCTokenKind.SUBTRACTION_ASSIGNMENT, FCTokenClass.SYMBOL));
		
		keyWords.put("=", new CL(FCTokenKind.ASSIGNMENT, FCTokenClass.SYMBOL));
		keyWords.put("&", new CL(FCTokenKind.BIT_AND, FCTokenClass.SYMBOL));
		keyWords.put("<<", new CL(FCTokenKind.BIT_LEFT_SHIFT, FCTokenClass.SYMBOL));
		keyWords.put("~", new CL(FCTokenKind.BIT_NOT, FCTokenClass.SYMBOL));
		keyWords.put("|", new CL(FCTokenKind.BIT_OR, FCTokenClass.SYMBOL));
		keyWords.put(">>", new CL(FCTokenKind.BIT_RIGHT_SHIFT, FCTokenClass.SYMBOL));
		keyWords.put("^", new CL(FCTokenKind.BIT_XOR, FCTokenClass.SYMBOL));
		
		keyWords.put("&=", new CL(FCTokenKind.BIT_AND_ASSIGNMENT, FCTokenClass.SYMBOL));
		keyWords.put("|=", new CL(FCTokenKind.BIT_OR_ASSIGNMENT, FCTokenClass.SYMBOL));
		keyWords.put("^=", new CL(FCTokenKind.BIT_XOR_ASSIGNMENT, FCTokenClass.SYMBOL));
		
		keyWords.put("==", new CL(FCTokenKind.EQUALITY, FCTokenClass.SYMBOL));
		keyWords.put(">", new CL(FCTokenKind.GREATER_THAN, FCTokenClass.SYMBOL));
		keyWords.put(">=", new CL(FCTokenKind.GREATER_THAN_EQUAL, FCTokenClass.SYMBOL));
		keyWords.put("!=", new CL(FCTokenKind.INEQUAL, FCTokenClass.SYMBOL));
		keyWords.put("<", new CL(FCTokenKind.LESS_THAN, FCTokenClass.SYMBOL));
		keyWords.put("<=", new CL(FCTokenKind.LESS_THAN_EQUAL, FCTokenClass.SYMBOL));
		
		keyWords.put("&&", new CL(FCTokenKind.LOGICAL_AND, FCTokenClass.SYMBOL));
		keyWords.put("!", new CL(FCTokenKind.LOGICAL_NOT, FCTokenClass.SYMBOL));
		keyWords.put("||", new CL(FCTokenKind.LOGICAL_OR, FCTokenClass.SYMBOL));
		
		keyWords.put("[", new CL(FCTokenKind.ARRAY_LEFT, FCTokenClass.SYMBOL));
		keyWords.put("]", new CL(FCTokenKind.ARRAY_RIGHT, FCTokenClass.SYMBOL));
		keyWords.put(",", new CL(FCTokenKind.COMMA, FCTokenClass.SYMBOL));
		keyWords.put(".", new CL(FCTokenKind.DOT, FCTokenClass.SYMBOL));
		keyWords.put("{", new CL(FCTokenKind.OBJECT_LEFT, FCTokenClass.SYMBOL));
		keyWords.put("}", new CL(FCTokenKind.OBJECT_RIGHT, FCTokenClass.SYMBOL));
		keyWords.put("(", new CL(FCTokenKind.PARENT_LEFT, FCTokenClass.SYMBOL));
		keyWords.put(")", new CL(FCTokenKind.PARENT_RIGHT, FCTokenClass.SYMBOL));
		keyWords.put(";", new CL(FCTokenKind.TYPE, FCTokenClass.SYMBOL));
		keyWords.put(";", new CL(FCTokenKind.SEMICOLON, FCTokenClass.SYMBOL));
		
		keyWords.put("break", new CL(FCTokenKind.BREAK, FCTokenClass.KEYWORD));
		keyWords.put("case", new CL(FCTokenKind.CASE, FCTokenClass.KEYWORD));
		keyWords.put("continue", new CL(FCTokenKind.CONTINUE, FCTokenClass.KEYWORD));
		keyWords.put("default", new CL(FCTokenKind.DEFAULT, FCTokenClass.KEYWORD));
		keyWords.put("do", new CL(FCTokenKind.DO, FCTokenClass.KEYWORD));
		keyWords.put("else", new CL(FCTokenKind.ELSE, FCTokenClass.KEYWORD));
		keyWords.put("for", new CL(FCTokenKind.FOR, FCTokenClass.KEYWORD));
		keyWords.put("function", new CL(FCTokenKind.FUNCTION, FCTokenClass.KEYWORD));
		keyWords.put("if", new CL(FCTokenKind.IF, FCTokenClass.KEYWORD));
		keyWords.put("return", new CL(FCTokenKind.RETURN, FCTokenClass.KEYWORD));
		keyWords.put("switch", new CL(FCTokenKind.SWITCH, FCTokenClass.KEYWORD));
		keyWords.put("while", new CL(FCTokenKind.WHILE, FCTokenClass.KEYWORD));
		keyWords.put("true", new CL(FCTokenKind.TRUE, FCTokenClass.KEYWORD));
		keyWords.put("false", new CL(FCTokenKind.FALSE, FCTokenClass.KEYWORD));
		keyWords.put("new", new CL(FCTokenKind.NEW, FCTokenClass.KEYWORD));
		keyWords.put("null", new CL(FCTokenKind.NULL, FCTokenClass.KEYWORD));
		keyWords.put("var", new CL(FCTokenKind.VAR, FCTokenClass.KEYWORD));
		keyWords.put("void", new CL(FCTokenKind.VOID, FCTokenClass.KEYWORD));
		keyWords.put("int", new CL(FCTokenKind.INT, FCTokenClass.KEYWORD));
		keyWords.put("Number", new CL(FCTokenKind.NUMBER, FCTokenClass.KEYWORD));
		keyWords.put("Array", new CL(FCTokenKind.ARRAY, FCTokenClass.KEYWORD));
	}
	public void set(String s, int l, int i)
	{
		this.tName = s;
		this.tLine = l;
		this.tIndex = i;
		if(keyWords.containsKey(s)) {
			CL cl = keyWords.get(s);
			this.tKind = cl.tKind;
			this.tClass = cl.tClass;
			return;
		}
		try {
			Float.parseFloat(s);
			this.tKind = null;
			this.tClass = FCTokenClass.NUMBER;
		}catch(NumberFormatException e) {
			this.tKind = null;
			this.tClass = FCTokenClass.ID;
		}
	}
	public boolean isKeyword()
	{
		return this.tClass == FCTokenClass.KEYWORD;
	}
	public boolean isSymbol()
	{
		return this.tClass == FCTokenClass.SYMBOL;
	}
	public boolean isId()
	{
		return this.tClass == FCTokenClass.ID;
	}
	public boolean isNumber()
	{
		return this.tClass == FCTokenClass.NUMBER;
	}
	public boolean isWhiteSpace()
	{
		return (this.tName.equals("\n") || this.tName.equals(" "));
	}
	public String toString()
	{
		return this.tName + " " + this.tKind + " " + this.tClass + " " + this.tLine + " " + this.tIndex;
	}
	public void trace()
	{
		System.out.println(this.tName + " " + this.tKind + " " + this.tClass + " " + this.tLine + " " + this.tIndex);
	}
}

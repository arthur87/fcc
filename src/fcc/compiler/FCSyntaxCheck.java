package fcc.compiler;

import java.util.ArrayList;
import fcc.Options;

public class FCSyntaxCheck {
	private String fileName;
	private FCTable table;
	private FCTokens tokens;
	private FCTokenStream tokenStream;
	private String source;
	public FCSyntaxCheck(Options options, String source)
	{
		this.fileName = options.sourceFile();
		this.source = source;
		this.table = new FCTable(options.arch());
		syntaxCheck(source);	
	}
	public String source()
	{
		return source;
	}
	public FCTable table()
	{
		return table;
	}
	public FCTokens tokens()
	{
		return tokens;
	}
	public FCTokenStream tokenStream()
	{
		return tokenStream;
	}
	private void syntaxCheck(String source)
	{
		tokenStream = new FCTokenStream(source);
		tokens = tokenStream.getTokens();
		FCToken token;
		/**
		 * �O���[�o���ϐ��錾�̕��@�`�F�b�N�E�O���[�o���ϐ��̃��X�g�A�b�v
		 * �֐��錾�̃��X�g�A�b�v
		 */
		int i;
		for(i = 0; i < tokens.size(); i++) {
			token = tokens.get(i);
			if(token.tKind == FCTokenKind.VAR) {
				token = tokens.get(++i);
				while(true) {
					if(token.tClass != FCTokenClass.ID) syntaxError(token);
					if(!table.addGlobalVar(token.tName)) syntaxError(token);
					token = tokens.get(++i);
					if(token.tKind == FCTokenKind.COMMA) {
						token = tokens.get(++i);
					}else if(token.tKind == FCTokenKind.SEMICOLON) {
						break;
					}else syntaxError(token);
				}
			}else if(token.tKind == FCTokenKind.FUNCTION) {
				token = tokens.get(++i);
				if(token.tClass != FCTokenClass.ID) syntaxError(token);
				String name = token.tName;
				int depth = 0;
				ArrayList<String> args = new ArrayList<String>(); /* ���� */
				while(true) {
					token = tokens.get(++i);
					if(token.tClass == FCTokenClass.ID) args.add(token.tName);
					if(token.tKind == FCTokenKind.PARENT_RIGHT) break;
				}
				while(true) {
					token = tokens.get(++i);
					if(token.tKind == FCTokenKind.OBJECT_LEFT) depth++;
					if(token.tKind == FCTokenKind.OBJECT_RIGHT) {
						depth--;
						if(depth == 0) break;
					}
				}
				if(table.isFunction(name)) syntaxError(token);
				table.addFunction(name, args);
			}else
				syntaxError(token);
		}
		/**
		 * ���[�J���ϐ��錾�̕��@�`�F�b�N
		 */
		for(i = 0; i < tokens.size(); i++) {
			token = tokens.get(i);
			if(token.tKind == FCTokenKind.FUNCTION) {
				String fname = tokens.get(i+1).tName;
				token = tokens.get(i+=2);
				/* �֐��̈��� */
				while(true) {
					token = tokens.get(++i);
					if(token.tClass == FCTokenClass.ID) {
						if(!table.addLocalVar(fname, token.tName)) syntaxError(token);
					}
					if(token.tKind == FCTokenKind.PARENT_RIGHT) break;
				}
				/* �֐��̒��g */
				int depth = 0;
				boolean isVoid = true; /* void�^�� */
				while(true) {
					token = tokens.get(++i);
					if(token.tKind == FCTokenKind.OBJECT_LEFT) depth++;
					if(token.tKind == FCTokenKind.OBJECT_RIGHT) {
						depth--;
						if(depth == 0) {
							//System.out.println(fname + "" + isVoid);
							table.addFuncType(fname, isVoid);
							break;
						}
					}
					/* �Ԃ�l�����邩 */
					if(token.tKind == FCTokenKind.RETURN) {
						token = tokens.get(++i);
						if(isVoid && token.tKind != FCTokenKind.SEMICOLON) {
							isVoid = false;
						}
					}
					
					/* ���[�J���ϐ��錾�̕��@�`�F�b�N */
					if(token.tKind == FCTokenKind.VAR) {
						token = tokens.get(++i);
						while(true) {
							if(token.tClass != FCTokenClass.ID) syntaxError(token);
							if(!table.addLocalVar(fname, token.tName)) syntaxError(token);
							token = tokens.get(++i);
							if(token.tKind == FCTokenKind.COMMA) {
								token = tokens.get(++i);
							}else if(token.tKind == FCTokenKind.SEMICOLON) {
								break;
							}else syntaxError(token);
						}
					}
					/* ���[�J���ϐ����錾�̃`�F�b�N */
					if(token.tClass == FCTokenClass.ID) {
						String name = token.tName;
						if(name.indexOf(".") < 0) { // API�łȂ��Ƃ�
							token = tokens.get(++i);
							if(token.tKind == FCTokenKind.PARENT_LEFT) {
								// �֐��Ăяo���̂Ƃ� �֐��錾�ƈ����̐��̃`�F�b�N
								int pdepth = 1;
								StringBuilder sb = new StringBuilder("");
								while(true) {
									token = tokens.get(++i);
									if(token.tClass == FCTokenClass.ID) {
										if(!table.isLocalVar(fname, name) && !table.isGlobalVar(token.tName)) syntaxError(token);
									}
									if(token.tKind == FCTokenKind.PARENT_LEFT) pdepth++;
									if(token.tKind == FCTokenKind.PARENT_RIGHT) {
										pdepth--;
										if(pdepth == 0) break;
									}
									sb.append(token.tName);
								}
								int pargc = 0;
								if(sb.toString().equals("")) pargc = 0;
								else pargc = sb.toString().split(",").length;
								if(!table.isFunction(name)) syntaxError(token);
								if(pargc != table.getFunctionArgc(name)) syntaxError(token);
							}else {
								// ���[�J���ϐ��܂��̓O���[�o���ϐ��̂Ƃ�
								if(!table.isLocalVar(fname, name) && !table.isGlobalVar(name)) syntaxError(token);
							}
						}else { // API�̂Ƃ�
							token = tokens.get(++i);
							if(token.tKind == FCTokenKind.PARENT_LEFT) {
								int pdepth = 1;
								StringBuilder sb = new StringBuilder("");
								while(true) {
									token = tokens.get(++i);
									if(token.tClass == FCTokenClass.ID) {
										if(!table.isLocalVar(fname,token.tName) && !table.isGlobalVar(token.tName)) syntaxError(token);
									}
									if(token.tKind == FCTokenKind.PARENT_LEFT) pdepth++;
									if(token.tKind == FCTokenKind.PARENT_RIGHT) {
										pdepth--;
										if(pdepth == 0) break;
									}
									sb.append(token.tName);
								}
								int pargc = 0;
								if(sb.toString().equals("")) pargc = 0;
								else pargc = sb.toString().split(",").length;
								if(!table.isAPI(name, pargc)) syntaxError(token);
							}
						}
					}
				}
			}
		}
	}
	private void syntaxError(FCToken token)
	{
		new FCError(fileName, token.tLine);
	}
}


package fcc.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class FCTable {
	@SuppressWarnings("rawtypes")
	private HashMap<String, ArrayList> funcs = new HashMap<String, ArrayList>();
	private HashMap<String, Boolean> funcTypes = new HashMap<String, Boolean>();
	private HashSet<String> gVars = new HashSet<String>();
	private HashSet<String> lVars = new HashSet<String>();
	private String apiList = null;
	public FCTable(String arch)
	{
		/* アーキテクチャ別のAPIを登録する */
		HashMap<String, String> apis = new HashMap<String, String>();
		apis.put("arduino", "Loop.start@1 IO.digitalWrite@2 IO.digitalRead@1 IO.analogWrite@2 IO.analogRead@1" +
				"Serial.begin@1 Serial.end@0 Serial.available@0 Serial.read@0 Serial.write@1 Serial.flush@0");
		apis.put("tinyos", "Loop.start@1 Read.read@0 Send.send@5 Read.data@0 Leds.led@2" +
				"Raceive.data1@0  Raceive.data2@0  Raceive.data3@0  Raceive.data4@0 Receive.data5@0 Received.id@0");
		if(apis.containsKey(arch)) apiList = apis.get(arch);
		else apiList = apis.get("arduino");
	}
	/**
	 * グローバル変数の登録
	 * @param s
	 * @return
	 */
	public boolean addGlobalVar(String s)
	{
		return gVars.add(s);
	}
	/**
	 * 登録済みのグローバル変数か
	 * @param s
	 * @return
	 */
	public boolean isGlobalVar(String s)
	{
		return gVars.contains(s);
	}
	public ArrayList<String> getGlobalVar()
	{
		ArrayList<String> v = new ArrayList<String>();
		Iterator<String> it = gVars.iterator();
		while(it.hasNext()) {
			v.add(it.next());
		}
		return v;
	}
	/**
	 * ローカル変数の登録（関数名@変数名）
	 * @param fname
	 * @param name
	 * @return
	 */
	public boolean addLocalVar(String fname, String name)
	{
		return lVars.add(fname + "@" + name);
	}
	/**
	 * 登録済みのローカル変数か
	 * @param fname
	 * @param name
	 * @return
	 */
	public boolean isLocalVar(String fname, String name)
	{
		return lVars.contains(fname + "@" + name);
	}
	public ArrayList<String> getLocalVar(String fname)
	{
		HashSet<String> vars = lVars;
		ArrayList<String> v = new ArrayList<String>();
		ArrayList<String> s = this.getFunction(fname);
		Iterator<String> it = vars.iterator();
		/* 関数内の変数をリストアップ */
		while(it.hasNext()) {
			String name = it.next();
			if(name.indexOf(fname+"@") > -1) {
				v.add(name.replaceFirst(fname+"@", ""));
			}
		}
		for(int i = 0; i < s.size(); i++) {
			v.remove(s.get(i));
		}
		return v;
	}
	/**
	 * 関数の登録（関数名・引数の文字列）
	 * @param name
	 * @param args
	 */
	public void addFunction(String name, ArrayList<String> args)
	{
		funcs.put(name, args);
	}
	/**
	 * 関数の取得
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getFunction(String name)
	{
		return funcs.get(name);
	}
	/**
	 * 登録済みの関数か
	 * @param s
	 * @return
	 */
	public boolean isFunction(String s)
	{
		return funcs.containsKey(s);
	}
	/**
	 * 引数の個数を取得する
	 * @param name
	 * @return
	 */
	public int getFunctionArgc(String name)
	{
		if(!funcs.containsKey(name)) return 0;
		return funcs.get(name).size();
	}
	/**
	 * 関数の型を登録する
	 * @param s
	 * @param b
	 * @return
	 */
	public void addFuncType(String s, boolean b)
	{
		funcTypes.put(s, b);
	}
	public boolean isFuncType(String s)
	{
		return funcTypes.get(s);
	}
	/**
	 * 登録済みのAPIか
	 * @param s
	 * @param i
	 * @return
	 */
	public boolean isAPI(String s, int i)
	{
		return (apiList.indexOf(s+"@"+i) > -1);
	}
}

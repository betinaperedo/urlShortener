package coop.tecso.main.db;

import java.util.Map;

import com.google.common.collect.Maps;

/*
*Betina
*/

public class PersistentMap {
	private static PersistentMap dataBase;
	public final Map<String, String> urlMap = Maps.newHashMap(); 
	
	public PersistentMap() {
		
	}
	
	public static PersistentMap getInstance(){
		if(dataBase==null)
			dataBase= new PersistentMap();
		
		return dataBase;
	}
	
	public void insert(String key, String value){
		urlMap.put(key, value);
	}
	public String select(String key){
		return urlMap.get(key);
	}
	public void delete(String key){
		 urlMap.remove(key);
	}

}

package coop.tecso.main.db;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mongodb.DB;
import com.mongodb.MongoClient;

import coop.tecso.main.model.UrlShortener;

/*
*Betina
*/

public class PersistentMap {
	private static PersistentMap dataBase;
	public final Map<String, UrlShortener> urlMap = Maps.newHashMap(); 
	
	public PersistentMap() {
		
	}
	
	public static PersistentMap getInstance() throws UnknownHostException{
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		DB db = mongoClient.getDB("database name");
		if(dataBase==null)
			dataBase= new PersistentMap();
		
		return dataBase;
	}
	
	public void insert(String key, String value){
		UrlShortener urlShortener = new UrlShortener(key,value);
		urlMap.put(key, urlShortener);
	}
	public String selectLongUrl(String key){
		UrlShortener urlShortener=urlMap.get(key);
		return urlShortener.getLongtUrl();
	}
	public void delete(String key){
		 urlMap.remove(key);
	}

	public List<UrlShortener> findAll(){
	  return ImmutableList.copyOf(urlMap.values());
	}

}

package coop.tecso.main.dao;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import coop.tecso.main.model.ShortUrl;

/*
*Betina
*/

public class MapShortUrlDaoImpl implements ShortUrlDAO {
	private static MapShortUrlDaoImpl dataBase;
	public final Map<String, ShortUrl> urlMap = Maps.newHashMap(); 
	
	public MapShortUrlDaoImpl() {
		
	}
	
	public static MapShortUrlDaoImpl getInstance() {		
		if(dataBase==null)
			dataBase= new MapShortUrlDaoImpl();		
		return dataBase;
	}
	
	public void save(ShortUrl urlShortener){		
		urlMap.put(urlShortener.getShortUrl(), urlShortener);
	}
	public ShortUrl get(String key){
		
		return urlMap.get(key);
	}
	public void delete(String key){
		 urlMap.remove(key);
	}

	public List<ShortUrl> findAll(){
	  return ImmutableList.copyOf(urlMap.values());
	}

	

}

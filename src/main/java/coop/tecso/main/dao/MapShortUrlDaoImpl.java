package coop.tecso.main.dao;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import coop.tecso.main.model.ShortUrl;

/*
*Betina
*/

public class MapShortUrlDaoImpl implements ShortUrlDAO {

	public final Map<String, ShortUrl> urlMap = Maps.newHashMap(); 
	
	public void save(ShortUrl urlShortener) {		
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

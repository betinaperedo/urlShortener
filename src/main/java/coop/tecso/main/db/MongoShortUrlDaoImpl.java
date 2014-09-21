package coop.tecso.main.db;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import coop.tecso.main.model.ShortUrl;

public class MongoShortUrlDaoImpl implements ShortUrlDAO{
	private static MongoShortUrlDaoImpl dataBase;
	private static MongoClient mongoClient;
	private static DB dbMongo;
	DBCollection tab_shorturl;
	public MongoShortUrlDaoImpl()  throws UnknownHostException {
		mongoClient = new MongoClient( "localhost" , 27017 );
		dbMongo = mongoClient.getDB("urlshortenerdb");
		tab_shorturl = dbMongo.getCollection("tab_shorturl");
	}
	
	public static MongoShortUrlDaoImpl getInstance() {		
		if(dataBase==null){
			try {
				dataBase= new MongoShortUrlDaoImpl();
			} catch (UnknownHostException e) {				
				e.printStackTrace();
			}
		}		
		return dataBase;
	}

	@Override
	public void save(ShortUrl shortUrl) {
		BasicDBObject document = new BasicDBObject();		
		document.put("shortUrl", shortUrl.getShortUrl());
		document.put("longUrl", shortUrl.getLongtUrl());		
		tab_shorturl.insert(document);
		
	}

	@Override
	public ShortUrl get(String id) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("shortUrl", id);
		DBCursor cursor = tab_shorturl.find(searchQuery);
		if (cursor.hasNext()) {
			DBObject dbObject=cursor.next();	
			String shortUrlStr=(String) dbObject.get("shortUrl");
			String longUrlStr=(String) dbObject.get("longUrl");
			ShortUrl shortUrl = new ShortUrl(shortUrlStr, longUrlStr);
			return shortUrl;
			
		}
		return null;
	}

	@Override
	public void delete(String id) {
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("shortUrl", id);
		tab_shorturl.remove(searchQuery);
	}

	@Override
	public List<ShortUrl> findAll() {
		BasicDBObject searchQuery = new BasicDBObject();		
		DBCursor cursor = tab_shorturl.find(searchQuery);	
		List<ShortUrl> list=new ArrayList<ShortUrl>();
		while (cursor.hasNext()) {
			DBObject dbObject=cursor.next();	
			String shortUrlStr=(String) dbObject.get("shortUrl");
			String longUrlStr=(String) dbObject.get("longUrl");
			ShortUrl shortUrl = new ShortUrl(shortUrlStr, longUrlStr);
			list.add(shortUrl);
			
		}
		return list;
	}

	
	
	
}

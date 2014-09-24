package urlshortener.src.test.java.coop.tecso.main.db;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import coop.tecso.main.dao.MapShortUrlDaoImpl;
import coop.tecso.main.dao.MongoShortUrlDaoImpl;
import coop.tecso.main.dao.ShortUrlDAO;
import coop.tecso.main.model.ShortUrl;

public class MapShortUrlDaoTest  {
	
	public ShortUrlDAO mapDb = MapShortUrlDaoImpl.getInstance();
	public ShortUrlDAO mongoDb = MongoShortUrlDaoImpl.getInstance();
	
	@Test
	public void testInsertMapDb(){
		ShortUrl shortUrl = new ShortUrl( ShortUrl.getShortUrlSha1Implementation("www.google.com"),"www.google.com");
		mapDb.save(shortUrl);
		assertNotNull(mapDb.findAll());
		ShortUrl shortUrlDb = mapDb.get(ShortUrl.getShortUrlSha1Implementation("www.google.com"));
		assertSame(shortUrl.getShortUrl(), shortUrlDb.getShortUrl());
		assertSame(shortUrl.getLongtUrl(), shortUrlDb.getLongtUrl());        
		
	}

}

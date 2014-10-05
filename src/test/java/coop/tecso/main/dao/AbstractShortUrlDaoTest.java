package coop.tecso.main.dao;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import coop.tecso.main.model.shortUrl.ShortUrl;

public abstract class AbstractShortUrlDaoTest {
	
	protected ShortUrlDAO shortUrlDAO;
	
	@Test
	public void testSave(){
		String longUrlStr ="www.google.com";
		String shortUrlStr = ShortUrl.getShortUrlSha1Implementation(longUrlStr);
		ShortUrl shortUrl= new ShortUrl(shortUrlStr, longUrlStr);
		assertEquals(shortUrlStr, "2LmfaL");
		
		shortUrlDAO.save(shortUrl);
		assertEquals(1, shortUrlDAO.findAll().size());
		assertEquals(shortUrl, shortUrlDAO.findAll().get(0));
		assertEquals(longUrlStr,shortUrlDAO.get(shortUrlStr).getLongUrl());
		assertEquals(shortUrlStr,shortUrlDAO.get(shortUrlStr).getShortUrl());		
	}
	
	@Test
	public void testDelete(){
		ShortUrl shortUrl= ShortUrl.ofUrl("www.google.com");
		
		shortUrlDAO.save(shortUrl);
		assertEquals(1, shortUrlDAO.findAll().size());
		
		shortUrlDAO.delete(shortUrl.getShortUrl());
		assertEquals(0, shortUrlDAO.findAll().size());		
	}
	
	@Test
	public void testDeleteMissingUrlShouldNotThrowException(){
		shortUrlDAO.delete("missingUrl");
	}
	
	@Test
	public void testFindAll(){
		
		assertEquals(0, shortUrlDAO.findAll().size());
		
		String longUrlStr ="www.google.com";
		ShortUrl shortUrl= ShortUrl.ofUrl(longUrlStr);
		shortUrlDAO.save(shortUrl);
		
		assertEquals(1, shortUrlDAO.findAll().size());
		
		longUrlStr ="www.facebook.com";
		shortUrl= ShortUrl.ofUrl(longUrlStr);
		shortUrlDAO.save(shortUrl);
		
		assertEquals(2, shortUrlDAO.findAll().size());
		
		longUrlStr ="www.twitter.com";
		shortUrl= ShortUrl.ofUrl(longUrlStr);
		shortUrlDAO.save(shortUrl);
		
		assertEquals(3, shortUrlDAO.findAll().size());

	}
	
}

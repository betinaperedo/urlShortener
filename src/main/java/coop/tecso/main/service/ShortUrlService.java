package coop.tecso.main.service;

import java.util.List;

import coop.tecso.main.model.shortUrl.ShortUrl;

public interface ShortUrlService {
	public void save(ShortUrl shortUrl);
	public ShortUrl get(String shortUrlString);
	public void delete(String id);
	public List<ShortUrl> findAll();
	public String createAndSave(String urlinput);	

}

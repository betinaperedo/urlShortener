package coop.tecso.main.db;

import java.util.List;

import coop.tecso.main.model.ShortUrl;

public interface ShortUrlDAO {
	public void save(ShortUrl shortUrl);
	public ShortUrl get(String shortUrlString);
	public void delete(String id);
	public List<ShortUrl> findAll();	

}

package coop.tecso.main.service;

import java.util.List;

import javax.annotation.Resource;

import coop.tecso.main.dao.MapShortUrlDaoImpl;
import coop.tecso.main.dao.ShortUrlDAO;
import coop.tecso.main.model.ShortUrl;

public class ShortUrlServiceImpl implements ShortUrlService{

	
	ShortUrlDAO shortUrlDAO=  MapShortUrlDaoImpl.getInstance();
	
	@Override
	public void save(ShortUrl shortUrl) {
		shortUrlDAO.save(shortUrl);
		
	}

	@Override
	public ShortUrl get(String shortUrlString) {
		return shortUrlDAO.get(shortUrlString);
	}

	@Override
	public void delete(String id) {
		shortUrlDAO.delete(id);
		
	}

	@Override
	public List<ShortUrl> findAll() {		
		return shortUrlDAO.findAll();
	}

	@Override
	public String createAndSave(String longUrlinput) {
		String shortUrlStr = ShortUrl.getShortUrlSha1Implementation(longUrlinput);
		ShortUrl shortUrl = new ShortUrl(shortUrlStr,longUrlinput);
		this.save(shortUrl);
		return shortUrlStr;
		
	}

}

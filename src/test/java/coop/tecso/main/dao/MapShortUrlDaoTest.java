package coop.tecso.main.dao;

import org.junit.Before;

public class MapShortUrlDaoTest extends AbstractShortUrlDaoTest {
	
	@Before
	public void setUp(){
		this.shortUrlDAO= new MapShortUrlDaoImpl();
	}

}

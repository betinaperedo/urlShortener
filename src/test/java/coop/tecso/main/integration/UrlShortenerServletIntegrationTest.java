package coop.tecso.main.integration;

import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.mockito.Mockito;

import com.mongodb.MongoClient;

import coop.tecso.main.dao.AbstractShortUrlDaoTest;
import coop.tecso.main.dao.MongoShortUrlDaoImpl;
import coop.tecso.main.dao.ShortUrlDAO;
import coop.tecso.main.service.ShortUrlService;
import coop.tecso.main.service.ShortUrlServiceImpl;

public class UrlShortenerServletIntegrationTest {
	MongoClient mongoClient;
	String dbName;
	@Rule public TestName name = new TestName();
	ShortUrlDAO shortUrlDAO;
	ShortUrlService shortUrlService;
	
	@Before
	public void setUp(){
		HttpServletRequest removeRequest = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse removeResponse = Mockito.mock(HttpServletResponse.class);
		try {
			mongoClient = new MongoClient("localhost", 27017);
			dbName =name.getMethodName();
			String collecion ="tab_shorturl";
			this.shortUrlDAO= new MongoShortUrlDaoImpl(mongoClient,dbName,collecion);
			this.shortUrlService= new ShortUrlServiceImpl(shortUrlDAO);
		}catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}	
	
	@After
	public void dropDataBase(){
		mongoClient.dropDatabase(dbName);
	}
	
	
		

}

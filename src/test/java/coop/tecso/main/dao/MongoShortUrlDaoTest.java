package coop.tecso.main.dao;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.mongodb.MongoClient;

public class MongoShortUrlDaoTest extends AbstractShortUrlDaoTest{
	MongoClient mongoClient;
	String dbName;
	@Rule public TestName name = new TestName();
	
	@Before
	public void setUp(){
		
		try {
			mongoClient = new MongoClient("localhost", 27017);
			dbName =name.getMethodName();
			String collecion ="tab_shorturl";
			this.shortUrlDAO= new MongoShortUrlDaoImpl(mongoClient,dbName,collecion);
		}catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}	
	
	@After
	public void dropDataBase(){
		mongoClient.dropDatabase(dbName);
	}
		

}

package coop.tecso.main.servlet;

import java.net.UnknownHostException;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.mongodb.MongoClient;

import coop.tecso.main.dao.MongoShortUrlDaoImpl;
import coop.tecso.main.dao.ShortUrlDAO;
import coop.tecso.main.service.ShortUrlService;
import coop.tecso.main.service.ShortUrlServiceImpl;

public class UrlShortenerModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new UrlShortenerServletModule());
		bind(UrlShortenerServlet.class).in(Singleton.class);
		bind(ShortUrlService.class).to(ShortUrlServiceImpl.class).in(Singleton.class);
		
		bindConstant().annotatedWith(Names.named("hostname")).to("localhost");
//		bind(String.class).annotatedWith(Names.named("hostname")).toInstance("localhost");
		
		
	}
	
	@Provides @Singleton
	ShortUrlDAO provideShortUrlDAO(){		
		try {
			return new MongoShortUrlDaoImpl(new MongoClient("localhost", 27017),"us_db","tab_shorturl");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		
	}

}

package coop.tecso.main.servlet;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;

import coop.tecso.main.dao.MongoShortUrlDaoImpl;
import coop.tecso.main.dao.ShortUrlDAO;
import coop.tecso.main.model.shortUrl.ShortUrl;
import coop.tecso.main.service.ShortUrlService;
import coop.tecso.main.service.ShortUrlServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UrlShortenerServletIntegrationTest {
	
	@Rule public TestName name = new TestName();
	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	MongoClient mongoClient;
	String dbName;
	ShortUrlService shortUrlService;
	UrlShortenerServlet urlShortenerServlet;
	
	@Before
	public void setUp(){
		
		try {
			
			mongoClient = new MongoClient("localhost", 27017);
			dbName = name.getMethodName();
			
			String collecion = "tab_shorturl";
			ShortUrlDAO shortUrlDAO = new MongoShortUrlDaoImpl(mongoClient,dbName,collecion);
			
			this.shortUrlService = new ShortUrlServiceImpl(shortUrlDAO);
			urlShortenerServlet = new UrlShortenerServlet(shortUrlService);
			
		}catch (UnknownHostException e) {
			
			throw new RuntimeException(e);
			
		}
	}	
	
	@After
	public void dropDataBase(){
		
		mongoClient.dropDatabase(dbName);
		
	}
	
	
	public void doCreate(String urlinput) throws IOException, ServletException {
		
		HttpServletRequest request=Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		Integer size = shortUrlService.findAll().size();
		StringWriter sw = new StringWriter();
		PrintWriter pw  = new PrintWriter(sw);
		
		when(request.getParameter("urlinput")).thenReturn(urlinput);
		when(request.getServletPath()).thenReturn("/create");
		when(response.getWriter()).thenReturn(pw);		
		
		urlShortenerServlet.doPost(request, response);

		verify(request).getParameter("urlinput");
		verify(request).getServletPath();
	
		assertEquals(sw.getBuffer().toString().trim(), ShortUrl.getShortUrlSha1Implementation(urlinput));	
		// Esta assertion es muy laxa. Comprar el estado inicial y final del servicio y no 
		// el tamaño.
		assertEquals(size+1, shortUrlService.findAll().size());
		assertEquals(urlinput, shortUrlService.get(ShortUrl.getShortUrlSha1Implementation(urlinput)).getLongUrl());	
	}
		
	public void doRemove(String urlinput) throws IOException, ServletException{
		
		Integer size = shortUrlService.findAll().size();		
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		when(request.getParameter("urlinput"))
		    .thenReturn(ShortUrl.getShortUrlSha1Implementation(urlinput));
		when(request.getServletPath()).thenReturn("/remove");								

		urlShortenerServlet.doPost(request, response);

		verify(request).getParameter("urlinput");
		verify(request).getServletPath();

		assertEquals(size-1, shortUrlService.findAll().size());
	}

	private void doList() throws IOException, ServletException{
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
		
		when(request.getServletPath()).thenReturn("/list");
		when(response.getWriter()).thenReturn(pw);
						
		urlShortenerServlet.doGet(request, response);
		
		Gson gson = new GsonBuilder().create();
		String urlListJson =gson.toJson(shortUrlService.findAll());		
		
		verify(request).getServletPath();
		verify(response).getWriter();
		verify(response).setContentType("application/json");
		verify(response).setCharacterEncoding("UTF-8");
		
		assertEquals(sw.getBuffer().toString().trim(), urlListJson);
	}
	
	@Test
	public void doCreateRemoveListTest() throws IOException, ServletException {
		doList();
		doCreate("www.google.com");
		doCreate("www.facebook.com");
		doCreate("www.twitter.com");				
		doRemove("www.twitter.com");
		doList();
	}
}

package coop.tecso.main.servlet;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import coop.tecso.main.model.ShortUrl;
import coop.tecso.main.service.ShortUrlService;

public class UrlShortenerServletUTest {
	
	HttpServletRequest request;
	HttpServletResponse response;
	ShortUrlService shortUrlService;
	UrlShortenerServlet urlShortenerServlet;
	@Before
	public void setUp(){
		request=Mockito.mock(HttpServletRequest.class);
		response=Mockito.mock(HttpServletResponse.class);
		shortUrlService=Mockito.mock(ShortUrlService.class);
		urlShortenerServlet = new UrlShortenerServlet(shortUrlService);
	}	
	
	@After
	public void dropDataBase(){
	
	}

	@Test
	public void getList() throws IOException, ServletException{
		
		StringWriter sw = new StringWriter();
		PrintWriter pw  =new PrintWriter(sw);


		Mockito.when(request.getServletPath()).thenReturn("/list");
		Mockito.when(response.getWriter()).thenReturn(pw);
						
		urlShortenerServlet.doGet(request, response);
		
		Gson gson = new GsonBuilder().create();
		String urlListJson =gson.toJson(shortUrlService.findAll());		
		
		Mockito.verify(request).getServletPath();
		Mockito.verify(response).getWriter();
		assertEquals(sw.getBuffer().toString().trim(), urlListJson);
	}
	
	@Test
	public void postCreate() throws IOException, ServletException{
		StringWriter sw = new StringWriter();
		PrintWriter pw  =new PrintWriter(sw);
		String urlinput="www.google.com";
		
		Mockito.when(request.getParameter("urlinput")).thenReturn(urlinput);
		Mockito.when(request.getServletPath()).thenReturn("/create");
		Mockito.when(response.getWriter()).thenReturn(pw);
		Mockito.when(shortUrlService.createAndSave(urlinput)).thenReturn(ShortUrl.getShortUrlSha1Implementation(urlinput));				
		
		urlShortenerServlet.doPost(request, response);
		
		Mockito.verify(request).getParameter("urlinput");
		Mockito.verify(request).getServletPath();
		Mockito.verify(shortUrlService).createAndSave(urlinput);						
		assertEquals(sw.getBuffer().toString().trim(), ShortUrl.getShortUrlSha1Implementation(urlinput));						
	}
	
	@Test
	public void postRemove() throws IOException, ServletException{
		String urlinput="www.google.com";						
		shortUrlService.createAndSave(urlinput);
		
		Mockito.when(request.getParameter("urlinput")).thenReturn(urlinput);
		Mockito.when(request.getServletPath()).thenReturn("/remove");
				
		urlShortenerServlet.doPost(request, response);
		
		Mockito.verify(request).getParameter("urlinput");	
		Mockito.verify(request).getServletPath();	
	}
	
	
	

}

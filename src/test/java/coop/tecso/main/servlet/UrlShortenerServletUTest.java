package coop.tecso.main.servlet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import coop.tecso.main.model.shortUrl.ShortUrl;
import coop.tecso.main.service.ShortUrlService;


@RunWith(MockitoJUnitRunner.class)
public class UrlShortenerServletUTest {
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private ShortUrlService shortUrlService;
	@Mock
	private UrlShortenerServlet urlShortenerServlet;
	
	@Before
	public void setUp(){
		urlShortenerServlet = new UrlShortenerServlet(shortUrlService);
	}
	
	
	@Test
	public void getList() throws IOException, ServletException {

		StringWriter sw = new StringWriter();
		PrintWriter pw  =new PrintWriter(sw);

		ImmutableList<ShortUrl> urls = ImmutableList.of(
				new ShortUrl(ShortUrl.getShortUrlSha1Implementation("www.google.com"),"www.google.com" ),
				new ShortUrl( ShortUrl.getShortUrlSha1Implementation("www.facebook.com"),"www.facebook.com"));

		when(request.getServletPath()).thenReturn("/list");
		when(response.getWriter()).thenReturn(pw);
		when(shortUrlService.findAll()).thenReturn(urls);
		
		urlShortenerServlet.doGet(request, response);

		Gson gson = new GsonBuilder().create();		
		assertEquals(gson.toJson(urls), sw.getBuffer().toString().trim());
		verify(response).setContentType("application/json");
		verify(response).setCharacterEncoding("UTF-8");
	}

	@Test
	public void postCreate() throws IOException, ServletException{
		
		StringWriter sw = new StringWriter();
		PrintWriter pw  = new PrintWriter(sw);
		String urlinput = "www.google.com";
		
		when(request.getParameter("urlinput")).thenReturn(urlinput);
		when(request.getServletPath()).thenReturn("/create");
		when(response.getWriter()).thenReturn(pw);
		when(shortUrlService.createAndSave(urlinput)).thenReturn(ShortUrl.getShortUrlSha1Implementation(urlinput));				
		
		urlShortenerServlet.doPost(request, response);
		
		verify(request).getParameter("urlinput");
		verify(request).getServletPath();
		verify(shortUrlService).createAndSave(urlinput);						
		assertEquals(sw.getBuffer().toString().trim(), ShortUrl.getShortUrlSha1Implementation(urlinput));						
	}
	
	@Test
	public void postRemove() throws IOException, ServletException{
		
		String input = ShortUrl.getShortUrlSha1Implementation("www.google.com");								
		
		when(request.getParameter("urlinput")).thenReturn(input);
		when(request.getServletPath()).thenReturn("/remove");
				
		urlShortenerServlet.doPost(request, response);
		
		verify(request).getParameter("urlinput");	
		verify(request).getServletPath();	
		verify(shortUrlService).delete(input);
		
	}
}
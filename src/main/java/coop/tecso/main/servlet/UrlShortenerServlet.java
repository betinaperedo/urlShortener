package coop.tecso.main.servlet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

import coop.tecso.main.model.shortUrl.ShortUrl;
import coop.tecso.main.service.ShortUrlService;

//@WebServlet(urlPatterns = { "/*" })
public class UrlShortenerServlet extends HttpServlet {

	public static final String CONTENT_URL = "/urlshortener/";
	public static final String REMOVE_URL = "/remove";
	public static final String CREATE_URL = "/create";
	public static final String INDEX_PATH = "/index.jsp";
	public static final String LOGIN_PATH = "/signin";
	public static final String LIST_URL = "/list";
	public static final String INDEX_PAGE = "index.jsp";
	public static final String LOGIN_PAGE = "/signin.jsp";
	public static final String INTERNAL_ERROR = "Missing urlinput parameter";
	public static final String URL_PARAMETER = "urlinput";
	public static final String STATIC_CONTENT = "/static";

	public final ShortUrlService shortUrlService;

	@Inject	
	public UrlShortenerServlet(ShortUrlService shortUrlService) {
		this.shortUrlService = shortUrlService;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String servletPath = req.getServletPath();
		if(servletPath.equals(LOGIN_PATH)){
			String email=URLDecoder.decode(req.getParameter("email"), Charsets.UTF_8.name());
			String pass=URLDecoder.decode(req.getParameter("password"), Charsets.UTF_8.name());
			checkArgument(!Strings.isNullOrEmpty(email), INTERNAL_ERROR);
			checkArgument(!Strings.isNullOrEmpty(pass), INTERNAL_ERROR);
			if(isValidUser(email,pass)){
				Cookie cookie = new Cookie("AID", email);
				resp.addCookie(cookie);
				resp.getWriter().write("1");
			}else{
				resp.getWriter().write("0");
			}
			
		}else{
			String urlinput = URLDecoder.decode(req.getParameter(URL_PARAMETER), Charsets.UTF_8.name());
			checkArgument(!Strings.isNullOrEmpty(urlinput), INTERNAL_ERROR);
			
			if (servletPath.equals(CREATE_URL)) {	
				String shortUrlStr=shortUrlService.createAndSave(urlinput);			
				resp.getWriter().append(shortUrlStr);
			}else if (servletPath.equals(REMOVE_URL)) {			
				shortUrlService.delete(urlinput);			
			}
		}
		
	}

	private boolean isValidUser(String email, String pass) {
		// TODO Auto-generated method stub
		if(email.contains("@"))
			return true;
		else
			return false;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String servletPath =req.getServletPath();
		if (servletPath.equals(LOGIN_PAGE)) {
			//cargo el index
			ByteStreams.copy(
					req.getServletContext().getResourceAsStream(LOGIN_PAGE),
					resp.getOutputStream());
		}
		else if (servletPath.equals(INDEX_PATH)) {
			//cargo el index
			ByteStreams.copy(
					req.getServletContext().getResourceAsStream(INDEX_PAGE),
					resp.getOutputStream());
		} else if (servletPath.startsWith("/img")) {
			ByteStreams.copy(
					req.getServletContext().getResourceAsStream(req.getServletPath()),
					resp.getOutputStream());
		}
		else if (servletPath.startsWith(LIST_URL)) {
			Gson gson = new GsonBuilder().create();
			String urlListJson =gson.toJson(shortUrlService.findAll());
			resp.setContentType("application/json");
			resp.setCharacterEncoding(Charsets.UTF_8.name());			
			resp.getWriter().write(urlListJson);
			
		} 
		else if(servletPath.startsWith(STATIC_CONTENT)){
			ByteStreams.copy(
					req.getServletContext().getResourceAsStream(req.getServletPath()),
					resp.getOutputStream());
		}
		else {
			ShortUrl shortUrl =shortUrlService.get(req.getServletPath().substring(1));
			if(shortUrl!=null){
				String redirectUrl = shortUrl.getLongUrl();			
				if (!redirectUrl.startsWith("http")) {
					redirectUrl = "http://" + redirectUrl;
				}
				resp.sendRedirect(redirectUrl);
			}
			
		}
	}
}
package coop.tecso.main.servlet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import coop.tecso.main.dao.MapShortUrlDaoImpl;
import coop.tecso.main.dao.MongoShortUrlDaoImpl;
import coop.tecso.main.dao.ShortUrlDAO;
import coop.tecso.main.model.ShortUrl;
import coop.tecso.main.service.ShortUrlService;
import coop.tecso.main.service.ShortUrlServiceImpl;

@WebServlet(urlPatterns = { "/*" })
public class UrlShortenerServlet extends HttpServlet {

	public static final String CONTENT_URL = "/urlshortener/";
	public static final String REMOVE_URL = "/remove";
	public static final String CREATE_URL = "/create";
	public static final String INDEX_PATH = "/";
	public static final String LIST_URL = "/list";
	public static final String INDEX_PAGE = "index.jsp";
	public static final String INTERNAL_ERROR = "Missing urlinput parameter";
	public static final String URL_PARAMETER = "urlinput";
	

	
	public ShortUrlService shortUrlService = new ShortUrlServiceImpl();
	public int count;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String urlinput = URLDecoder.decode(req.getParameter(URL_PARAMETER), Charsets.UTF_8.name());
		checkArgument(!Strings.isNullOrEmpty(urlinput), INTERNAL_ERROR);
		
		if (req.getPathInfo().equals(CREATE_URL)) {	
			String shortUrlStr=shortUrlService.createAndSave(urlinput);			
			resp.getWriter().append(shortUrlStr);
		}else if (req.getPathInfo().equals(REMOVE_URL)) {			
			shortUrlService.delete(urlinput);
			
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req.getPathInfo().equals(INDEX_PATH)) {
			//cargo el index
			ByteStreams.copy(
					req.getServletContext().getResourceAsStream(INDEX_PAGE),
					resp.getOutputStream());
		} else if (req.getPathInfo().startsWith("/img")) {
			ByteStreams.copy(
					req.getServletContext().getResourceAsStream(req.getPathInfo()),
					resp.getOutputStream());
		}
		else if (req.getPathInfo().startsWith(LIST_URL)) {
			Gson gson = new GsonBuilder().create();
			String urlListJson =gson.toJson(shortUrlService.findAll());
			resp.setContentType("application/json");
			resp.setCharacterEncoding(Charsets.UTF_8.name());			
			resp.getWriter().write(urlListJson);
			
		}
		else {
			checkState(req.getPathInfo().startsWith(INDEX_PATH));
			ShortUrl shortUrl =shortUrlService.get(req.getPathInfo().substring(1));
			if(shortUrl!=null){
				String redirectUrl = shortUrl.getLongtUrl();			
				if (!redirectUrl.startsWith("http")) {
					redirectUrl = "http://" + redirectUrl;
				}
				resp.sendRedirect(redirectUrl);
			}
			
		}
	}
	
	

}

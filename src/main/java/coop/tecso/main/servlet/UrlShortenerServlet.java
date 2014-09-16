package coop.tecso.main.servlet;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import coop.tecso.main.db.PersistentMap;

@WebServlet(urlPatterns = { "/*" })
public class UrlShortenerServlet extends HttpServlet {

	public static final String CREATE_URL = "/create";
	public static final String REMOVE_URL = "/create";
	public static final String INDEX_PATH = "/";
	public static final String INDEX_PAGE = "index.jsp";
	public static final String INTERNAL_ERROR = "Missing urlinput parameter";
	public static final String URL_PARAMETER = "urlinput";

	public final Map<String, String> urlMap = Maps.newHashMap();
	public PersistentMap dataBase = PersistentMap.getInstance();
	public int count;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String urlinput = req.getParameter(URL_PARAMETER);

		checkArgument(!Strings.isNullOrEmpty(urlinput), INTERNAL_ERROR);

		String urlOuput = getShortUrl(urlinput);
		
		if (req.getPathInfo().equals(CREATE_URL)) {
			persistsUrlMapping(urlinput, urlOuput);
			resp.getWriter().append("/urlshortener/" + urlOuput);
		}else if (req.getPathInfo().equals(REMOVE_URL)) {
			deleteUrlMapping(urlOuput);
			
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req.getPathInfo().equals(INDEX_PATH)) {
			ByteStreams.copy(
					req.getServletContext().getResourceAsStream(INDEX_PAGE),
					resp.getOutputStream());
		} else {
			checkState(req.getPathInfo().startsWith(INDEX_PATH));
			String redirectUrl = dataBase.select(req.getPathInfo().substring(1));
			resp.sendRedirect("http://" + redirectUrl);
		}
	}

	private String getShortUrl(String url) {
		
//		return Integer.toString(count++);
		byte[] convertme =url.getBytes();
		return toSHA1(convertme,url);
	}
	
	public static String toSHA1(byte[]  convertme,String s) {
		HashFunction hf = Hashing.sha1();
		HashCode hashCode= hf.hashBytes(convertme);
		hashCode=hf.hashString(s, Charset.defaultCharset());
		byte[] sha1 = hashCode.asBytes();
		
		
		
	    String shaStr =BaseEncoding.base64Url().encode(sha1);;
	    shaStr = shaStr.substring(0, 6);	    
	    try {
			return URLEncoder.encode(shaStr, Charsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			throw Throwables.propagate(e);
		}
	}
	

	private void persistsUrlMapping(String longUrl, String shortUrl) {
		dataBase.insert(shortUrl, longUrl);
	}
	private void deleteUrlMapping(String shortUrl) {
		dataBase.delete(shortUrl);
	}

}

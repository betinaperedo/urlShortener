package coop.tecso.main;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;

@WebServlet(urlPatterns = { "/*" })
public class UrlShortenerServlet extends HttpServlet {

	public static final String CREATE_URL = "/create";
	public static final String INDEX = "/";

	public final Map<String, String> urlMap = Maps.newHashMap();
	public int count;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String urlinput = req.getParameter("urlinput");

		checkArgument(!Strings.isNullOrEmpty(urlinput), "Missing urlinput parameter");

		String urlOuput = getShortUrl(urlinput);
		persistsUrlMapping(urlinput, urlOuput);
		if (req.getPathInfo().equals(CREATE_URL)) {
			String htmlRespone = "<html>";
			urlOuput = "http://localhost:8080/urlshortener/" + urlOuput;
			htmlRespone += "<h2>Your short url is : " + urlOuput + "</h2>";
			htmlRespone += "</html>";
			resp.getWriter().append(htmlRespone);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req.getPathInfo().equals(INDEX)) {
			ByteStreams.copy(
					req.getServletContext().getResourceAsStream("index.jsp"),
					resp.getOutputStream());
		} else {
			checkState(req.getPathInfo().startsWith("/"));
			String redirectUrl = urlMap.get(req.getPathInfo().substring(1));
			resp.sendRedirect("http://" + redirectUrl);
		}
	}

	private String getShortUrl(String url) {
		return Integer.toString(count++);
	}

	private void persistsUrlMapping(String longUrl, String shortUrl) {
		urlMap.put(shortUrl, longUrl);
	}

}

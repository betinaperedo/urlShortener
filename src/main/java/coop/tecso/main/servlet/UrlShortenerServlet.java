package coop.tecso.main.servlet;

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

import coop.tecso.main.db.PersistentMap;

@WebServlet(urlPatterns = { "/*" })
public class UrlShortenerServlet extends HttpServlet {

	public static final String CREATE_URL = "/create";
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
		return Integer.toString(count++);
	}
	private String buildAnswer(String urlOuput) {
		StringBuilder sb = new StringBuilder("<html>");
//		sb.append("<body>");
//		sb.append("<form action='/urlshortener/redirect' method='post' name='redirectionForm'>");
//		sb.append("<h2>Your short url is : ");
		sb.append("<a href='");
		sb.append("http://localhost:8080/urlshortener/");
		sb.append(urlOuput);
		sb.append("'>http://bety/");	
		sb.append(urlOuput);
		sb.append("</a>");	
//		sb.append("</form> ");
//		sb.append("</body>");
		sb.append("</html>");
		return sb.toString();
		
	}

	private void persistsUrlMapping(String longUrl, String shortUrl) {
		dataBase.insert(shortUrl, longUrl);
	}

}

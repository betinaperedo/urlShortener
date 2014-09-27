package coop.tecso.main.servlet;

import com.google.inject.servlet.ServletModule;

public class UrlShortenerServletModule extends ServletModule {

	  @Override
	     protected void configureServlets() {
	       serve("*").with(UrlShortenerServlet.class);
	     }
}

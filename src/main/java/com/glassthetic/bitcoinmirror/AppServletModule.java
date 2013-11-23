package com.glassthetic.bitcoinmirror;

import com.google.inject.servlet.ServletModule;

/**
 * Binds routes to Servlets
 * 
 * @author mark
 */
public class AppServletModule extends ServletModule {
  
  @Override
  protected void configureServlets() {
    serve("/oauth2callback").with(AuthServlet.class);
  }
}

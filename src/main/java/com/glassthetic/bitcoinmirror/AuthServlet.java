package com.glassthetic.bitcoinmirror;


import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.*;

import com.google.inject.Singleton;


/**
 * This servlet manages the OAuth 2.0 dance
 *
 * @author Mark Fayngersh - http://google.com/+MarkFayngersh
 */
@SuppressWarnings("serial")
@Singleton
public class AuthServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(AuthServlet.class.getSimpleName());
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String code;
    
    // If we have a code, finish OAuth flow
    if ((code = req.getParameter("code")) != null) {
      LOG.info("Got OAuth code, exchanging for access token.");
      
      User user = AuthUtil.createUser(req, code);
      
      if (user != null) {        
        // Bootstrap user if first time
        if (!user.bootstrapped)
          BootstrapUser.bootstrapUser(user);
        
        // Set user id on session
        req.getSession().setAttribute("user_id", user.id);
      } else {
        LOG.info("Unable to create user");
        req.getSession().removeAttribute("user_id");
      }
      
      // Redirect back to index
      res.sendRedirect(WebUtil.buildUrl(req, "/"));
      return;
    }
    
    // Else, initiate a new flow.
    LOG.info("No auth context found. Kicking off a new auth flow.");
    
    String authorizationUrl = AuthUtil.getAuthorizationUrl(req);
    res.sendRedirect(authorizationUrl);
  }
}

package com.glassthetic.bitcoinforglass;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.*;


/**
 * This servlet manages the OAuth 2.0 dance
 *
 * @author Jenny Murphy - http://google.com/+JennyMurphy
 * @author Mark Fayngersh - http://google.com/+MarkFayngersh
 */
@SuppressWarnings("serial")
public class AuthServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(AuthServlet.class.getSimpleName());
  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    
    // If we have a code, finish OAuth flow
    if (req.getParameter("code") != null) {
      LOG.info("Got OAuth code, exchanging for access token.");
      
      AuthorizationCodeFlow flow = AuthUtil.newAuthorizationCodeFlow();
      TokenResponse tokenResponse =
          flow.newTokenRequest(req.getParameter("code"))
              .setRedirectUri(WebUtil.buildUrl(req, "/oauth2callback"))
              .execute();

      // Extract the Google User ID from the ID token in the auth response
      String userId = ((GoogleTokenResponse) tokenResponse).parseIdToken().getPayload().getUserId();

      LOG.info("Code exchange worked. User " + userId + " logged in.");

      // Set it into the session
      AuthUtil.setUserId(req, userId);
      flow.createAndStoreCredential(tokenResponse, userId);
      
      // Bootstrap new user
      BootstrapUser.bootstrapUser(req, userId);

      // Redirect back to index
      res.sendRedirect(WebUtil.buildUrl(req, "/"));
      return;
    }
    
    // Else, initiate a new flow.
    LOG.info("No auth context found. Kicking off a new auth flow.");
    
    AuthorizationCodeFlow flow = AuthUtil.newAuthorizationCodeFlow();
    GenericUrl url =
        flow.newAuthorizationUrl().setRedirectUri(WebUtil.buildUrl(req, "/oauth2callback"));
    url.set("approval_prompt", "force");
    res.sendRedirect(url.build());

  }
}

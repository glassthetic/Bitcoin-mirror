package com.glassthetic.bitcoinmirror;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;


/**
 * A collection of utility functions that simplify common authentication and
 * user identity tasks
 *
 * @author Mark Fayngersh
 */
public class AuthUtil {
  private static final Logger LOG = Logger.getLogger(AuthUtil.class.getSimpleName());
  
  private static GoogleAuthorizationCodeFlow flow = null;
  private static final String CLIENTSECRETS_LOCATION = "/client_secrets.json";
  private static final String REDIRECT_URI = "/oauth2callback";
  private static final List<String> SCOPES = Arrays.asList(
      "https://www.googleapis.com/auth/glass.timeline",
      "https://www.googleapis.com/auth/userinfo.profile");
  
  /**
   * Creates and returns a new {@link GoogleAuthorizationCodeFlow} for this app.
   * 
   * @return {@link GoogleAuthorizationCodeFlow}
   * @throws IOException
   */
  static GoogleAuthorizationCodeFlow getFlow() throws IOException {
    if (flow == null) {
      HttpTransport httpTransport = new NetHttpTransport();
      JsonFactory jsonFactory = new JacksonFactory();
      Reader reader = new InputStreamReader(AuthUtil.class.getResourceAsStream(CLIENTSECRETS_LOCATION));
      GoogleClientSecrets clientSecrets =
          GoogleClientSecrets.load(jsonFactory, reader);
      flow =
          new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, SCOPES)
              .setAccessType("offline").setApprovalPrompt("force").build();
    }
    return flow;
  }

  /**
   * Send a request to the UserInfo API to retrieve the user's information.
   * 
   * @param credentials {@link GoogleCredential}
   * @return {@link Userinfo}
   * @throws IOException 
   */
  static Userinfo getUserInfo(GoogleCredential credentials) throws IOException {
    Oauth2 userInfoService =
        new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credentials).build();
    Userinfo userInfo = userInfoService.userinfo().get().execute();
    return userInfo;
  }
  
  /**
   * Exchange an authorization code for OAuth2.0 credentials.
   * 
   * @param req {@link HttpServletRequest}
   * @param authorizationCode
   * @return {@link GoogleCredential}
   * @throws IOException 
   */
  static GoogleCredential exchangeCode(HttpServletRequest req, String authorizationCode)
      throws IOException { 
    GoogleAuthorizationCodeFlow flow = getFlow();
    GoogleTokenResponse response =
        flow.newTokenRequest(authorizationCode).setRedirectUri(WebUtil.buildUrl(req, REDIRECT_URI)).execute();
    
    return createGoogleCredential(response.getAccessToken(), response.getRefreshToken());      
  }
  
  /**
   * Creates a new {@link GoogleCredential} object from access and refresh tokens
   * 
   * @param accessToken
   * @param refreshToken
   * @return
   * @throws IOException
   */
  public static GoogleCredential createGoogleCredential(String accessToken, String refreshToken)
      throws IOException {
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    Reader reader = new InputStreamReader(AuthUtil.class.getResourceAsStream(CLIENTSECRETS_LOCATION));
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(jsonFactory, reader);
    
    GoogleCredential credentials = new GoogleCredential.Builder()
    .setJsonFactory(jsonFactory)
    .setTransport(httpTransport)
    .setClientSecrets(clientSecrets)
    .build()
      .setAccessToken(accessToken)
      .setRefreshToken(refreshToken);
    
    return credentials;
  }
  
  /**
   * Retrieve the authorization URL.
   *
   * @param req {@link HttpServletRequest}
   * @return Authorization URL to redirect the user to.
   * @throws IOException Unable to load client_secrets.json.
   */
  public static String getAuthorizationUrl(HttpServletRequest req)
      throws IOException {
    GoogleAuthorizationCodeRequestUrl urlBuilder =
        getFlow().newAuthorizationUrl().setRedirectUri(WebUtil.buildUrl(req, REDIRECT_URI));
    return urlBuilder.build();
  }
  
  /**
   * Creates a new User
   * 
   * @param req {@link HttpServletRequest}
   * @param authorizationCode
   * @return {@link User}
   * @throws IOException 
   */
  public static User createUser(HttpServletRequest req, String authorizationCode)
      throws IOException {
    GoogleCredential credentials = exchangeCode(req, authorizationCode);
    Userinfo userInfo = getUserInfo(credentials);
    User user = User.createUser(userInfo, credentials);
    return user;
  }
}

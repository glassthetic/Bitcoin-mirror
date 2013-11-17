package com.glassthetic.bitcoinforglass;

import static com.glassthetic.bitcoinforglass.OfyService.ofy;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.oauth2.model.Userinfo;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;


/**
 * Represents a User Entity in the Datastore 
 * 
 * @author mark
 */
@Entity
public class User {
  private static final Logger LOG = Logger.getLogger(User.class.getSimpleName());
  
  @Id String id;
  
  String name;
  String accessToken;
  String refreshToken;
  Boolean bootstrapped;
  
  @Ignore GoogleCredential credentials;
  
  /**
   * No-arg constructor required by Objectify
   */
  @SuppressWarnings("unused")
  private User() {}
  
  /**
   * Instantiates a new User
   * 
   * @param id
   * @param name
   * @param accessToken
   * @param refreshToken
   * @throws IOException 
   */
  public User(String id, String name, String accessToken, String refreshToken) throws IOException {
    this.id = id;
    this.name = name;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.bootstrapped = false;

    this.credentials = AuthUtil.createGoogleCredential(accessToken, refreshToken);
  }
  
  
  /***********************
   * 
   * Static helper methods
   * 
   ***********************/
  
  /**
   * Retrieves User from Datastore
   * 
   * @param userId
   * @return {@link User}
   */
  public static User getUser(String userId) {
    User user = ofy().load().type(User.class).id(userId).now();
    return user;
  }
  
  /**
   * Creates new User in the Datastore, or updates existing one.
   * 
   * @param userInfo {@link Userinfo}
   * @param credentials {@link GoogleCredential}
   * @throws IOException 
   */
  public static User createUser(Userinfo userInfo, GoogleCredential credentials) throws IOException {
    User user = ofy().load().type(User.class).id(userInfo.getId()).now();
    
    if (user != null) {
      user.accessToken = credentials.getAccessToken();
      user.refreshToken = credentials.getRefreshToken();
      user.credentials = AuthUtil.createGoogleCredential(credentials.getAccessToken(), credentials.getRefreshToken());
    } else {
      user = new User(
        userInfo.getId(),
        userInfo.getName(), 
        credentials.getAccessToken(),
        credentials.getRefreshToken());
    }
    
    ofy().save().entity(user).now();
    return user;
  }
}

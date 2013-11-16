package com.glassthetic.bitcoinforglass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.TimelineItem;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class BootstrapUser {
  private static final Logger LOG = Logger.getLogger(BootstrapUser.class.getSimpleName());
  
  
  public static void bootstrapUser(HttpServletRequest req, String userId) throws IOException {
    Credential credential = AuthUtil.newAuthorizationCodeFlow().loadCredential(userId);
        
    TimelineItem btcCard = new TimelineItem();
    btcCard.setText("Welcome to Bitcoin for Glass!");
    
    List<MenuItem> menuItems = new ArrayList<MenuItem>();
    menuItems.add(new MenuItem().setAction("TOGGLE_PINNED"));
    menuItems.add(new MenuItem().setAction("DELETE"));
    btcCard.setMenuItems(menuItems);
    
    MirrorClient.insertTimelineItem(credential, btcCard);
    
    LOG.info("Inserted card: " + btcCard.toString());
  }
}

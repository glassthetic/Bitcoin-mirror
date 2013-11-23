package com.glassthetic.bitcoinmirror;

import static com.glassthetic.bitcoinmirror.OfyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;


public class BootstrapUser {
  private static final Logger LOG = Logger.getLogger(BootstrapUser.class.getSimpleName());
  
  public static void bootstrapUser(User user) throws IOException {     
    TimelineItem btcCard = new TimelineItem();
    btcCard.setText("Bitcoin Bootstrap Test");
    
    List<MenuItem> menuItems = new ArrayList<MenuItem>();
    menuItems.add(new MenuItem().setAction("TOGGLE_PINNED"));
    menuItems.add(new MenuItem().setAction("DELETE"));
    btcCard.setMenuItems(menuItems);
    
    btcCard.setNotification(new NotificationConfig().setLevel("DEFAULT"));
    
    MirrorClient.insertTimelineItem(user.credentials, btcCard);
    
    user.bootstrapped = true;
    ofy().save().entity(user).now();
    
    LOG.info("Bootstrapped user '" + user.name + "' with new card: " + btcCard.toPrettyString());
  }
}

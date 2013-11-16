package com.glassthetic.bitcoinforglass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.mirror.model.MenuItem;
import com.google.api.services.mirror.model.NotificationConfig;
import com.google.api.services.mirror.model.TimelineItem;

public class InsertTimelineServlet extends HttpServlet {
  private static final Logger LOG = Logger.getLogger(InsertTimelineServlet.class.getSimpleName());

  
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    String userId = AuthUtil.getUserId(req);
    Credential credential = AuthUtil.newAuthorizationCodeFlow().loadCredential(userId);
    
    TimelineItem item = new TimelineItem();
    item.setText("Test from Java :D");
    
    List<MenuItem> menuItems = new ArrayList<MenuItem>();
    menuItems.add(new MenuItem().setAction("DELETE"));
    item.setMenuItems(menuItems);
    
    item.setNotification(new NotificationConfig().setLevel("DEFAULT"));
    
    TimelineItem result = MirrorClient.insertTimelineItem(credential, item);    
  }
}

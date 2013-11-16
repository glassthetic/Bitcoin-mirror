<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="com.glassthetic.bitcoinforglass.AuthUtil" %>

<!doctype html>
<html>
  <head>
    <title>Bitcoin for Glass</title>
    
    <link type="text/css" rel="stylesheet" href="/static/stylesheets/main.css" />
  </head>

  <body>
    <div class="about">
		    <h1>Bitcoin for Glass</h1>
		    <h2>Pin a card displaying the latest Bitcoin exchange rate.</h2>
		    <h3>Project by glassthetic</h3>
		    <a href="http://glassthetic.com">Homepage</a>
      <a href="https://plus.google.com/115972798831479068154/posts">+glassthetic</a>
    </div>
    
    <div class="settings">
			   <% if (AuthUtil.getUserId(request) != null) { %>
			     <h2 class="heading">Settings</h2>
				    
				    <div class="group">
				      <p>Exchange</p>
				      <span>Bitstamp</span><input type="radio" name="exchange" value="bitstamp"><br/>
		        <span>Mt. Gox</span><input type="radio" name="exchange" value="mtgox">
				    </div>
				    
				    <button class="btn">Save</button>
				    				    
				    <h2 class="heading">Extras</h2>
				    <a class="btn yellow">Resend Card</a>
				    <a class="btn red">Delete</a>
			   <% } else { %>
			     <a href="/oauth2callback"><img src="/static/images/getitonglass_172x60_action_button.png"/></a>
			   <% } %>
			 </div>
  </body>
</html>

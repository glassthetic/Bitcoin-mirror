package com.glassthetic.bitcoinmirror;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * Constructs an {@link Injector} with {@link AppServletModule}
 * 
 * This is a {@link GuiceServletContextListener} that is triggered upon deployment
 * before any requests arrive. 
 * 
 * @author Mark Fayngersh
 */
public class GuiceServletConfig extends GuiceServletContextListener {

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(new AppServletModule());
  }
}

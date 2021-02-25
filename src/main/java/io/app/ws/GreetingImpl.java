package io.app.soap;

import java.util.Map;

import javax.inject.Inject;
import javax.jws.WebService;

import io.app.backend.HelloWorldBackend;
import io.app.ws.HelloWorld;
import org.jboss.logging.Logger;

import io.app.domain.User;

/**
 * Implementation of HelloWorld.
 */

//@WebService(/* endpointInterface = "io.app.soap.HelloWorld" */)
public class GreetingImpl /* implements HelloWorld */ {
  static final Logger logger = Logger.getLogger(GreetingImpl.class);

  @Inject
  HelloWorldBackend hwService;

  //@Override
  public String hello() {
    return hwService.hello();
  }
  //@Override
  public String greet() {
    return hwService.hello();
  }

  //@Override
  public String sayHi(String text) {
    return hwService.sayHi(text);
  }

  //@Override
  public void addUser(User user) {
    try {
      logger.info("sayHiToUser:" + user);
      hwService.addUser(user);
    } catch (Throwable e) {
      logger.info("**** error seen:" + e);
      throw e;
    }
  }

  //@Override
  public Map<Integer, User> getUsers() {
    return hwService.getUsers();
  }
}

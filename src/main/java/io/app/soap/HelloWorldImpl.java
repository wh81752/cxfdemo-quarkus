package io.app.soap;

import io.app.service.HelloWorldService;
import org.jboss.logging.Logger;

import javax.jws.WebService;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of HelloWorld.
 *
 */

@WebService(serviceName = "HelloWorld")
public class HelloWorldImpl implements HelloWorld {
    static final Logger logger = Logger.getLogger(HelloWorldImpl.class);

    HelloWorldService hwService = new HelloWorldService();

    @Override
    public String hello() {
        return hwService.hello();
    }

    @Override
    public String sayHi(String text) {
        return hwService.sayHi(text);
    }

    @Override
    public String sayHiToUser(User user) {
        return hwService.sayHiToUser(user);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return hwService.getUsers();
    }
}
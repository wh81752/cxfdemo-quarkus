package io.app.soap;

import java.util.Map;

import javax.jws.WebService;

import org.jboss.logging.Logger;

import io.app.domain.User;
import io.app.service.HelloWorldService;

/**
 * Implementation of HelloWorld.
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

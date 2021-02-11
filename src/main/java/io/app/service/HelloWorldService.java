package io.app.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.Dependent;

import org.jboss.logging.Logger;

import io.app.domain.User;

/**
 * HelloWorld Service Implementation
 *
 * <p>
 * Provides a (thread safe) hello world service.
 *
 */
@Dependent
public class HelloWorldService {
    static final Logger logger = Logger.getLogger(HelloWorldService.class);
    static final Map<Integer, User> users = new ConcurrentHashMap<>();

    public String hello() {
        logger.debug("hello called");
        return "Hello World";
    }

    public String sayHi(String text) {
        logger.debug("sayHi called with arg: " + text);
        return String.format("%s %s", "Hello", text);
    }

    public String sayHiToUser(User user) {
        String r = "";
        logger.debug("sayHiToUser called with arg: " + user);
        if (user != null) {
            users.put(users.size() + 1, user);
            r = "Hello " + user.getName();
        }
        return r;
    }

    public Map<Integer, User> getUsers() {
        logger.debug("getUsers called");
        return users;
    }
}

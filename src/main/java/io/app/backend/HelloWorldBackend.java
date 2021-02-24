package io.app.backend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.Dependent;

import org.jboss.logging.Logger;

import io.app.domain.User;

/**
 * HelloWorld Backend Implementation
 *
 * <p>
 * Provides a (thread safe) hello world service.
 *
 */
@Dependent
public class HelloWorldBackend {
    static final Logger logger = Logger.getLogger(HelloWorldBackend.class);
    static final Map<Integer, User> users = new ConcurrentHashMap<>();

    public String hello() {
        logger.debug("hello called");
        return "Hello World";
    }

    public String sayHi(String text) {
        logger.debug("sayHi called with arg: " + text);
        return String.format("%s %s", "Hello", text);
    }

    @RolesAllowed({ "APPUSER" })
    public void addUser(User user) {
        logger.debug("addUser called with arg: " + user);
        if (user != null) {
            users.put(users.size() + 1, user);
        }
    }

    public Map<Integer, User> getUsers() {
        logger.debug("getUsers called");
        return users;
    }
}

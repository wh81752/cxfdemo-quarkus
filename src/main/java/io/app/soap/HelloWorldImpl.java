package io.app.soap;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import io.app.domain.User;
import io.app.service.HelloWorldService;

/**
 * Implementation of HelloWorld.
 */

@ApplicationScoped
public class HelloWorldImpl implements HelloWorld {
    static final Logger logger = Logger.getLogger(HelloWorldImpl.class);

    @Inject
    HelloWorldService hwService;

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
        try {
            logger.info("sayHiToUser:" + user);
            return hwService.sayHiToUser(user);
        } catch (Throwable e) {
            logger.info("**** error seen:" + e);
            throw e;
        }
    }

    @Override
    public String securedHiToUser(User user) {
        return hwService.sayHiToUser(user);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return hwService.getUsers();
    }
}

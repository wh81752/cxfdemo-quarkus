package io.app.ws;

import io.app.backend.HelloWorldBackend;
import io.app.domain.User;
import org.apache.cxf.annotations.SchemaValidation;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Map;

/**
 * Implementation of HelloWorld.
 */
//@WebService(endpointInterface = "io.app.ws.HelloWorld")
//@ApplicationScoped
//@SchemaValidation
//@SOAPBinding(style = SOAPBinding.Style.RPC)
public class GreetingImpl implements HelloWorld {
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
            logger.info("addUser:" + user);
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

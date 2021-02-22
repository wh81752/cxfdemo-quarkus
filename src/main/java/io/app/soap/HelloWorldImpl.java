package io.app.soap;

import io.app.domain.User;
import io.app.service.HelloWorldService;
import org.jboss.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.jws.WebService;
import java.util.Map;

/**
 * Implementation of HelloWorld.
 */

@WebService(serviceName = "HelloWorld")
@RolesAllowed({ "APPUSER" })
public class HelloWorldImpl implements HelloWorld {
    static final Logger logger = Logger.getLogger(HelloWorldImpl.class);

    @Inject
    HelloWorldService hwService;

    @Override
    @RolesAllowed({ "APPUSER" })
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

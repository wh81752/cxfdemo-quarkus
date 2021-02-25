package io.app.ws;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.cxf.annotations.SchemaValidation;
import org.jboss.logging.Logger;

import io.app.backend.HelloWorldBackend;
import io.app.domain.User;

/**
 * Implementation of HelloWorld.
 */

@WebService(serviceName = "HelloWorld",endpointInterface="io.app.ws.HelloWorld")
@RequestScoped
@SchemaValidation
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class HelloWorldImpl implements HelloWorld {
    static final Logger logger = Logger.getLogger(HelloWorldImpl.class);

    public HelloWorldImpl() {
        logger.info("ctor");
    }

    @PostConstruct
    void postConstruct() {
        logger.info("postConstruct");
    }

    @Inject
    HelloWorldBackend hwService;

    @Override
    public String hello() {
        return hwService.hello();
    }
    @Override
    public String greet() {
        return hwService.hello();
    }

    @Override
    public String sayHi(String text) {
        return hwService.sayHi(text);
    }

    @Override
    public void addUser(User user) {
        hwService.addUser(user);
    }

    @Override
    public Map<Integer, User> getUsers() {
        return hwService.getUsers();
    }
}

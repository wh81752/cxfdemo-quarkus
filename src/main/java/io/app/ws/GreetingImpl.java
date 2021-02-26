package io.app.ws;

import io.app.backend.HelloWorldBackend;
import io.app.domain.User;
import org.apache.cxf.annotations.SchemaValidation;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Map;

/**
 * Implementation of SEI HelloWorld.
 * <p>
 * Just an alternative implementation of SEI HellowWorld in order to demonstrate that QUARKUS-CXF also supports such a
 * scenario.
 * <p>
 * Another change compared to HelloWorldImpl is that this bean is scoped as @ApplicationScoped.
 * <p>
 * Notice that values are given for various WS properties, i.e. name, portName, serviceName and targetNamespace. None of
 * them have an impact on generated WSDL. As of quarkus-cxf-0.6.0 all this settings are ignored. See issue #140 for
 * details.
 */
@WebService(
        endpointInterface = "io.app.ws.HelloWorld",
        name = "Greeting",
        portName = "GreetingPort",
        serviceName = "Greeting",
        targetNamespace = "http://greet.app.io/"
)
@ApplicationScoped
@SchemaValidation
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class GreetingImpl implements HelloWorld {
    static final Logger logger = Logger.getLogger(GreetingImpl.class);

    @Inject
    HelloWorldBackend hwService;

    @Override
    public String hello() {
        return String.format("greet:%s",hwService.hello());
    }

    @Override
    public String greet() {
        return String.format("greet:%s",hwService.hello());
    }

    /**
     * Deviation from HelloWorldImpl: use a different @WebParam name, here
     * text instead of arg0.
     *
     * However: CXF states that @WebParam is placed on SEI. Applying on impl should
     * either be a failure or should have no impact. The later is the case of CXF
     * 3.4.2 (respective: quarkus-cxf-0.6.0).
     */
    @Override
    public String sayHi(@WebParam(name = "text") String text) {
        return String.format("greet:%s",hwService.sayHi(text));
    }

    @Override
    public void addUser(User user) {
        try {
            logger.info("sayHiToUser:" + user);
            hwService.addUser(user);
        } catch (Throwable e) {
            logger.info("**** error seen:" + e);
            throw e;
        }
    }

    @Override
    public Map<Integer, User> getUsers() {
        return hwService.getUsers();
    }
}

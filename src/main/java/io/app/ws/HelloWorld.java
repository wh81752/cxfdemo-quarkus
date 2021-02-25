package io.app.ws;

import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.app.domain.User;

/**
 * Simple Hello (Greeter) Webservice.
 *
 * <p>
 * Say hi to well known users.
 */

@SuppressWarnings("unused")
@WebService(serviceName = "HelloWorld")
public interface HelloWorld {

    /**
     * Most basic service function.
     */
    @WebMethod
    String hello();

    String greet();

    /**
     * Say hi to someone. Slightly more complex than just hello().
     */
    String sayHi(String text);

    /*
     * Advanced usecase of passing an Interface in. JAX-WS/JAXB does not support interfaces directly. Special XmlAdapter
     * classes need to be written to handle them
     */
    @XmlElement(required = true)
    void addUser(@XmlElement(required = true) @WebParam(name = "user") User user);

    /*
     * Map passing JAXB also does not support Maps. It handles Lists great, but Maps are not supported directly. They
     * also require use of a XmlAdapter to map the maps into beans that JAXB can use.
     */
    @XmlJavaTypeAdapter(IntegerUserMapAdapter.class)
    Map<Integer, User> getUsers();
}

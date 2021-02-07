package io.app.soap;

import io.app.domain.User;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

/**
 * Simple Hello (Greeter) Webservice.
 *
 * <p>Say hi to well known users.
 *
 */

@SuppressWarnings("unused")
@WebService
//@SchemaValidation
public interface HelloWorld {

    /**
     * Most basic service function.
     */
    String hello();

    /**
     * Say hi to someone. Slightly more complex than just hello().
     */
    String sayHi(String text);

    /* Advanced usecase of passing an Interface in.  JAX-WS/JAXB does not
     * support interfaces directly.  Special XmlAdapter classes need to
     * be written to handle them
     */
    @WebMethod()
    @WebResult(name = "result")
    @XmlElement(required = true)
    String sayHiToUser(@XmlElement(required = true) @WebParam(name = "user") User user);

    /* Map passing
     * JAXB also does not support Maps.  It handles Lists great, but Maps are
     * not supported directly.  They also require use of a XmlAdapter to map
     * the maps into beans that JAXB can use.
     */
    @XmlJavaTypeAdapter(IntegerUserMapAdapter.class)
    Map<Integer, User> getUsers();
}

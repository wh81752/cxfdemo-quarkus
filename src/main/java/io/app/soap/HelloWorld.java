package io.app.soap;

import io.app.domain.User;

import javax.annotation.security.RolesAllowed;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

/**
 * Simple Hello (Greeter) Webservice.
 *
 * <p>Say hi to well known users.
 */

@SuppressWarnings("unused")
@WebService
@RolesAllowed({ "APPUSER" })
//@SchemaValidation                // XML issues if @SchemaValidation is enabled. Note: works
// perfectly in a Quarkus-Undertow-CXF scenario.
public interface HelloWorld {

    /**
     * Most basic service function.
     */
    @WebMethod()
    String hello();

    /**
     * Say hi to someone. Slightly more complex than just hello().
     */
    @WebMethod()
    String sayHi(String text);

    /* Advanced usecase of passing an Interface in.  JAX-WS/JAXB does not
     * support interfaces directly.  Special XmlAdapter classes need to
     * be written to handle them
     */
    @WebMethod()
    @XmlElement(required = true)
    String sayHiToUser(@XmlElement(required = true) @WebParam(name = "user") User user);

    @WebMethod()
    @XmlElement(required = true)
    @RolesAllowed({ "APPUSER" })
    String securedHiToUser(@XmlElement(required = true) @WebParam(name = "user") User user);

    /* Map passing
     * JAXB also does not support Maps.  It handles Lists great, but Maps are
     * not supported directly.  They also require use of a XmlAdapter to map
     * the maps into beans that JAXB can use.
     */
    @XmlJavaTypeAdapter(IntegerUserMapAdapter.class)
    Map<Integer, User> getUsers();
}

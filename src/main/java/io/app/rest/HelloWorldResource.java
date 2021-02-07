package io.app.rest;

import io.app.service.HelloWorldService;
import io.app.soap.User;
import org.jboss.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Class Documentation
 *
 * <p>What is the point of this class?
 *
 * @author geronimo1
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class HelloWorldResource {
    private static final Logger logger = Logger.getLogger(HelloWorldResource.class);

    HelloWorldService hwService = new HelloWorldService();

    @GET
    @Path("/hello")
    public String hello() {
        return hwService.hello();
    }

    @GET
    @Path("/hello/{arg}")
    public String sayHi(@PathParam("arg") String text) {
        return hwService.sayHi(text);
    }

    @POST
    @Path("/hello/{user}")
    public String sayHiToUser(@PathParam("user") User user) {
        return hwService.sayHiToUser(user);
    }

    @GET
    @Path("/users")
    public Map<Integer, User> getUsers() {
        return hwService.getUsers();
    }
}

package io.app.rest;

import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import io.app.domain.User;
import io.app.domain.UserImpl;
import io.app.service.HelloWorldService;

/**
 * Class Documentation
 *
 * <p>
 * What is the point of this class?
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
@Path("/hw") // STRANGE: MUST be present.
public class HelloWorldResource {
    private static final Logger logger = Logger.getLogger(HelloWorldResource.class);

    @Inject
    HelloWorldService hwService;

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
    public String sayHiToUser(@PathParam("user") UserImpl user) { // TODO: use interface User instead
        return hwService.sayHiToUser(user);
    }

    @POST
    @Path("/sechello/{user}")
    @RolesAllowed({ "APPUSER" })
    public String sayHiToUserSecured(@PathParam("user") UserImpl user) { // TODO: use interface User instead
        return String.format("***%s***", hwService.sayHiToUser(user));
    }

    @GET
    @Path("/users")
    @PermitAll
    public Map<Integer, User> getUsers() {
        return hwService.getUsers();
    }
}

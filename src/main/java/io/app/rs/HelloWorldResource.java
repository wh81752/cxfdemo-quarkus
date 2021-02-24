package io.app.rs;

import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import io.app.backend.HelloWorldBackend;
import io.app.domain.User;
import io.app.domain.UserImpl;

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
    HelloWorldBackend hwService;

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
    @Path("/add/{user}")
    @RolesAllowed({ "APPUSER" })
    public void addUser(@PathParam("user") UserImpl user) { // TODO: use interface User instead
        hwService.addUser(user);
    }

    @GET
    @Path("/users")
    @PermitAll
    public Map<Integer, User> getUsers() {
        return hwService.getUsers();
    }
}

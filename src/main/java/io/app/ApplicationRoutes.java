package io.app;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import io.vertx.ext.web.Router;

/**
 * Class Documentation
 *
 * <p>
 * What is the point of this class?
 *
 * @author geronimo1
 */
@ApplicationScoped
public class ApplicationRoutes {
  public void routes(@Observes Router router) {
    router.get("/ok").handler(rc -> rc.response().end("OK from Route"));
  }
}

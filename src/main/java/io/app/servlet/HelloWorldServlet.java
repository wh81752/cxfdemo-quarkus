package io.app.servlet;

import java.util.Objects;

import javax.enterprise.inject.spi.CDI;
import javax.servlet.ServletConfig;
import javax.xml.ws.Endpoint;

import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.jboss.logging.Logger;

import io.app.soap.HelloWorldImpl;

/**
 * HelloWorldServlet
 *
 * <p>
 * Provides a CXF servlet
 *
 * @author geronimo1
 */
public class HelloWorldServlet extends CXFNonSpringServlet {
  static final Logger logger = Logger.getLogger(HelloWorldServlet.class);

  public HelloWorldServlet() {
    logger.trace("ctor: HelloWorldServlet");
  }

  @Override
  public void loadBus(ServletConfig servletConfig) {
    // create new bus if necessary.
    this.bus = (this.bus != null) ? this.bus : BusFactory.getDefaultBus(true);
    // avoid this crazy stacktraces just because a regular fault happend ..
    // this.bus.setProperty("org.apache.cxf.logging.FaultListener", new NoDramaFaultListener());
    // publish my webservice
    HelloWorldImpl hwImpl = CDI.current().select(HelloWorldImpl.class).get();
    Objects.requireNonNull(hwImpl);
    Endpoint.publish("/hw", hwImpl);
  }

}

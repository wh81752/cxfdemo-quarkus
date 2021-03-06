package io.app.soap;

import io.app.domain.UserImpl;
import io.app.ws.HelloWorld;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.eclipse.microprofile.config.ConfigProvider;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

import static io.app.soap.Xml.NsXPath.SOAP;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

/**
 * E2E tests of GreetingImpl.
 *
 */

@QuarkusTest
class CXFGreetingTest {
    private static final Logger logger = Logger.getLogger(CXFGreetingTest.class);

    @BeforeAll
    static void setup() {}

    /**
     * @return port number of quarkus instance initiated by @QuarkusTest
     */
    static private Integer quarkusport() {
        return ConfigProvider.getConfig().getValue("quarkus.http.test-port", Integer.class);
    }

    static private URL urlBuilder(String fmt, Object... args) {
        try {
            return new URL(String.format(fmt, args));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    static private String baseurl() {
        return String.format("http://localhost:%s/ws/greet",quarkusport());
    }

    static private URL wsURL() {
        return urlBuilder(baseurl());
    }

    static private URL wsdlURL() {
        return urlBuilder("%s?wsdl", baseurl());
    }

    static private QName serviceName() {
        return new QName("http://ws.app.io/", "HelloWorld");
    }

    static private QName portName() {
        return new QName("http://ws.app.io/", "HelloWorldImplPort");
    }

    static private HelloWorld client() {
        Service service = Service.create(serviceName());
        service.addPort(portName(), SOAP.namespace, wsURL().toString());
        return service.getPort(portName(), HelloWorld.class);
    }

    @Test
    void wsdl() {
        ValidatableResponse then;
        then = given().log().all(true).when().get("/ws/greet?wsdl").then();
        then.statusCode(200);
        then.contentType(ContentType.XML);
        then.body(hasXPath("/wsdl:definitions").withNamespaceContext(Xml.soapNamespaceContext()));
        then.body(Xml.matcherWsdl());
    }

    @Test
    void hello_with_wsdl() {
        Service service = Service.create(wsdlURL(), serviceName());
        HelloWorld client = service.getPort(HelloWorld.class);
        MatcherAssert.assertThat(client.hello(), CoreMatchers.is("greet:Hello World"));
    }

    @Test
    void hello_no_wsdl() {
        MatcherAssert.assertThat(client().hello(), CoreMatchers.is("greet:Hello World"));
    }

    @Test
    void sayHi_no_wsdl() {
        MatcherAssert.assertThat(client().sayHi("foo"), CoreMatchers.is("greet:Hello foo"));
    }

    @Test
    void sayHiToUser() {
        client().addUser(new UserImpl("bar"));
    }

    @Test
    void getUsers() {
        assertNotNull(client().getUsers());
        assertTrue(client().getUsers().size() > 0);
    }
}

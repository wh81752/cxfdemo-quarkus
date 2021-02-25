package io.app.soap;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.xmlunit.matchers.HasXPathMatcher.hasXPath;

import java.nio.charset.StandardCharsets;

import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.app.soap.Xml.NsXPath;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

/**
 * Class Documentation
 *
 * <p>
 * Test SOAP EP
 *
 * @author wh81752
 */

@QuarkusTest
class RestAssuredHelloWorldTest {
  private static final Logger logger = Logger.getLogger(RestAssuredHelloWorldTest.class);

  @BeforeAll
  static void setup() {
    EncoderConfig enc;
    // Config demo:
    // Use "Content-Type: application/xml" as default for all HTTP requests.
    // Problem: overriding RestAssured.requestSpecification destroys the original setup magically
    // done via @QuarkusTest. To get it working again, RestAssured must be told about the test
    // port used by Quarkus. Perhaps other things also need to be done.
    RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.XML)
        .setPort(ConfigProvider.getConfig().getValue("quarkus.http.test-port", Integer.class))
        .build();
    //
    // UTF-8, UTF-8 and UTF-8 again. Is there any reason not to use UTF-8 nowadays?
    //
    enc = encoderConfig().defaultContentCharset(StandardCharsets.UTF_8);


    RestAssured.config = RestAssured.config().encoderConfig(enc);
  }

  @Test
  void wsdl() {
    ValidatableResponse then;
    then = given().log().all(true).when().get("/ws/hw?wsdl").then();
    then.statusCode(200);
    then.contentType(ContentType.XML);
    // TODO: check against namespaced root element
    then.body(hasXPath("/wsdl:definitions").withNamespaceContext(Xml.soapNamespaceContext()));
    then.body(Xml.matcherWsdl());
  }

  @Test
  void hello_get() {
    ValidatableResponse then;
    then = given().log().all(true).when().get("/ws/hw/hello").then();
    then.statusCode(500);
    then.contentType(ContentType.XML);
  }

  @Test
  void hello_post_empty() {
    ValidatableResponse then;
    then = given().log().all(true).when().post("/ws/hw/hello").then();
    then.statusCode(500);
    then.contentType(ContentType.XML);
  }

  public static String helloSoapRequest() {
    return "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:blz='http://soap"
        + ".app.io/'><soapenv:Header/><soapenv:Body><blz:hello /></soapenv:Body></soapenv:Envelope>";
  }

  @Test
  void hello_post_soap() {
    // <soap:Envelope
    // xmlns:soap="http://schemas.xmlsoap.org/ws/envelope/"><soap:Body><ns1:helloResponse
    // xmlns:ns1="http://ws.app.io/"><return>Hello
    // World</return></ns1:helloResponse></soap:Body></soap:Envelope>
    ValidatableResponse then;
    then = given().log().all(true).body(helloSoapRequest()).when().post("/ws/hw/hello").then();
    then.statusCode(200);
    then.contentType(ContentType.XML);
    // Hint: use exactly those prefixes as provided with function withNamespaceContext()
    then.body(hasXPath("/SENV:Envelope/SENV:Body/APP:helloResponse/return")
        .withNamespaceContext(NsXPath.ns()));
  }

  @Test
  void hello_post_soap_variant_A() {
    ValidatableResponse then;
    then =
        given().log().all(true).body(Xml.SOAPREQUESTS.hello()).when().post("/ws/hw/hello").then();
    then.statusCode(200);
    then.contentType(ContentType.XML);
    // Hint: use exactly those prefixes as provided with function withNamespaceContext()
    then.body(hasXPath("/SENV:Envelope/SENV:Body/APP:helloResponse/return")
        .withNamespaceContext(NsXPath.ns()));
  }

  @Test
  void sayHi() {
    ValidatableResponse then;
    then = given().log().all(true).body(Xml.SOAPREQUESTS.sayHi("foo")).when().post("/ws/hw/user")
        .then();
    then.statusCode(200);
    then.contentType(ContentType.XML);
    // Hint: use exactly those prefixes as provided with function withNamespaceContext()
    then.body(hasXPath("/SENV:Envelope/SENV:Body/APP:sayHiResponse/return")
        .withNamespaceContext(NsXPath.ns()));
  }

}

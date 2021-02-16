package io.app.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.*;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

/**
 * Class Documentation
 *
 * <p>
 * What is the point of this class?
 *
 * @author geronimo1
 */
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HelloWorldResourceTest {

  @BeforeAll
  static void setup() {
    // Config demo:
    // Use "Content-Type: application/json" as default for all HTTP requests.
    // Problem: overriding RestAssured.requestSpecification destroys the original setup magically
    // done via @QuarkusTest. To get it working again, RestAssured must be told about the test
    // port used by Quarkus. Perhaps other things also need to be done.
    RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON)
        .setPort(ConfigProvider.getConfig().getValue("quarkus.http.test-port", Integer.class))
        .build();
  }



  @Test
  @Order(1)
  public void test_ep_hello() {
    given().when().get("/rest/hw/hello").then().statusCode(200).body(is("Hello World"));
  }



  @Test
  @Order(1)
  public void test_ep_hello_world() {
    given().when().get("/rest/hw/hello/World").then().statusCode(200).body(is("Hello World"));
  }



  @Test
  @Order(1)
  public void test_ep_hello_null() {
    given().when().get("/rest/hw/hello/").then().statusCode(200).body(is("Hello World"));
  }



  @Test
  @Order(5)
  public void test_ep_get_users() {
    given().when().get("/rest/hw/users").then().statusCode(200)
        .body(is("{\"1\":{\"name\":\"foobar\"}}"));
  }



  @Test
  @Order(1)
  public void test_ep_post_hello_user() {
    given().when().post("/rest/hw/hello/foobar").then().statusCode(200).body(is("Hello foobar"));
  }
}

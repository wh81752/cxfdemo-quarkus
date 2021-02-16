package io.app.rest;

import io.app.domain.User;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.config.ConfigProvider;
import org.hamcrest.CoreMatchers;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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
    private static final Logger logger = Logger.getLogger(HelloWorldResourceTest.class);

    @BeforeAll
    static void setup() {
        // Config demo:
        // Use "Content-Type: application/json" as default for all HTTP requests.
        // Problem: overriding RestAssured.requestSpecification destroys the original setup magically
        // done via @QuarkusTest. To get it working again, RestAssured must be told about the test
        // port used by Quarkus. Perhaps other things also need to be done.
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType(ContentType.JSON).setPort(
                ConfigProvider.getConfig().getValue("quarkus.http.test-port", Integer.class)).build();
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
        given().when().get("/rest/hw/users").then().statusCode(200).body(is("{\"1\":{\"name\":\"foo\"},\"2\":{\"name" +
                                                                                    "\":\"bar\"}}"));
    }

    @Test
    @Order(5)
    public void test_ep_get_users_not_empty() {
        Map refvalue = Collections.emptyMap();
        Map curvalue = given().when().get("/rest/hw/users").then().statusCode(200).extract().as(Map.class);
        assertThat(curvalue, CoreMatchers.not(refvalue));
        assertThat(curvalue.size(), CoreMatchers.is(2));
    }

    @Test
    @Order(5)
    public void test_ep_get_users_not_empty_jsonpath() {
        // Expected JSON:
        // {
        //    { 1 , "foo" },
        //    { 2 , "bar" }
        // }
        given().when().get("/rest/hw/users").then().statusCode(200).body("1.name", is("foo"));
        given().when().get("/rest/hw/users").then().statusCode(200).body("2.name", is("bar"));
        // CheckItOut -> calculate number of children of root element (!)
        given().when().get("/rest/hw/users").then().statusCode(200).body("size()", is(2));
    }

    /**
     * Example of how to decode textual return value into a Java object and using Hamcrest matchers to assert existence
     * (or absence) of a value.
     */
    @Test
    @Order(0)
    public void test_ep_get_users_empty() {

        Map<Integer, User> expected = Collections.emptyMap();
        Map<Integer, User> actual = given().when().get("/rest/hw/users").then().statusCode(200).extract().as(Map.class);
        assertThat(actual, CoreMatchers.is(expected));
    }

    @Test
    @Order(1)
    public void test_ep_post_hello_user() {
        given().when().post("/rest/hw/hello/foo").then().statusCode(200).body(
                is(String.format("Hello %s", "foo")));
        given().when().post("/rest/hw/hello/bar").then().statusCode(200).body(
                is(String.format("Hello %s", "bar")));
    }
}

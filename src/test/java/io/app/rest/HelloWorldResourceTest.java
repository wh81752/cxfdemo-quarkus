package io.app.rest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.eclipse.microprofile.config.ConfigProvider;
import org.hamcrest.CoreMatchers;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;
import org.xmlunit.matchers.HasXPathMatcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
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
        ValidatableResponse then;
        then = given().when().get("/rest/hw/hello").then();
        then.statusCode(200);
        then.contentType(ContentType.JSON);
        then.body(is("Hello World"));
    }

    @Test
    @Order(1)
    public void test_ep_hello_world() {
        ValidatableResponse then;
        then = given().when().get("/rest/hw/hello/World").then();
        then.statusCode(200);
        then.contentType(ContentType.JSON);
        then.body(is("Hello World"));
    }

    @Test
    @Order(1)
    public void test_ep_hello_null() {
        ValidatableResponse then;
        then = given().when().get("/rest/hw/hello/").then();
        then.statusCode(200);
        then.contentType(ContentType.JSON);
        then.body(is("Hello World"));
    }

    @Test
    @Order(5)
    public void test_ep_get_users() {
        ValidatableResponse then = given().when().get("/rest/hw/users").then();
        then.statusCode(200);
        then.contentType(ContentType.JSON);
        then.body(is("{\"1\":{\"name\":\"foo\"},\"2\":{\"name" + "\":\"bar\"}}"));
    }

    @Test
    @Order(5)
    public void test_ep_get_users_not_empty() {
        ValidatableResponse then;
        Map refvalue = Collections.emptyMap();

        then = given().when().get("/rest/hw/users").then();
        then.statusCode(200);
        then.contentType(ContentType.JSON);

        Map curvalue = then.extract().as(Map.class);
        assertThat(curvalue, CoreMatchers.not(refvalue));
        assertThat(curvalue.size(), CoreMatchers.is(2));
    }

    @Test
    @Order(5)
    public void test_ep_get_users_not_empty_jsonpath() {
        ValidatableResponse then;
        // Expected JSON:
        // {
        // { 1 , "foo" },
        // { 2 , "bar" }
        // }
        then = given().when().get("/rest/hw/users").then();
        then.contentType(ContentType.JSON);
        then.statusCode(200);
        then.body("1.name", is("foo"));
        then.body("2.name", is("bar"));
        then.body("size()", is(2));
    }

    /**
     * Example of how to decode textual return value into a Java object and using Hamcrest matchers to assert existence
     * (or absence) of a value.
     */
    @Test
    @Order(0)
    public void test_ep_get_users_empty() {
        ValidatableResponse then;
        Map refvalue = Collections.emptyMap();

        then = given().when().get("/rest/hw/users").then();
        then.statusCode(200);
        then.contentType(ContentType.JSON);

        Map curvalue = then.extract().as(Map.class);
        assertThat(curvalue, CoreMatchers.is(refvalue));
        assertThat(curvalue.size(), CoreMatchers.is(0));
    }

    @Test
    @Order(0)
    public void test_ep_get_usersxml_empty() {
        ValidatableResponse response;

        response = given().contentType(ContentType.XML).accept(ContentType.ANY).log().all().when().get("/rest/hw/users")
                          .then();

        response.statusCode(200);
        response.contentType(ContentType.XML);
        // Test whether XML document is empty.
        response.body(not(HasXPathMatcher.hasXPath("/*/node()")));
    }

    @Test
    @Order(1)
    public void test_ep_post_hello_user() {
        Arrays.asList("foo", "bar").stream().forEach((String item) -> {
            ValidatableResponse then;

            then = given().when().post("/rest/hw/hello/{name}", item).then();
            then.statusCode(200);
            then.contentType(ContentType.JSON);
            then.body(is(String.format("Hello %s", item)));
        });
    }


    /**
     * Test against secured (rolebased) endpoint
     */
    @Test
    @Order(1)
    public void test_ep_post_rbac_hello_user() {
        ValidatableResponse then;

        then = given().when().post("/rest/hw/rbac/hello/{name}", "foo").then();
        then.statusCode(401);
        // Content-Type is absent on error
        then.contentType(not(ContentType.JSON));
        then.contentType(is(""));

        then = given().when().auth().basic("u","p").post("/rest/hw/rbac/hello/{name}", "foo").then();
        then.statusCode(200);
        then.contentType(ContentType.JSON);
    }
}

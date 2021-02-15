package io.app.soap;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Class Documentation
 *
 * <p>
 * What is the point of this class?
 *
 * @author geronimo1
 */
@QuarkusTest
class HelloWorldImplTest {
    @Test
    void wsdl() {
        given().when().get("/soap/hw?wsdl").then().statusCode(200);
    }

    @Test()
    @Disabled
    void hello() {
        given().when().get("/soap/hw/hello").then().statusCode(200);
    }

    @Test
    @Disabled
    void sayHi() {
        given().when().get("/soap/hw/sayhi").then().statusCode(200);
    }

    @Test
    @Disabled
    void sayHiToUser() {
        given().when().get("/soap/hw/sayHiToUser").then().statusCode(200);
    }

    @Test
    @Disabled
    void securedHiToUser() {
        given().when().get("/soap/hw/sayHiToUser").then().statusCode(200);
    }

    @Test
    @Disabled
    void getUsers() {
        given().when().get("/soap/hw/users").then().statusCode(200);
    }
}

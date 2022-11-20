package com.javahelp.backend.endpoint;

import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import org.junit.Test;

import io.restassured.http.Header;

/**
 * Test suite for {@link HTTPHandler}
 */
public class HTTPHandlerTest {

    private static final String LOGIN = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/login";

    @Test
    public void testMissingBody() {
        when().post(LOGIN).then().statusCode(400).body("message", equalTo("Missing body"));
    }

    @Test
    public void testUnsupportedMedia() {
        given().body("sjkldfhglkdsjhfg")
                .when().post(LOGIN).then().statusCode(415);
    }

    @Test
    public void testParsingError() {
        given().header(new Header("Content-Type", "application/json"))
                .body("sjldkgfhsl").when().post(LOGIN).then().statusCode(400)
                .body("message", equalTo("Cannot parse body json"));
    }

    @Test
    public void testMissingFields() {
        given()
                .header(new Header("Content-Type", "application/json"))
                .body("{\"email\":\"test\"}").post(LOGIN).then().statusCode(400);
    }

}

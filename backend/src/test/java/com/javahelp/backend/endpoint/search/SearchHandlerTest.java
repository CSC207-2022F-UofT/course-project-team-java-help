package com.javahelp.backend.endpoint.search;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assume.assumeTrue;
import static io.restassured.RestAssured.given;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.search.RandomDataPopulater;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class SearchHandlerTest {
    private static final String SEARCH = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/providers/search";

    public boolean userDatabaseAccessible() {
        try {
            IUserStore.getDefaultImplementation().read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean surveyDatabaseAccessible() {
        try {
            ISurveyStore.getDefaultImplementation().read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean srDatabaseAccessible() {
        try {
            ISurveyResponseStore.getDefaultImplementation().read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testMissingUserID() {
        given().header(new Header("Content-Type", "application/json"))
                .body("{\"ranking\":\"false\"," +
                        "\"filters\":[\"filter_1\":\"attr1_0\"," +
                                     "\"filter_2\":\"attr2_0\"]}")
                .when().post(SEARCH).then().statusCode(400).body("message",
                        equalTo("Request must contain \"id\""));
    }

    @Test
    public void testValidSearch() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(surveyDatabaseAccessible());
        assumeTrue(srDatabaseAccessible());

        JsonObject json = Json.createObjectBuilder()
                .add("userID", "test")
                .add("ranking", false)
                .add("filters", Json.createObjectBuilder()
                        .add("filter_1", "attr1_0")
                        .add("filter_2", "attr2_0"))
                .build();

        given().header(new Header("Content-Type", "application/json"))
                .body(json.toString()).when().post(SEARCH).then().statusCode(200)
                .contentType(ContentType.JSON)
                .body("success", equalTo(true));
    }
}

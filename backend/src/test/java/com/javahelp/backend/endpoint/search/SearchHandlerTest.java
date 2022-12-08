package com.javahelp.backend.endpoint.search;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assume.assumeTrue;
import static io.restassured.RestAssured.given;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.data.search.RandomSurveyPopulation;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

import org.junit.Test;

import java.time.Duration;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Tests {@link SearchHandler}
 */
public class SearchHandlerTest {
    private static final String SEARCH = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/providers/search";

    IUserStore users = IUserStore.getDefaultImplementation();
    ISurveyStore surveys = ISurveyStore.getDefaultImplementation();
    ISurveyResponseStore responses = ISurveyResponseStore.getDefaultImplementation();
    ITokenStore tokens = ITokenStore.getDefaultImplementation();

    /**
     *
     * @return whether the user table is accessible
     */
    private boolean tokenDatabaseAccessible() {
        try {
            tokens.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @return whether the user table is accessible
     */
    private boolean userDatabaseAccessible() {
        try {
            users.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @return whether the survey table is accessible
     */
    private boolean surveyDatabaseAccessible() {
        try {
            surveys.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *
     * @return whether the response table is accessible
     */
    private boolean responseDatabaseAccessible() {
        try {
            responses.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testValidSearch() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(surveyDatabaseAccessible());
        assumeTrue(responseDatabaseAccessible());
        assumeTrue(tokenDatabaseAccessible());

        RandomSurveyPopulation population = new RandomSurveyPopulation(surveys, responses, users);

        try {
            population.populate();
            User client = population.getRandomClient();

            Token token = tokens.create(new Token(Duration.ofDays(1), "", client.getStringID()));

            JsonObject json = Json.createObjectBuilder()
                    .add("userID", client.getStringID())
                    .add("ranking", false)
                    .add("filters", Json.createArrayBuilder())
                    .build();

            given().header(new Header("Content-Type", "application/json"))
                    .header(new Header("Authorization", "JavaHelp id=" +
                            client.getStringID() + " token=" + token.getToken()))
                    .body(json.toString()).when().post(SEARCH).then().statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("success", equalTo(true));
        } finally {
            population.delete();
        }
    }
}

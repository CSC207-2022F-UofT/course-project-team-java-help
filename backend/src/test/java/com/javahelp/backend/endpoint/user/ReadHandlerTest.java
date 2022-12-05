package com.javahelp.backend.endpoint.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import static io.restassured.RestAssured.given;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;
import com.javahelp.model.util.json.UserConverter;

import org.junit.Test;

import java.time.Duration;

import io.restassured.http.Header;
import io.restassured.response.Response;

/**
 * Tests for {@link ReadHandler}
 */
public class ReadHandlerTest {

    IUserStore store = IUserStore.getDefaultImplementation();
    ITokenStore tokenStore = ITokenStore.getDefaultImplementation();

    private static final String USER_ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/";

    ClientUserInfo info = new ClientUserInfo("i want to sleep",
            "please let me sleep",
            ":(",
            "sldhfljkfhgd",
            "sdlfhjldskfjhg");

    User user = new User("", info, "dummy_user_to_be_created");

    UserPassword password = new UserPassword("wow i love hashing", SHAPasswordHasher.getInstance());

    Token token = new Token(Duration.ofDays(1), "Test token", user.getStringID());

    /**
     * @return whether the database is accessible
     */
    private boolean databaseAccessible() {
        try {
            store.read("ljljk");
            tokenStore.read("lhlkhj");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Creates and populates test {@link User} and test {@link Token}
     */
    private void createUser() {
        store.create(user, password);
        token.setUserID(user.getStringID());
        tokenStore.create(token);
    }

    /**
     * Tests a successful read for a user
     */
    @Test
    public void testReadExists() {
        assumeTrue(databaseAccessible());

        try {
            createUser();

            Response r = given().header(new Header("Content-Type", "application/json"))
                    .header(new Header("Authorization", "JavaHelp id=" + user.getStringID()
                            + " token=" + token.getToken()))
                    .when().get(USER_ENDPOINT + user.getStringID());

            r.then().statusCode(200)
                    .contentType("application/json");

            String body = r.getBody().asString();

            User read = UserConverter.getInstance().fromJSONString(body);

            assertEquals(user.getStringID(), read.getStringID());

            ClientUserInfo clientInfo = (ClientUserInfo) read.getUserInfo();

            assertEquals(info.getEmailAddress(), clientInfo.getEmailAddress());
            assertEquals(info.getFirstName(), clientInfo.getFirstName());

        } finally {
            store.delete(user.getStringID());
        }
    }

    /**
     * Tests a read with an unauthorized request
     */
    @Test
    public void testReadNotAuthorized() {
        assumeTrue(databaseAccessible());

        try {
            createUser();

            given().header(new Header("Content-Type", "application/json"))
                    .when().get(USER_ENDPOINT + user.getStringID()).then().statusCode(401);
        } finally {
            store.delete(user.getStringID());
        }
    }

    /**
     * Tests a read with a request authorized for the wrong user
     */
    @Test
    public void testReadForbidden() {
        assumeTrue(databaseAccessible());

        try {
            createUser();

            given().header(new Header("Content-Type", "application/json"))
                    .header(new Header("Authorization", "JavaHelp id=" + user.getStringID()
                            + " token=" + token.getToken()))
                    .when().get(USER_ENDPOINT + "shbibidimbap").then().statusCode(403);
        } finally {
            store.delete(user.getStringID());
        }
    }
}

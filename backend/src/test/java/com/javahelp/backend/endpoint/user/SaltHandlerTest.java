package com.javahelp.backend.endpoint.user;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assume.assumeTrue;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.endpoint.user.SaltHandler;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

import java.util.Base64;

/**
 * Test {@link SaltHandler}
 */
public class SaltHandlerTest {

    private static final String ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/salt";

    private static final String USER_ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/";

    IUserStore store = IUserStore.getDefaultImplementation();

    /**
     * @return whether the database is accessible from the current process
     */
    public boolean databaseAccessible() {
        try {
            store.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testMissingParameters() {
        when().get(ENDPOINT).then().statusCode(400)
                .body("message", equalTo("Must include id, email, or username query string parameter"));
    }

    @Test
    public void testSuccess() {
        assumeTrue(databaseAccessible());

        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");
        User u = new User("test", clientInfo, "test_user");
        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());

        try {
            store.create(u, p);

            given().queryParam("email", "test.client@mail.com")
                    .when().get(ENDPOINT)
                    .then().statusCode(200)
                    .body("salt", equalTo(Base64.getEncoder().encodeToString(p.getSalt())));

            when().get(USER_ENDPOINT + u.getStringID() + "/salt")
                    .then().statusCode(200)
                    .body("salt", equalTo(Base64.getEncoder().encodeToString(p.getSalt())));
        } finally {
            store.delete(u.getStringID());
        }
    }

    @Test
    public void testError() {
        given().queryParam("username", "sahjldfgo")
                .when().get(ENDPOINT)
                .then().statusCode(404)
                .body("errorMessage", equalTo("The specified user cannot be found"));
    }

}

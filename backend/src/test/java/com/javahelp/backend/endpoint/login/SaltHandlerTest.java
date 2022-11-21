package com.javahelp.backend.endpoint.login;

import static org.hamcrest.CoreMatchers.equalTo;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.authentication.SaltInteractor;
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

    @Test
    public void testMissingParameters() {
        when().get(ENDPOINT).then().statusCode(400)
                .body("message", equalTo("Must include id, email, or username query string parameter"));
    }

    @Test
    public void testSuccess() {
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");
        User u = new User("test", clientInfo, "test_user");
        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());

        store.create(u, p);

        given().queryParam("email", "test.client@mail.com")
                        .when().get(ENDPOINT)
                        .then().statusCode(200)
                        .body("salt", equalTo(Base64.getEncoder().encodeToString(p.getSalt())));

        when().get(USER_ENDPOINT + u.getStringID() + "/salt")
                .then().statusCode(200)
                .body("salt", equalTo(Base64.getEncoder().encodeToString(p.getSalt())));

        store.delete(u.getStringID());
    }

    @Test
    public void testError() {
        given().queryParam("username", "sahjldfgo")
                .when().get(ENDPOINT)
                .then().statusCode(200)
                .body("errorMessage", equalTo("The specified user cannot be found"));
    }

}

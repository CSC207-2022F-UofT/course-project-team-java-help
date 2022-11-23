package com.javahelp.backend.endpoint.delete;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import io.restassured.http.Header;
import io.restassured.http.Headers;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class DeleteHandlerTest {
    final IUserStore userStore = IUserStore.getDefaultImplementation();
    final ITokenStore tokenStore = ITokenStore.getDefaultImplementation();
    private static final String USER_ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/";

    ClientUserInfo userInfo;
    UserPassword password;
    User user;
    Token token;

    @Before
    public void setUp() {
        userInfo = new ClientUserInfo("123@mail.com", "University of Toronto",
                "123-456-7890", "J", "M");

        password = new UserPassword("password", SHAPasswordHasher.getInstance());
        user = new User("test", userInfo, "asdfgh");
        userStore.create(user, password);
        token = new Token(Duration.ofMinutes(30), "test", user.getStringID());
        tokenStore.create(token);
    }

    @Test
    public void testSuccess() {
        JsonObject body = Json.createObjectBuilder()
                        .add("userid", user.getStringID())
                        .add("token", token.getToken())
                        .build();

        given().header(new Header("Content-Type", "application/json"))
                .header(new Header("Authorization", "JavaHelp id=" + user.getStringID()
                + " token=" + token.getToken()))
                .when().delete(USER_ENDPOINT + user.getStringID()).then().statusCode(400)
                        .body("message", equalTo("Must include user id and token " +
                                "in authorization header"));
    }

    @After
    public void cleanUp() {
        tokenStore.delete(token.getToken());
        userStore.delete(user.getStringID());
    }
}

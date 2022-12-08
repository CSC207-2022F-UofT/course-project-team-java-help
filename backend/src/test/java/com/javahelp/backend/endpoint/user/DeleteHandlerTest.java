package com.javahelp.backend.endpoint.user;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static io.restassured.RestAssured.when;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;
import com.javahelp.model.user.UserPassword;

import org.hamcrest.CoreMatchers;
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

    /**
     * @return whether the database is accessible from the current process
     */
    public boolean databaseAccessible() {
        try {
            userStore.read("test");
            tokenStore.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test(timeout = 20000)
    public void testSuccess() {
        assumeTrue(databaseAccessible());

        UserInfo userInfo = new ClientUserInfo("123@mail.com", "University of Toronto",
                "123-456-7890", "J", "M");

        UserPassword password = new UserPassword("password", SHAPasswordHasher.getInstance());
        User user = new User("test", userInfo, "asdfgh");
        Token token = null;

        try {
            userStore.create(user, password);
            token = new Token(Duration.ofMinutes(30), "test", user.getStringID());
            tokenStore.create(token);

            given().header(new Header("Content-Type", "application/json"))
                    .header(new Header("Authorization", "JavaHelp id=" + user.getStringID()
                            + " token=" + token.getToken()))
                    .when().delete(USER_ENDPOINT + user.getStringID()).then().statusCode(200)
                    .body("success", equalTo(true));

        } finally {
            tokenStore.delete(token.getToken());
            userStore.delete(user.getStringID());
        }
    }

    @Test(timeout = 20000)
    public void testNoMatchPathParameters() {
        assumeTrue(databaseAccessible());

        UserInfo userInfo = new ClientUserInfo("123@mail.com", "University of Toronto",
                "123-456-7890", "J", "M");

        UserInfo userInfo1 = new ClientUserInfo("fail@mail.com", "University",
                "123-123-1234", "M", "J");
        UserPassword password = new UserPassword("password", SHAPasswordHasher.getInstance());
        UserPassword password1 = new UserPassword("qwertyui", SHAPasswordHasher.getInstance());
        User user = new User("test", userInfo, "asdfgh");
        User user1 = new User("test1", userInfo1, "qwerty");
        Token token = null;

        try {
            userStore.create(user, password);
            userStore.create(user1, password1);
            token = new Token(Duration.ofMinutes(30), "test", user.getStringID());
            tokenStore.create(token);

            given().header(new Header("Content-Type", "application/json"))
                    .header(new Header("Authorization", "JavaHelp id=" + user.getStringID()
                            + " token=" + token.getToken()))
                    .when().delete(USER_ENDPOINT + user1.getStringID()).then().statusCode(403)
                    .body("message", equalTo("The path parameters do not match the given current user"));

        } finally {
            tokenStore.delete(token.getToken());
            userStore.delete(user.getStringID());
        }
    }
}

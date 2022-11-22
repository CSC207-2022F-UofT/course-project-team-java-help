package com.javahelp.backend.endpoint.delete;


import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;

import io.restassured.http.Header;
import jakarta.json.Json;
import jakarta.json.JsonObject;

public class DeleteHandlerTest {
    final IUserStore userStore = IUserStore.getDefaultImplementation();
    final ITokenStore tokenStore = ITokenStore.getDefaultImplementation();

    @Test
    public void testSuccess() {
        ClientUserInfo userInfo = new ClientUserInfo("123@mail.com", "University of Toronto",
                "123-456-7890", "J", "M");

        UserPassword password = new UserPassword("password", SHAPasswordHasher.getInstance());
        User user = new User("test", userInfo, "asdfgh");

        userStore.create(user, password);

        Duration duration = Duration.ofMinutes(30);
        Token token = new Token(duration, "test", user.getStringID());
        tokenStore.create(token);

        JsonObject body = Json.createObjectBuilder()
                        .add("userID", user.getStringID())
                        .add("token", token.getToken())
                        .build();

        final String ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/" + user.getStringID() + "/delete";

        given().header(new Header("Content-Type", "application/json"))
                .header(new Header("Authorization", "JavaHelp id=" + user.getStringID()
                + " token=" + token.getToken()))
                .body(body.toString()).when().post(ENDPOINT).then().statusCode(200)
                .body("success", equalTo(true));

        tokenStore.delete(token.getToken());
        userStore.delete(user.getStringID());
    }
}

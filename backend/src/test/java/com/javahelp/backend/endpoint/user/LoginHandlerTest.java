package com.javahelp.backend.endpoint.user;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assume.assumeTrue;
import static io.restassured.RestAssured.given;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.endpoint.user.LoginHandler;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

import io.restassured.http.ContentType;
import io.restassured.http.Header;
import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Test suite for {@link LoginHandler}
 */
public class LoginHandlerTest {

    private static final String LOGIN = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/login";

    /**
     *
     * @return whether the database is accessible from the current process
     */
    public boolean databaseAccessible() {
        try {
            IUserStore.getDefaultImplementation().read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testMissingIdentifying() {
        given().header(new Header("Content-Type", "application/json"))
                .body("{\"saltHash\":\"sfdghld\", \"stayLoggedIn\":\"sjhgdsgf\"}")
                .when().post(LOGIN).then().statusCode(400).body("message",
                        equalTo("Request must contain one of \"id\", \"email\", \"username\""));
    }

    @Test
    public void testInvalidHash() {
        given().header(new Header("Content-Type", "application/json"))
                .body("{\"saltHash\":\"sfdghld\", \"stayLoggedIn\":\"sjhgdsgf\", \"email\":\"asdfs\"}")
                .when().post(LOGIN).then().statusCode(400).body("message",
                        equalTo("Invalid password saltHash, cannot parse"));
    }

    @Test
    public void testValidHash_IncorrectPassword() {
        assumeTrue(databaseAccessible());

        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());
        UserPassword q = new UserPassword("wrong_password", SHAPasswordHasher.getInstance());

        IUserStore db = IUserStore.getDefaultImplementation();

        ClientUserInfo info = new ClientUserInfo("sdjkfhkls", "sdf", "sdf",
                "sdfhjkl", "sdjlfhkslkjd");

        User u = new User("", info, "testing_user_login_123");

        try {
            db.create(u, p);

            db.read(u.getStringID());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {

            }

            JsonObject json = Json.createObjectBuilder()
                    .add("username", "testing_user_login_123")
                    .add("saltHash", q.getBase64SaltHash())
                    .add("stayLoggedIn", false)
                    .build();

            given().header(new Header("Content-Type", "application/json"))
                    .body(json.toString()).when().post(LOGIN).then().statusCode(200)
                    .body("success", equalTo(false));
        } finally {
            db.delete(u.getStringID());
        }
    }

    @Test
    public void testValidHash_CorrectPassword() {
        assumeTrue(databaseAccessible());

        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());

        IUserStore db = IUserStore.getDefaultImplementation();

        ClientUserInfo info = new ClientUserInfo("sdjkfhkls", "sdf", "sdf",
                "sdfhjkl", "sdjlfhkslkjd");

        User u = new User("", info, "testing_user_login_123");

        try {
            db.create(u, p);

            db.read(u.getStringID());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {

            }

            JsonObject json = Json.createObjectBuilder()
                    .add("username", "testing_user_login_123")
                    .add("saltHash", p.getBase64SaltHash())
                    .add("stayLoggedIn", false)
                    .build();

            given().header(new Header("Content-Type", "application/json"))
                    .body(json.toString()).when().post(LOGIN).then().statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("success", equalTo(true));
        } finally {
            db.delete(u.getStringID());
        }
    }
}

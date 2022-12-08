package com.javahelp.backend.endpoint.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import static io.restassured.RestAssured.given;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;
import com.javahelp.model.util.json.UserConverter;

import org.junit.Test;

import java.io.StringReader;

import io.restassured.http.Header;
import io.restassured.response.Response;
import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Test for {@link RegisterHandler}
 */
public class RegisterHandlerTest {

    private static final String ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/register/user";

    IUserStore users = IUserStore.getDefaultImplementation();

    /**
     * @return whether the database is accessible
     */
    private boolean databaseAccessible() {
        try {
            users.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testRegister() {

        assumeTrue(databaseAccessible());

        User u = new User("", new ClientUserInfo(
                "jacob.klimczak@gmail.com",
                "123 street",
                ":(",
                "jacob",
                "klimczak"),
                "johnny");

        UserPassword p = new UserPassword("iceCreamInATub", SHAPasswordHasher.getInstance());

        User read = null;

        try {
            JsonObject requestBodyJson = Json.createObjectBuilder()
                    .add("user", UserConverter.getInstance().toJSON(u))
                    .add("saltHash", p.getBase64SaltHash())
                    .build();

            Response r = given()
                    .header(new Header("Content-Type", "application/json"))
                    .body(requestBodyJson.toString())
                    .when().post(ENDPOINT);

            JsonObject responseBodyJson = Json.createReader(new StringReader(r.getBody().asString())).readObject();

            r.then().statusCode(200)
                    .contentType("application/json");

            read = UserConverter.getInstance().fromJSON(responseBodyJson.getJsonObject("user"));

            assertEquals(u.getUsername(), read.getUsername());
        } finally {
            if (read != null) {
                users.delete(read.getStringID());
            }
        }
    }

}

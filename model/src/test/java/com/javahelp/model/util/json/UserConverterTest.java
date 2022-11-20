package com.javahelp.model.util.json;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;

import org.junit.Test;

import jakarta.json.JsonObject;

/**
 * Test {@link UserConverter}
 */
public class UserConverterTest {

    ProviderUserInfo i = new ProviderUserInfo("email", "address",
            "sfgdfgd",
            "sdfsdf");
    User u = new User("sldkhjfslkdf", i, "sdljkfhlskdf");
    UserConverter converter = UserConverter.getInstance();

    @Test
    public void convert() {
        JsonObject converted = converter.toJSON(u);
        User restored = converter.fromJSON(converted);

        assertEquals(u.getStringID(), restored.getStringID());
        assertEquals(u.getUsername(), restored.getUsername());
        assertEquals(u.getUserInfo().getType(), restored.getUserInfo().getType());
        assertEquals(u.getUserInfo().getEmailAddress(), restored.getUserInfo().getEmailAddress());
    }

    @Test
    public void convertString() {
        String converted = converter.toJSONString(u);
        User restored = converter.fromJSONString(converted);

        assertEquals(u.getStringID(), restored.getStringID());
        assertEquals(u.getUsername(), restored.getUsername());
        assertEquals(u.getUserInfo().getType(), restored.getUserInfo().getType());
        assertEquals(u.getUserInfo().getEmailAddress(), restored.getUserInfo().getEmailAddress());
    }
}

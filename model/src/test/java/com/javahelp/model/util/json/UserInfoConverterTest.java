package com.javahelp.model.util.json;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.UserInfo;

import org.junit.Test;

import jakarta.json.JsonObject;

/**
 * Test converting {@link com.javahelp.model.user.UserInfo} to {@link jakarta.json.JsonObject}
 */
public class UserInfoConverterTest {

    UserInfo a = new ProviderUserInfo("email", "address",
            "sfgdfgd",
            "sdfsdf");
    UserInfo b = new ClientUserInfo("sdljhfs", "sdfsdf",
            "asdfsdf", "asdfsdf", "sadfsdf");
    UserInfoConverter converter = UserInfoConverter.getInstance();

    @Test
    public void convert() {
        JsonObject aConverted = converter.toJSON(a), bConverted = converter.toJSON(b);
        UserInfo aRestored = converter.fromJSON(aConverted), bRestored = converter.fromJSON(bConverted);

        assertEquals(a.getEmailAddress(), aRestored.getEmailAddress());
        assertEquals(b.getEmailAddress(), bRestored.getEmailAddress());

        assertEquals(a.getType(), aRestored.getType());
        assertEquals(b.getType(), bRestored.getType());
    }

    @Test
    public void convertString() {
        String aConverted = converter.toJSONString(a), bConverted = converter.toJSONString(b);
        UserInfo aRestored = converter.fromJSONString(aConverted), bRestored = converter.fromJSONString(bConverted);

        assertEquals(a.getEmailAddress(), aRestored.getEmailAddress());
        assertEquals(b.getEmailAddress(), bRestored.getEmailAddress());

        assertEquals(a.getType(), aRestored.getType());
        assertEquals(b.getType(), bRestored.getType());
    }

}

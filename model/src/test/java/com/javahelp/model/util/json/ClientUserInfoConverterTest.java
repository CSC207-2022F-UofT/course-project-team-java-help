package com.javahelp.model.util.json;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.user.ClientUserInfo;

import org.junit.Test;

import jakarta.json.JsonObject;

/**
 * Test converting {@link com.javahelp.model.user.ClientUserInfo}
 */
public class ClientUserInfoConverterTest {

    ClientUserInfo a = new ClientUserInfo("email", "address",
            "sfgdfgd",
            "sdfsdf",
            "sdfljghdlfghj");
    ClientUserInfo b = new ClientUserInfo("sdjklshg",
            "sldkjhldk",
            "sldjhflsjkdhg",
            "sldkfjhlsdkfjhgsgfs",
            "sldkjhlskjhdg");
    ClientUserInfoConverter converter = ClientUserInfoConverter.getInstance();

    @Test
    public void convert() {
        JsonObject aConverted = converter.toJSON(a), bConverted = converter.toJSON(b);
        ClientUserInfo aRestored = converter.fromJSON(aConverted), bRestored = converter.fromJSON(bConverted);

        assertEquals(a.getLastName(), aRestored.getLastName());
        assertEquals(b.getFirstName(), bRestored.getFirstName());

        assertEquals(a.getAddress(), aRestored.getAddress());
        assertEquals(b.getEmailAddress(), bRestored.getEmailAddress());
    }

    @Test
    public void convertString() {
        String aConverted = converter.toJSONString(a), bConverted = converter.toJSONString(b);
        ClientUserInfo aRestored = converter.fromJSONString(aConverted), bRestored = converter.fromJSONString(bConverted);

        assertEquals(a.getLastName(), aRestored.getLastName());
        assertEquals(b.getFirstName(), bRestored.getFirstName());

        assertEquals(a.getAddress(), aRestored.getAddress());
        assertEquals(b.getEmailAddress(), bRestored.getEmailAddress());
    }
}

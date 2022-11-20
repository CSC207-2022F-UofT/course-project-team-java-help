package com.javahelp.model.util.json;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;

import org.junit.BeforeClass;
import org.junit.Test;

import jakarta.json.JsonObject;

/**
 * Test {@link ProviderUserInfoConverter}
 */
public class ProviderUserInfoConverterTest {

    ProviderUserInfo a = new ProviderUserInfo("email", "address",
            "sfgdfgd",
            "sdfsdf");
    ProviderUserInfo b = new ProviderUserInfo("sdjklshg",
            "sldkjhldk",
            "sldjhflsjkdhg",
            "sldkfjhlsdkfjhgsgfs");
    ProviderUserInfoConverter converter = ProviderUserInfoConverter.getInstance();

    @BeforeClass
    public void setup() {
        a.setCertified(false);
        b.setCertified(true);
    }

    @Test
    public void convert() {
        JsonObject aConverted = converter.toJSON(a), bConverted = converter.toJSON(b);
        ProviderUserInfo aRestored = converter.fromJSON(aConverted), bRestored = converter.fromJSON(bConverted);

        assertEquals(a.isCertified(), aRestored.isCertified());
        assertEquals(b.isCertified(), bRestored.isCertified());

        assertEquals(a.getPracticeName(), aRestored.getPracticeName());
        assertEquals(b.getEmailAddress(), bRestored.getEmailAddress());
    }

    @Test
    public void convertString() {
        String aConverted = converter.toJSONString(a), bConverted = converter.toJSONString(b);
        ProviderUserInfo aRestored = converter.fromJSONString(aConverted), bRestored = converter.fromJSONString(bConverted);

        assertEquals(a.isCertified(), aRestored.isCertified());
        assertEquals(b.isCertified(), bRestored.isCertified());

        assertEquals(a.getPracticeName(), aRestored.getPracticeName());
        assertEquals(b.getEmailAddress(), bRestored.getEmailAddress());
    }
}

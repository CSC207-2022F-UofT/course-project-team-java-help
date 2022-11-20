package com.javahelp.model.util.json;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.token.Token;

import org.junit.Test;

import java.time.Instant;

import jakarta.json.JsonObject;

/**
 * Test {@link TokenConverter}
 */
public class TokenConverterTest {

    Token a = new Token("sfgjkd", Instant.ofEpochMilli(23870), Instant.ofEpochMilli(203028), "sdfsdf", "sdhslg");
    Token b = new Token("sdljhdlsjfg", Instant.ofEpochMilli(234234), Instant.now(), "sdfjh", "sdfs");
    TokenConverter converter = TokenConverter.getInstance();

    @Test
    public void convert() {
        JsonObject aConverted = converter.toJSON(a), bConverted = converter.toJSON(b);
        Token aRestored = converter.fromJSON(aConverted), bRestored = converter.fromJSON(bConverted);

        assertEquals(a.getUserID(), aRestored.getUserID());
        assertEquals(b.getUserID(), bRestored.getUserID());

        assertEquals(a.getToken(), aRestored.getToken());
        assertEquals(b.getTag(), bRestored.getTag());

        assertEquals(a.getExpiryDate().toEpochMilli(), aRestored.getExpiryDate().toEpochMilli());
        assertEquals(b.getIssuedDate().toEpochMilli(), bRestored.getIssuedDate().toEpochMilli());
    }

    @Test
    public void convertString() {
        String aConverted = converter.toJSONString(a), bConverted = converter.toJSONString(b);
        Token aRestored = converter.fromJSONString(aConverted), bRestored = converter.fromJSONString(bConverted);

        assertEquals(a.getUserID(), aRestored.getUserID());
        assertEquals(b.getUserID(), bRestored.getUserID());

        assertEquals(a.getToken(), aRestored.getToken());
        assertEquals(b.getTag(), bRestored.getTag());

        assertEquals(a.getExpiryDate().toEpochMilli(), aRestored.getExpiryDate().toEpochMilli());
        assertEquals(b.getIssuedDate().toEpochMilli(), bRestored.getIssuedDate().toEpochMilli());
    }

}

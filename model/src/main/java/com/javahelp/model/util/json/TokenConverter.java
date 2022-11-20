package com.javahelp.model.util.json;

import com.javahelp.model.token.Token;

import java.time.Instant;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Converts {@link Token}s to {@link JsonObject}s
 */
public class TokenConverter implements IJSONConverter<Token> {

    private static TokenConverter instance = new TokenConverter();

    /**
     * Private constructor
     */
    private TokenConverter() {


    }

    /**
     * @return {@link TokenConverter} instance
     */
    public static TokenConverter getInstance() {
        return instance;
    }

    @Override
    public JsonObject toJSON(Token input) {
        return Json.createObjectBuilder()
                .add("token", input.getToken())
                .add("tag", input.getTag())
                .add("user", input.getUserID())
                .add("expiry", input.getExpiryDate().toEpochMilli())
                .add("issued", input.getIssuedDate().toEpochMilli())
                .build();
    }

    @Override
    public Token fromJSON(JsonObject object) {
        if (object.containsKey("token") && object.containsKey("tag")
                && object.containsKey("user") && object.containsKey("expiry")
                && object.containsKey("issued")) {
            String token = object.getString("token");
            String tag = object.getString("tag");
            String user = object.getString("user");
            long expiry = object.getJsonNumber("expiry").longValue();
            long issued = object.getJsonNumber("issued").longValue();
            Instant expiryInstant = Instant.ofEpochMilli(expiry);
            Instant issuedInstant = Instant.ofEpochMilli(issued);
            return new Token(token, issuedInstant, expiryInstant, tag, user);
        }
        return null;
    }
}

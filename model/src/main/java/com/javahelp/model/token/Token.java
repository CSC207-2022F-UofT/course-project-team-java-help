package com.javahelp.model.token;

import com.javahelp.model.user.User;

import java.time.*;

/**
 * Representation of token entity
 */

public class Token {
    /**
     * Token requested by the user
     */
    private String token;

    /**
     * Expiry date of the token.
     */
    private Instant expiry;

    /**
     * Issued date of the token.
     */
    private Instant issued;

    /**
     * ID of {@link User} for {@link Token}.
     */
    private String userId;

    /**
     * This holds any additional information about the toekn.
     */
    private String tag;


    /**
     * Creates a new {@link Token}
     *
     * @param token  {@link String} of this {@link Token}
     * @param issued {@link Instant} of this {@link Token}
     * @param expiry {@link Instant} of this {@link Token}
     * @param tag    {@link String} of this {@link Token}
     * @param userId ID of {@link User} who requested this {@link Token}
     */
    public Token(String token, Instant issued, Instant expiry, String tag, String userId) {
        this.token = token;
        this.issued = issued;
        this.expiry = expiry;
        this.userId = userId;
        this.tag = tag;
    }

    /**
     * @return unique {@link String} token for this {@link Token}
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Sets the token {@link String} for this {@link Token}
     * @param s {@link String} for the {@link Token}
     */
    public void setToken(String s) {
        token = s;
    }

    /**
     * @return {@link String} tag for this {@link Token}
     */
    public String getTag() {
        return this.tag;
    }

    /**
     * @return {@link Instant} issued date for this {@link Token}
     */
    public Instant getIssuedDate() {
        return this.issued;
    }

    /**
     * @return {@link Instant} expiry date for this {@link Token}
     */
    public Instant getExpiryDate() {
        return this.expiry;
    }

    /**
     * @return {@link String} ID of the {@link User} this {@link Token} is for
     */
    public String getUserID() {
        return this.userId;
    }
}

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
    private LocalDate expiry;

    /**
     * Issued date of the token.
     */
    private LocalDate issued;

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
     * @param issued {@link LocalDate} of this {@link Token}
     * @param expiry {@link LocalDate} of this {@link Token}
     * @param tag    {@link String} of this {@link Token}
     * @param userId ID of {@link User} who requested this {@link Token}
     */
    public Token(String token, LocalDate issued, LocalDate expiry, String tag, String userId) {
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
     * @return {@link String} tag for this {@link Token}
     */
    public String getTag() {
        return this.tag;
    }

    /**
     * @return {@link LocalDate} issued date for this {@link Token}
     */
    public LocalDate getIssuedDate() {
        return this.issued;
    }

    /**
     * @return {@link LocalDate} expiry date for this {@link Token}
     */
    public LocalDate getExpiryDate() {
        return this.expiry;
    }

    /**
     * @return {@link String} ID of the {@link User} this {@link Token} is for
     */
    public String getUserID() {
        return this.userId;
    }
}

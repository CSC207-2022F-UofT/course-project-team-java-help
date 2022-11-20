package com.javahelp.backend.domain.user.authentication;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * Result of a request to authenticate with a {@link Token}
 */
public class TokenAuthResult {
    private User user;
    private Token token;
    private String errorMessage;

    /**
     * Creates a new {@link TokenAuthResult}
     *
     * @param user  {@link User} that authenticated
     * @param token {@link Token} authenticated with
     */
    public TokenAuthResult(User user, Token token) {
        this.token = token;
        this.user = user;
    }

    /**
     * Creates a new {@link TokenAuthResult}
     *
     * @param errorMessage {@link String} error message
     */
    public TokenAuthResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return whether the authentication was successful
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }

    /**
     * @return {@link User} authenticated
     */
    public User getUser() {
        return user;
    }

    /**
     * @return {@link String} error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return {@link Token} used to authenticate
     */
    public Token getToken() {
        return token;

    }

}

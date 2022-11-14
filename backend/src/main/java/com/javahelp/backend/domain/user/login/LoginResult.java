package com.javahelp.backend.domain.user.login;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * Bundle of {@link Token} and {@link User} created by a {@link LoginInteractor}
 */
public class LoginResult {

    private final User user;

    private final Token token;

    private final String errorMessage;

    /**
     * Creates a new successful {@link LoginResult} with the specified {@link User} and {@link Token}
     *
     * @param u {@link User} to bundle
     * @param t {@link Token} to bundle
     */
    LoginResult(User u, Token t) {
        user = u;
        token = t;
        errorMessage = null;
    }

    /**
     * Creates a new failed {@link LoginResult} with the specified error message
     *
     * @param errorMessage {@link String} error message to include
     */
    LoginResult(String errorMessage) {
        user = null;
        token = null;
        this.errorMessage = errorMessage;

    }

    /**
     * @return the {@link User}
     */
    public User getUser() {
        return user;
    }

    /**
     * @return the {@link Token}
     */
    public Token getToken() {
        return token;
    }

    /**
     * @return {@link String} error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return whether this login was successful
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }
}

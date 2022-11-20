package com.javahelp.backend.endpoint.login;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * Response to be sent by {@link LoginHandler}
 */
public class LoginResponse {

    boolean success;

    String errorMessage;

    User user;

    Token token;

    /**
     * Creates a new {@link LoginResponse}
     *
     * @param user  {@link User} that logged in
     * @param token {@link Token} retrieved
     */
    public LoginResponse(User user, Token token) {
        success = true;
        this.user = user;
        this.token = token;
    }

    /**
     * Creates a new {@link LoginResponse} for a login failure
     *
     * @param errorMessage {@link String} error message
     */
    public LoginResponse(String errorMessage) {
        success = false;
        this.errorMessage = errorMessage;
    }
}

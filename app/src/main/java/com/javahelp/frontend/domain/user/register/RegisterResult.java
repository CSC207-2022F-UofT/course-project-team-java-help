package com.javahelp.frontend.domain.user.register;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;


/**
 * Result of a request to log in made with a {@link IRegisterDataAccess}
 */
public class RegisterResult {
    private  String errorMessage;
    private Token token;
    private User user;

    /**
     * Creates a new failure {@link RegisterResult}
     *
     * @param errorMessage {@link String} error message received
     */
    public RegisterResult(String errorMessage) {this.errorMessage = errorMessage;}

    /**
     * Creates a new successful {@link RegisterResult}
     *
     * @param user  {@link User} that authenticated
     * @param token {@link Token} used to authenticate
     */
    public RegisterResult(User user, Token token) {
        this.user = user;
        this.token = token;
    }

    /**
     * @return whether the user authenticated successfully
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }

    /**
     * @return {@link String} error message received
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return {@link User} that authenticated
     */
    public User getUser() {return user;}

    /**
     * @return the {@link Token} used to authenticate
     */
    public Token getToken() {
        return token;
    }

}




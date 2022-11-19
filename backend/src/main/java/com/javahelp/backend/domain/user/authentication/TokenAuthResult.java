package com.javahelp.backend.domain.user.authentication;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

import org.graalvm.compiler.api.replacements.Snippet;

public class TokenAuthResult {
    private final String desiredUserID;
    private final Token token;
    private final boolean authenticated;
    private final String errorMessage;

    /**
     * Constructs a TokenAuthResult object.
     *
     * @param desiredUserID: the ID of the user that needs to be authenticated
     * @param token: the token used to authenticate the user
     */
    public TokenAuthResult(String desiredUserID, Token token) {
        this.token = token;
        this.desiredUserID = desiredUserID;
        this.authenticated = desiredUserID.equals(token.getUserID());
        if (authenticated) {
            this.errorMessage = null;
        } else {
            this.errorMessage = "Token Authentication failed";
        }
    }

    /**
     * Constructor for when fetching the token from the database fails
     */
    public TokenAuthResult(String desiredUserID) {
        this.errorMessage = "Could not fetch from database";
        this.desiredUserID = desiredUserID;
        this.token = null;
        this.authenticated = false;
    }

    /**
     * Gets the result of the authentication
     *
     * @return the result of the authentication
     */
    public boolean getAuthenticated() {
        return this.authenticated;
    }

}

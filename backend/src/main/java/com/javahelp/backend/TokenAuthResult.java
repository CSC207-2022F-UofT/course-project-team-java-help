package com.javahelp.backend;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

import org.graalvm.compiler.api.replacements.Snippet;

public class TokenAuthResult {
    private boolean authenticated;
    private User user;
    private Token token;
    private String errorMessage;

    private String ID;

    /**
     * Constructs a TokenAuthResult object.
     *
     * @param user: the user that needs to be authenticated
     * @param token: the token used to authenticate the user
     */
    public TokenAuthResult(User user, Token token) {
        this.authenticated = user.getStringID().equals(token.getUserID());
        this.user = user;
        this.token = token;
        if (authenticated) {
            this.errorMessage = null;
        } else {
            this.errorMessage = "Token Authentication failed";
        }
    }

    public Boolean getAuthenticated() { return this.authenticated; }


}

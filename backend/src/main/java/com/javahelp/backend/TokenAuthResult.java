package com.javahelp.backend;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

public class TokenAuthResult {
    private boolean authenticated;
    private User user;
    private Token token;
    private String errorMessage;

    public TokenAuthResult(User user, Token token) {
        if (user.getStringID().equals(token.getUserID())) {
            this.authenticated = true;
            this.errorMessage = null;
        } else {
            this.authenticated = false;
            this.errorMessage = "Token Authentication Failed";
        }
        this.user = user;
        this.token = token;
    }
}

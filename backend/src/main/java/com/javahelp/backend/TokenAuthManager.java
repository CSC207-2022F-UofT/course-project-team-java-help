package com.javahelp.backend;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

public class TokenAuthManager {
    private final IUserStore userStore;
    private final ITokenStore tokenStore;
    private TokenAuthResult authenticate;

    /**
     * Constructs a TokenAuthManager object.
     *
     * @param userStore: interface that manages users
     * @param tokenStore: interface that manages tokens
     */
    public TokenAuthManager(IUserStore userStore, ITokenStore tokenStore) {
        this.userStore = userStore;
        this.tokenStore =tokenStore;
    }

    public void setAuthenticate(User desiredUser, Token token) {
        this.authenticate = new TokenAuthResult(desiredUser, token);
    }

    /**
     * compares the input user and token and returns the result of the comparison
     *
     * @return TokenAuthResult object with the results of the comparison
     */
    private TokenAuthResult authenticate(User desiredUser, Token token) {
        setAuthenticate(desiredUser, token);
        return authenticate;
    }
}

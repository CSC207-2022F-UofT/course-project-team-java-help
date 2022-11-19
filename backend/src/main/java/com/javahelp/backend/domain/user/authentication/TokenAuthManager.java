package com.javahelp.backend.domain.user.authentication;

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
        this.tokenStore = tokenStore;
    }

    /**
     * compares the input user and token and returns the result of the comparison
     *
     * @return TokenAuthResult object with the results of the comparison
     */
    public TokenAuthResult authenticate(String desiredUserID, String token) {
        this.authenticate = new TokenAuthResult(desiredUserID, tokenStore.read(token));
        return authenticate;
    }
}
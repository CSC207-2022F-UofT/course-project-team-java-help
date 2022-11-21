package com.javahelp.backend.domain.user.authentication;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * Interactor for authenticating with {@link Token}s
 */
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
     * Authenticates with the specified {@link Token} for the specified {@link User}
     * @param input {@link ITokenAuthInput} supplying input information
     * @return TokenAuthResult object with the results of the comparison
     */
    public TokenAuthResult authenticate(ITokenAuthInput input) {
        User u = userStore.read(input.getUserID());
        Token t = tokenStore.read(input.getToken());
        String l = input.getToken();
        if (u == null) {
            return new TokenAuthResult("Unable to locate user");
        } else if (t == null || !u.getStringID().equals(t.getUserID())) {
            return new TokenAuthResult("Authentication failed");
        }

        this.authenticate = new TokenAuthResult(u, t);
        return authenticate;
    }
}

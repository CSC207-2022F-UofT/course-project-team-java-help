package com.javahelp.backend.auth;

import com.javahelp.backend.core.gateway.ICRUDGateway;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * Interface for classes interact with {@link Token}s on a database
 */
public interface ITokenStore extends ICRUDGateway<Token, String> {

    /**
     * Determines whether a {@link Token} matching the parameters exists.
     * <p></p>
     * Default implementation calls {code read} on the provided {@link Token}
     * and returns true if the {@link Token} exists for the specified {@link User}
     * and false otherwise
     * @param token {@link Token} {@link String}
     * @param userId non null ID of the {@link User} to check {@link Token} for
     * @return whether there is a matching {@link Token} for the {@link User} with the specified ID
     */
    default boolean containsMatching(String token, String userId) {
        Token t = read(token);
        return t != null && userId.equals(t.getUser().getStringID());
    }

}

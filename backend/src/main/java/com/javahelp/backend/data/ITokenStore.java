package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.javahelp.backend.data.ICRUDGateway;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * Interface for classes interact with {@link Token}s on a database
 */
public interface ITokenStore extends ICRUDGateway<Token, String> {

    /**
     *
     * @return the default implementation of {@link ITokenStore}
     */
    static ITokenStore getDefaultImplementation() {
        return new DynamoDBTokenStore("javahelpBackendTokens", Regions.US_EAST_1);
    }

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
        return t != null && userId.equals(t.getUserID());
    }

}

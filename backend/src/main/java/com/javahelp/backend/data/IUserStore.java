package com.javahelp.backend.data;

import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

/**
 * Interface for interacting with {@link User}s in a database
 *
 * Implementations of create without a specified password should specify some form
 * of default password that is used
 */
public interface IUserStore extends ICRUDGateway<User, String> {

    /**
     * Creates a {@link User}, then mutates that {@link User}
     * to provide it with its new ID
     * @param u {@link User} to create
     * @param password {@link UserPassword} to assign to the new {@link User}
     * @return {@link User} that was created
     */
    default User create(User u, UserPassword password) {
        u = create(u);
        updatePassword(u.getStringID(), password);
        return u;
    }

    /**
     * Updates the password of a {@link User}
     * @param userId {@link String} ID of the {@link User} to update
     * @param password {@link UserPassword} to assign
     */
    void updatePassword(String userId, UserPassword password);

    /**
     * Gets the {@link UserPassword} for the specified {@link User}
     * @param userId {@link String} ID of the {@link User}
     * @return the {@link UserPassword}
     */
    UserPassword readPassword(String userId);

}

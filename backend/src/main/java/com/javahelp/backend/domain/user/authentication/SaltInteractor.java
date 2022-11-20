package com.javahelp.backend.domain.user.authentication;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

/**
 * Retrieves the salt for a specified {@link com.javahelp.model.user.User}
 */
public class SaltInteractor {

    private final IUserStore users;

    /**
     * Creates a new {@link SaltInteractor}
     * @param users {@link IUserStore} to use
     */
    public SaltInteractor(IUserStore users) {
        this.users = users;
    }

    /**
     * Gets the salt for the specified {@link User}
     * @param input {@link ISaltInput} to use
     * @return {@link SaltResult} with the result
     */
    public SaltResult get(ISaltInput input) {
        UserPassword password = users.readPassword(input.getUserID());
        if (password == null) {
            return new SaltResult("The specified user cannot be found");
        } else {
            return new SaltResult(password.getSalt());
        }
    }
}

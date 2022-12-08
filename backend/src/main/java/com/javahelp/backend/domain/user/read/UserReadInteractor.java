package com.javahelp.backend.domain.user.read;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.User;

/**
 * Interactor for reading a {@link User} and passing back their info
 */
public class UserReadInteractor {

    private final IUserStore users;

    /**
     * Creates a new {@link UserReadInteractor}
     * @param users {@link IUserStore} to use
     */
    public UserReadInteractor(IUserStore users) {
        this.users = users;
    }

    /**
     * Returns the {@link User} with the specified id, or null if not found
     * @param input {@link IUserReadInputBoundary} containing information about the specified {@link User}
     * @return the {@link User} or null
     */
    public User readUser(IUserReadInputBoundary input) {
        return users.read(input.getID());
    }

}

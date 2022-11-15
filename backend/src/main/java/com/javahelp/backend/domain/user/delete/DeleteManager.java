package com.javahelp.backend.domain.user.delete;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.User;

/**
 * A class for managing an account deletion action.
 */
public class DeleteManager {

    /**
     * The {@link IUserStore} to use.
     */
    private final IUserStore userStore;

    /**
     * Constructs a {@link DeleteManager} instance.
     *
     * @param userStore: the {@link IUserStore} used to store users.
     */
    public DeleteManager(IUserStore userStore) {
        this.userStore = userStore;
    }


    /**
     * Deletes the user with the given userID from the database.
     *
     * @param input: an {@link IDeleteInputBoundary} instance that contains the userID of the user to be deleted.
     * @return a {@link DeleteResult} instance encoding a successful account deletion.
     */
    public DeleteResult delete(IDeleteInputBoundary input) {
        User user = userStore.read(input.getUserID());
        userStore.delete(input.getUserID());
        return new DeleteResult(user);
    }
}

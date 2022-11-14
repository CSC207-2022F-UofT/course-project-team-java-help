package com.javahelp.backend.accountdeletion;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

/**
 * A class for managing an account deletion action.
 */
class DeleteManager {

    /**
     * The {@link IUserStore} to use.
     */
    private final IUserStore userStore;

    /**
     * Constructs a {@link DeleteManager} instance.
     *
     * @param userStore: the UserStore used to store users.
     */
    protected DeleteManager(IUserStore userStore) {
        this.userStore = userStore;
    }


    /**
     * @param user: the {@link User} to be deleted.
     * @param userPassword: the {@link UserPassword} entered by the user.
     *
     * @return whether the {@link UserPassword} entered by the user matches the {@link UserPassword} in the database.
     */
    private boolean verify(User user, UserPassword userPassword) {
        UserPassword dbPassword = userStore.readPassword(user.getStringID());
        return userPassword.getBase64SaltHash().equals(dbPassword.getBase64SaltHash());
    }

    /**
     * Tries to delete the user with the given userID and the entered password.
     * If the entered password does not match the password for this user in the database,
     * the deletion is unsuccessful.
     *
     * Else, the user with the given userID will be deleted from the database.
     *
     * @param input: an {@link IDeleteInputBoundary} instance that contains one of
     *                           username, email, or userID of the user to be deleted.
     * @param userPassword: the {@link UserPassword} entered by the user.
     * @return a {@link DeleteResult} instance encoding whether the deletion is successful.
     */
    protected DeleteResult delete(IDeleteInputBoundary input, UserPassword userPassword) {
        User user = locateUser(input.getUserID(), input.getEmail(), input.getUsername());

        if (verify(user, userPassword)) {
            userStore.delete(user.getStringID());
            return new DeleteResult(user);
        }

        return new DeleteResult("The password is incorrect.");
    }

    /**
     * A helper method to locates a user given at least one of userID, email, or username.
     *
     * @param userID: the {@link String} userID of the user.
     * @param email: the {@link String} email of the user.
     * @param username: the {@link String} username of the user.
     *
     * @return the located {@link User} in the database, or null if none located.
     */
    private User locateUser(String userID, String email, String username) {
        if (userID == null && email == null && username == null) {
            throw new IllegalArgumentException("At least one of userID, email, or username must be provided");
        }

        User u = null;

        if (userID != null) {
            u = userStore.read(userID);
        }
        else if (email != null) {
            u = userStore.readByEmail(email);
        }
        else {
            u = userStore.readByUsername(username);
        }

        return u;
    }
}

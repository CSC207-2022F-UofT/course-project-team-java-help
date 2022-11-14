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
     * @param userStore: the {@link IUserStore} used to store users.
     */
    protected DeleteManager(IUserStore userStore) {
        this.userStore = userStore;
    }


    /**
     * A helper method for verifying whether the password entered by the user matches the password in the database.
     *
     * @param userID: the {@link String} userID of the user to be deleted.
     * @param userPassword: the {@link UserPassword} entered by the user.
     * @return whether the {@link UserPassword} entered by the user matches the {@link UserPassword} in the database.
     */
    private boolean verify(String userID, UserPassword userPassword) {
        UserPassword dbPassword = userStore.readPassword(userID);
        return userPassword.getBase64SaltHash().equals(dbPassword.getBase64SaltHash());
    }

    /**
     * Tries to delete the user with the given userID and the entered password from the database.
     * If the entered password does not match the password for this user in the database,
     * the deletion is unsuccessful.
     *
     * Else, the user with the given userID will be deleted from the database.
     *
     * @param input: an {@link IDeleteInputBoundary} instance that contains the userID of the user to be deleted,
     *               and the password entered by the user.
     * @return a {@link DeleteResult} instance encoding whether the deletion is successful.
     */
    protected DeleteResult delete(IDeleteInputBoundary input) {

        if (verify(input.getUserID(), input.getUserPassword())) {
            User user = userStore.read(input.getUserID());
            userStore.delete(input.getUserID());
            return new DeleteResult(user);
        }

        return new DeleteResult("The password is incorrect");
    }

    protected DeleteResult delete(String userID, UserPassword up) {
        if (verify(userID, up)) {
            User user = userStore.read(userID);
            userStore.delete(userID);
            return new DeleteResult(user);
        }
        return new DeleteResult("The password is incorrect");
    }

}

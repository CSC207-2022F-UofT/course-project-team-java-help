package com.javahelp.backend.accountdeletion;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import java.util.Arrays;

/**
 * A DeleteManager for an account deletion action.
 */
class DeleteManager {
    /**
     * The UserStore to use.
     */
    private final IUserStore iUserStore;

    /**
     * Constructs a DeleteManager instance.
     *
     * @param iUserStore: the UserStore used to store users.
     */
    protected DeleteManager(IUserStore iUserStore) {
        this.iUserStore = iUserStore;
    }

    /**
     * A helper method used to verify whether the entered password matches the password
     * stored in the database.
     *
     * @param userID: the userID of the user.
     * @param userPassword: the password entered by the user.
     * @return whether the password entered matches the password in the database.
     */
    private boolean verify(String userID, UserPassword userPassword) {
        UserPassword dbPassword = iUserStore.readPassword(userID);
        return Arrays.equals(dbPassword.getHash(), userPassword.getHash()) &&
                Arrays.equals(dbPassword.getSalt(), userPassword.getSalt());
    }

    /**
     * Tries to delete the user with the given userID and the entered password.
     * If the entered password does not match the password for this user in the database,
     * the deletion is unsuccessful.
     *
     * Else, the user with the given userID will be deleted from the database.
     *
     * @param userID: the userID of the user.
     * @param userPassword: the password entered by the user.
     * @return a DeleteResult instance encoding whether the deletion is successful.
     */
    protected DeleteResult delete(String userID, UserPassword userPassword) {
        User user = iUserStore.read(userID);

        if (verify(userID, userPassword)) {
            iUserStore.delete(userID);
            return new DeleteResult(true, user);
        }

        return new DeleteResult(false, user);
    }
}

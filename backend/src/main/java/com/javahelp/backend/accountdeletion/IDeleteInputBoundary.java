package com.javahelp.backend.accountdeletion;

import com.javahelp.model.user.UserPassword;

/**
 * An interface for classes that want to supply input of a {@link String} userID to a {@link DeleteManager}.
 *
 */
interface IDeleteInputBoundary {

    /**
     * @return the {@link String} userID of the user to be deleted, or null if none provided.
     */
    String getUserID();

    /**
     * @return the {@link UserPassword} entered by the user.
     */
    UserPassword getUserPassword();
}

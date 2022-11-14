package com.javahelp.backend.accountdeletion;

import com.javahelp.model.user.UserPassword;

/**
 * An interface for classes that want to supply input to a {@link DeleteManager}.
 *
 * Must supply one of username, email, or userID.
 */
interface IDeleteInputBoundary {

    /**
     * @return the {@link String} username of the user to be deleted, or null if none provided.
     */
    String getUsername();

    /**
     * @return the {@link String} email of the user to be deleted, or null if none provided.
     */
    String getEmail();

    /**
     * @return the {@link String} userID of the user to be deleted, or null if none provided.
     */
    String getUserID();

    /**
     * @return the {@link UserPassword} of the user to be deleted.
     */
    UserPassword getUserPassword();
}

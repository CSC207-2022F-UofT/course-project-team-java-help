package com.javahelp.backend.domain.user.delete;

/**
 * An interface for classes that want to supply input of a {@link String} userID to a {@link DeleteManager}.
 *
 */
interface IDeleteInputBoundary {

    /**
     * @return the {@link String} userID of the user to be deleted, or null if none provided.
     */
    String getUserID();
}

package com.javahelp.frontend.domain.user.delete;

import com.javahelp.model.user.User;

/**
 * The input boundary for deleting a {@link User}.
 */
public interface IDeleteInput {

    /**
     * Deletes the {@link User} with the given {@link String} userID.
     *
     * @param userID: the {@link String} userID of the {@link User} to be deleted.
     */
    void delete(String userID);
}

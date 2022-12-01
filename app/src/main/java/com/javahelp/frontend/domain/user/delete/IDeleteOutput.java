package com.javahelp.frontend.domain.user.delete;

import com.javahelp.model.user.User;

/**
 * An interface that provides information about an account deletion.
 *
 * The output boundary for a {@link DeleteInteractor}.
 */
public interface IDeleteOutput {

    /**
     * Called when the account deletion was successful.
     */

    void success(User user);

    /**
     * Called when the account deletion failed
     */
    void failure();

    /**
     * Called when something is wrong with the deletion.
     *
     * @param errorMessage: the {@link String} error message to be received.
     */
    void error(String errorMessage);

    /**
     * Aborts this account deletion.
     */
    void abort();
}

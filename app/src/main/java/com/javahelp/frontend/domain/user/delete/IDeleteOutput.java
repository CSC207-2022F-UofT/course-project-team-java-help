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
     *
     * @param user: the {@link User} deleted.
     */
    void deleteSuccess(User user);

    /**
     * Called when the account deletion failed.
     *
     * @param errorMessage: the {@link String} error message from a failing account deletion.
     */
    void deleteFailure(String errorMessage);

    /**
     * Called when something is wrong with the deletion.
     *
     * @param errorMessage: the {@link String} error message to be received.
     */
    void deleteError(String errorMessage);

    /**
     * Aborts this account deletion.
     */
    void deleteAbort();
}

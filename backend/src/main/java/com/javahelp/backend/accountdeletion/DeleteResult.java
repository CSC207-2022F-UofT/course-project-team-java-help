package com.javahelp.backend.accountdeletion;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * A class used by a {@link DeleteManager} that encodes whether an account deletion was successful.
 */
class DeleteResult {
    private final String errorMessage;
    private final User user;

    /**
     * Constructs a {@link DeleteResult} instance for a successful deletion.
     *
     * @param user: the {@link User} to be deleted.
     */
    protected DeleteResult(User user) {
        errorMessage = null;
        this.user = user;
    }

    /**
     * Constructs a {@link DeleteResult} instance for a failed deletion.
     *
     * @param errorMessage: the {@link String} error message to be displayed.
     */
    protected DeleteResult(String errorMessage) {
        this.errorMessage = errorMessage;
        user = null;
    }

    /**
     * @return whether the account deletion was successful.
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }

    /**
     * @return the {@link User} to be deleted.
     */
    public User getUser() {
        return user;
    }

    /**
     * @return the {@link String} error message of the deletion.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}

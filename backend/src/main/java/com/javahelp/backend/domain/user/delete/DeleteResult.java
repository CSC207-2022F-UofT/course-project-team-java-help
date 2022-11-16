package com.javahelp.backend.domain.user.delete;

import com.javahelp.model.user.User;

/**
 * A class used by a {@link DeleteManager} that encodes the result of an account deletion action.
 */
class DeleteResult {
    private final User user;
    private final String errorMessage;

    /**
     * Constructs a {@link DeleteResult} instance for a successful account deletion.
     *
     * @param user: the deleted {@link User}.
     */
    protected DeleteResult(User user) {
        this.user = user;
        errorMessage = null;
    }

    /**
     * Constructs a {@link DeleteResult} instance for a failed account deletion.
     *
     * @param errorMessage: the {@link String} error message to be displayed.
     */
    protected DeleteResult(String errorMessage) {
        user = null;
        this.errorMessage = errorMessage;
    }

    /**
     * @return the deleted {@link User}.
     */
    public User getUser() {
        return user;
    }

    /**
     * @return the {@link String} error message for the deletion.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return whether this account deletion was successful.
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }

}

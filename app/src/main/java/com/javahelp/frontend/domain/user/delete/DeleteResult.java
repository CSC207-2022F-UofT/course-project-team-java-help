package com.javahelp.frontend.domain.user.delete;

import com.javahelp.model.user.User;

/**
 * A result class that contains information of whether an account deletion is successful.
 */
public class DeleteResult {
    private final User user;
    private final String errorMessage;

    /**
     * Constructs a {@link DeleteResult} for a successful deletion.
     *
     * @param user: the deleted {@link User}.
     */
    public DeleteResult(User user) {
        this.user = user;
        this.errorMessage = null;
    }

    /**
     * Constructs a {@link DeleteResult} for a unsuccessful deletion.
     *
     * @param errorMessage: the {@link String} error message to be returned.
     */
    public DeleteResult(String errorMessage) {
        this.errorMessage = errorMessage;
        user = null;
    }

    /**
     * @return whether this account deletion is successful.
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }

    /**
     * @return the deleted {@link User}, or null if unsuccessful.
     */
    public User getUser() {
        return user;
    }

    /**
     * @return the {@link String} error message of this deletion, or null if successful.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}

package com.javahelp.backend.accountdeletion;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * A class used by a {@link DeleteManager} that encodes a successful account deletion result.
 */
class DeleteResult {
    private final User user;
    private final String successMessage;

    /**
     * Constructs a {@link DeleteResult} instance for a successful account deletion.
     *
     * @param user: the deleted {@link User}.
     */
    protected DeleteResult(User user) {
        this.user = user;
        successMessage = "Account deletion successful";
    }

    /**
     * @return the deleted {@link User}.
     */
    public User getUser() {
        return user;
    }

    /**
     * @return the {@link String} success message for the deletion.
     */
    public String getSuccessMessage() {
        return successMessage;
    }

}

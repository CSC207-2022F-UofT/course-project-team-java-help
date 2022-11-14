package com.javahelp.backend.accountdeletion;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * A class that encodes whether an account deletion was successful.
 */
class DeleteResult {
    private boolean success;
    private User user;

    /**
     * Constructs a DeleteResult instance.
     *
     * @param success: whether the deletion was successful.
     * @param user: the user to be deleted.
     */
    protected DeleteResult(boolean success, User user) {
        this.success = success;
        this.user = user;
    }

    /**
     * Returns the status of the account deletion.
     *
     * @return whether the account deletion was successful.
     */
    protected boolean isSuccess() {
        return success;
    }

    /**
     * Sets the status of the account deletion.
     *
     * @param success: whether the account deletion was successful.
     */
    protected void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns the user to be deleted.
     *
     * @return the user to be deleted.
     */
    protected User getUser() {
        return user;
    }

    /**
     * Sets the user to be deleted.
     *
     * @param user: the user to be deleted.
     */
    protected void setUser(User user) {
        this.user = user;
    }

}

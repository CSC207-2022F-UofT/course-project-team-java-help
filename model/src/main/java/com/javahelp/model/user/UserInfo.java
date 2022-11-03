package com.javahelp.model.user;

/**
 * Stores the information of a user.
 */
public abstract class UserInfo {
    private String emailAddress;

    /**
     * Constructs a UserInfo object.
     *
     * @param emailAddress: the email address of the user.
     */
    public UserInfo(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the email address of this user.
     *
     * @return the email address of this user.
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Sets the email address of this user.
     *
     * @param emailAddress: the new email address of this user.
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the type of the user.
     *
     * @return "CLIENT" or "PROVIDER" depending on the type of this user.
     */
    public abstract UserType getType();
}

package com.javahelp.model.user;

import java.util.HashMap;

/**
 * Stores the information of a user.
 */
public abstract class UserInfo {
    private String emailAddress;
    private HashMap<String, String> attributeMap = new HashMap<>();

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
     * Adds an attribute to this client.
     * Should be called when SurveyResponse is updated.
     *
     * @param question: one question from the Survey.
     * @param answer: one integer answer from the Survey. (only integers answers are allowed for now)
     */
    public void setAttribute(String question, String answer) {
        this.attributeMap.put(question, answer);
    }

    /**
     * Gets one attribute of this client.
     * @return the answer to a question (attribute) of this client (or "-1" if question does not exist).
     */
    public String getSingleAttribute(String question) {
        return this.attributeMap.getOrDefault(question, "-1");
    }

    /**
     * Gets the an attribute of this client.
     * @return the answer to a question (attribute) of this client (or -1 if question does not exist).
     */
    public HashMap<String, String> getAllAttribute() {
        return this.attributeMap;
    }

    /**
     * Gets the type of the user.
     *
     * @return "CLIENT" or "PROVIDER" depending on the type of this user.
     */
    public abstract UserType getType();
}

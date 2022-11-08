package com.javahelp.model.user;

import java.util.HashMap;

/**
 * Stores the information of a client.
 */
public class ClientUserInfo extends UserInfo {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private HashMap<String, Integer> attributeMap;

    /**
     * Constructs a ClientInfo object.
     *
     * @param emailAddress: the email address of this client.
     * @param address: the address of this client.
     * @param phoneNumber: the phone number of this client.
     * @param firstName: the first name of this client.
     * @param lastName: the last name of this client.
     */
    public ClientUserInfo(String emailAddress, String address, String phoneNumber, String firstName,
                          String lastName) {
        super(emailAddress);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.attributeMap = new HashMap<>();
    }

    /**
     * Gets the first name of this client.
     *
     * @return the first name of this client.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of this client.
     *
     * @param firstName: the new first name of this client.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of this client.
     *
     * @return the last name of this client.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of this client.
     *
     * @param lastName: the new last name of this client.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the address of this client.
     *
     * @return the address of this client.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of this client.
     *
     * @param address: the new address of this client.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets the phone number of this client.
     *
     * @return the phone number of this client.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phone number of this client.
     *
     * @param phoneNumber: the new phone number of this client.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Adds an attribute to this client.
     * Should be called when SurveyResponse is updated.
     *
     * @param question: one question from the Survey.
     * @param answer: one integer answer from the Survey. (only integers answers are allowed for now)
     */
    public void setAttribute(String question, Integer answer) {
        this.attributeMap.put(question, answer);
    }

    /**
     * Gets the an attribute of this client.
     * @return the answer to a question (attribute) of this client (or -1 if question does not exist).
     */
    public Integer getAttribute(String question) {
        return this.attributeMap.getOrDefault(question, -1);
    }

    @Override
    public UserType getType() {
        return UserType.CLIENT;
    }
}

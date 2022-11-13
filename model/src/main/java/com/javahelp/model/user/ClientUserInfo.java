package com.javahelp.model.user;

import java.io.IOException;
import java.util.Objects;

/**
 * Stores the information of a client.
 */
public class ClientUserInfo extends UserInfo {
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String gender;

    /**
     * Constructs a ClientInfo object.
     *
     * @param emailAddress: the email address of this client.
     * @param address: the address of this client.
     * @param phoneNumber: the phone number of this client.
     * @param firstName: the first name of this client.
     * @param lastName: the last name of this client.
     * @param gender: the gender of this client
     */
    public ClientUserInfo(String emailAddress, String address, String phoneNumber, String firstName,
                          String lastName, String gender) {
        super(emailAddress);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        if (! gender.toUpperCase().equals("FEMALE") && ! gender.toUpperCase().equals("MALE") &&
        ! gender.toUpperCase().equals("OTHERS")){
            throw new IllegalArgumentException("Invalid input! Should be either FEMALE, " +
                    "MALE or OTHERS");
        }
        this.gender = gender.toUpperCase();
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

    @Override
    public UserType getType() {
        return UserType.CLIENT;
    }

    /**
     * Get the gender of this client
     *
     * @return "MALE" or "FEMALE" depending on their gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Re-correct the gender of this client
     *
     * @param gender: the updated gender of this client
     */
    public void setGender(String gender){
        this.gender = gender;
    }
}

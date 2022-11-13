package com.javahelp.backend.register;

import com.javahelp.model.user.UserInfo;

/**
 * The User request model that a User uses to register their new account
 */
public abstract class UserRequestModel{

    /**
     * An inserted username
     */
    private String username;

    /**
     * An inserted new account password
     */
    private byte[] password;

    /**
     * A re-entered new password (when registering only)
     */
    private byte[] password2;

    /**
     * The user's phone number
     */
    private String phoneNumber;

    /**
     * The email address of this user
     */
    private String emailAddress;

    /**
     * The living address of this user
     */
    private String address;

    /**
     * The stringID of this user (randomly generated but only once)
     */
    private String stringID;

    /**
     * To construct the requested model of the new account for this user
     * @param username the name for this user account
     * @param password the current *hashed* password of this user account
     * @param password2 the re-entered *hashed* password (only for registration)
     * @param phoneNumber the contact info of this user
     * @param emailAddress the email address of this user
     * @param address the living address of this suer
     * @param stringID the encoded stringID of this user (randomly generated but only once)
     */
    public UserRequestModel(String username, byte[] password, byte[] password2,
                          String phoneNumber, String emailAddress, String address, String stringID){
        this.username = username;
        this.password = password;
        this.password2 = password2;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.address = address;
        this.stringID = stringID;
    }

    /**
     * @return current username of this user
     */
    public String getUsername(){
        return username;
    }

    /**
     * @return the given password of this user
     */
    public byte[] getPassword(){
        return password;
    }

    /**
     * @return the re-entered password of this user
     */
    public byte[] getPassword2(){
        return password2;
    }

    /**
     * @return this user's phone number (their contact info)
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return this user's email address
     */
    public String getEmailAddress(){
        return emailAddress;
    }

    /**
     * @return this user's living address
     */
    public String getAddress(){
        return address;
    }

    /**
     * @return this user's encoded stringID
     */
    public String getStringID(){
        return stringID;
    }

    /**
     * @return the type of this request model
     */
    public abstract UserType getType();
}

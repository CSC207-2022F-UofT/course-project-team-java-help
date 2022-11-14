package com.javahelp.backend.register.client;

import com.javahelp.backend.register.UserRequestModel;
import com.javahelp.backend.register.UserType;

/**
 * The input data passed by a Client to create a new RequestModel
 */
public class ClientRequestModel extends UserRequestModel {

    private String firstName;
    private String lastName;
    private String gender;

    /**
     * To construct a new Request Model for a client
     * @param username the name of this client
     * @param password the password of this client
     * @param password2 the re-entered password of this client
     * @param phoneNumber the phone number (contact info) of this client
     * @param emailAddress the email address of this client
     * @param address the living address of this client
     * @param firstName the first name of this client
     * @param lastName the second name of this client
     * @param stringID a generated stringID for this client
     * @param gender the gender of this client
     */
    public ClientRequestModel(String username, byte[] password, byte[] password2,
                              String phoneNumber, String emailAddress, String address,
                              String firstName, String lastName, String stringID, String gender){
        super(username, password, password2, phoneNumber, emailAddress, address, stringID);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * @return the first name of this client
     */
    public String getFirstName(){
        return firstName;
    }

    /**
     * @return the last name of this client
     */
    public String getLastName(){
        return lastName;
    }

    /**
     * @return the status of being a therapist or not
     */
    @Override
    public UserType getType(){
        return UserType.CLIENT;
    }

    /**
     * @return the current gender of this client
     */
    public String getGender(){
        return this.gender;
    }
}

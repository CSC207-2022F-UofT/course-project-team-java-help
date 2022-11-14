package com.javahelp.backend.register.provider;

import com.javahelp.backend.register.UserRequestModel;
import com.javahelp.backend.register.UserType;

/**
 * The input data passed by a Provider to create a new RequestModel
 */
public class ProviderRequestModel extends UserRequestModel {

    private String practiceName;
    private boolean certified;

    public ProviderRequestModel(String username, byte[] password, byte[] password2,
                                String phoneNumber, String emailAddress, String address,
                                String practiceName, boolean certified, String stringID){
        super(username, password, password2, phoneNumber, emailAddress, address, stringID);
        this.practiceName = practiceName;
        this.certified = certified;
    }

    /**
     * @return the practice name of this provider
     */
    public String getPracticeName(){
        return practiceName;
    }

    /**
     * @return the status of verification of this provider
     */
    public boolean getCertified(){
        return this.certified;
    }

    /**
     * @return the status of being a therapist or not
     */
    @Override
    public UserType getType(){
        return UserType.PROVIDER;
    }
}

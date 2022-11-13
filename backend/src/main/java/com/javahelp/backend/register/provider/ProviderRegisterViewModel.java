package com.javahelp.backend.register.provider;

import com.javahelp.backend.register.UserInputBoundary;
import com.javahelp.backend.register.UserRegisterViewModel;

import java.security.SecureRandom;

/**
 * Receive the info of a user and respond to the submission click
 */
public class ProviderRegisterViewModel extends UserRegisterViewModel {
    /**
     * Static random generator to generate the stringID of this provider
     */
    private static SecureRandom rand;

    /**
     * An attribute representing for the gateway
     */
    private UserInputBoundary providerMethod;


    /**
     * Bundle every info from the UI into an object in form of ProviderRequestModel
     * @param username the name for the new account that Provider wants to make
     * @param password the password for the new account
     * @param password2 the re-entered password
     * @param phoneNumber the contact info of this provider
     * @param emailAddress the email address of this provider
     * @param address the living address of this provider
     * @param practiceName the name of this provider
     * @param certified the status of certification in the provider's specialization
     *
     * @return an instance of ProviderRequestModel that contains every input info in it
     */
    private static ProviderRequestModel bundleInfo(String username, String password, String password2,
                                           String phoneNumber, String emailAddress, String address,
                                           String practiceName, boolean certified){
        String encoded = ProviderRegisterViewModel.generateStringID(username);
        byte[] salt = new byte[10];
        rand.nextBytes(salt);
        return new ProviderRequestModel(username,
                ProviderRegisterViewModel.hash(salt, password),
                ProviderRegisterViewModel.hash(salt, password2),
                phoneNumber, emailAddress, address, practiceName, certified, encoded);
    }

    /**
     * Create a model of the current Provider Request Model (be called by the UI)
     * -------It should return a message of success/failure later---------
     */
    public void create(String username, String password, String password2,
                       String phoneNumber, String emailAddress, String address,
                       String practiceName, boolean certified){
        ProviderRequestModel model = ProviderRegisterViewModel.bundleInfo(username,
                password, password2, phoneNumber,
                emailAddress, address, practiceName, certified);
        providerMethod.create(model);
    }

    /**
     * Respond to the click from the Frontend Register UI
     */
    public void respondClick(boolean click, String username, String password, String password2,
                             String phoneNumber, String emailAddress, String address,
                             String practiceName, boolean certified){
        if (click){
            ProviderRegisterViewModel.bundleInfo(username, password, password2,
                    phoneNumber, emailAddress, address, practiceName, certified);
        }
    }
}

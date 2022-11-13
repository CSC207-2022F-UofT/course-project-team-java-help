package com.javahelp.backend.register.client;

import com.javahelp.backend.register.UserInputBoundary;
import com.javahelp.backend.register.UserRegisterViewModel;

import java.security.SecureRandom;

/**
 * Receive the info of a user and respond to the submission click
 */
public class ClientRegisterViewModel extends UserRegisterViewModel {
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
     * @param username the name of this client
     * @param password the password of this client
     * @param password2 the re-entered password of this client
     * @param phoneNumber the phone number (contact info) of this client
     * @param emailAddress the email address of this client
     * @param address the living address of this client
     * @param firstName the first name of this client
     * @param lastName the second name of this client
     * @param gender the gender of this client
     *
     * @return an instance of ProviderRequestModel that contains every input info in it
     */
    private static ClientRequestModel bundleInfo(String username, String password, String password2,
                                                   String phoneNumber, String emailAddress, String address,
                                                   String firstName, String lastName, String gender){
        String encoded = ClientRegisterViewModel.generateStringID(username);
        byte[] salt = new byte[10];
        rand.nextBytes(salt);
        return new ClientRequestModel(username, ClientRegisterViewModel.hash(salt, password),
                ClientRegisterViewModel.hash(salt, password2), phoneNumber,
                emailAddress, address, firstName, lastName, encoded, gender);
    }

    /**
     * Create a model of the current Provider Request Model (be called by the UI)
     * -------A message of success/failure later---------
     */
    public void create(String username, String password, String password2,
                       String phoneNumber, String emailAddress, String address,
                       String firstName, String lastName, String gender){
        ClientRequestModel model = ClientRegisterViewModel.bundleInfo(username,
                password, password2, phoneNumber,
                emailAddress, address, firstName, lastName, gender);
        providerMethod.create(model);
        // TODO: Implement an early failure message (later)
    }

    /**
     * Respond to the click from the Frontend Register UI
     */
    public void respondClick(boolean click, String username, String password, String password2,
                             String phoneNumber, String emailAddress, String address,
                             String firstName, String lastName, String gender){
        if (click){
            ClientRegisterViewModel.bundleInfo(username, password, password2,
                    phoneNumber, emailAddress, address, firstName, lastName, gender);
        }
    }
}

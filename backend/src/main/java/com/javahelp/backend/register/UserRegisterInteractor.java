package com.javahelp.backend.register;

import com.javahelp.backend.register.client.ClientRequestModel;
import com.javahelp.backend.register.provider.ProviderRequestModel;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;

import java.util.Arrays;

/**
 * To manage the data flow and produce the output result (Successful register or not)
 */
public class UserRegisterInteractor implements UserInputBoundary {

    /**
     * An object representing for the RegisterAccessInterface
     */
    private RegisterAccessInterface access;

    /**
     * An instance representing a UserPresenterInterface object
     */
    private static UserPresenterInterface presenterInt;

    /**
     * Check if the re-entered password is equal to the given password or not
     * @return the result of matching (true = their value match, false otherwise)
     */
    private static boolean checkPassword(UserRequestModel model){
        return Arrays.equals(model.getPassword(), model.getPassword2());
    }

    /**
     * Create a new ProviderInfo object based on the given model and use it for verifying
     * its uniqueness in the Database later (before creating a new account)
     *
     * @return a Provider Info object
     */
    @Override
    public User create(ProviderRequestModel model){
        if (UserRegisterInteractor.checkPassword(model) &&
                access.checkValidity(model.getUsername(), model.getPhoneNumber())) {
            ProviderUserInfo userInfo = new ProviderUserInfo(model.getEmailAddress(),
                    model.getAddress(), model.getPhoneNumber(), model.getPracticeName());
            UserRegisterInteractor.getResult(true, userInfo, null);
            return new User(model.getStringID(), userInfo, model.getUsername());
        }
        if (!UserRegisterInteractor.checkPassword(model)){
            if (access.checkValidity(model.getUsername(), model.getPhoneNumber())) {
                UserRegisterInteractor.getResult(false, null, "the given " +
                        "passwords did not match");
            } else {
                UserRegisterInteractor.getResult(false, null, "the given " +
                        "passwords did not match and the given info has already been occupied");
            }
        }
        else{UserRegisterInteractor.getResult(false, null, "the given info has "
                + "already been occupied");}
        return null;
    }

    /**
     * Create a new ClientInfo object based on the given model and use it for verifying
     * its uniqueness in the Database later (before creating a new account)
     *
     * @return a Client Info object
     */
    @Override
    public User create(ClientRequestModel model){
        // Success
        if (UserRegisterInteractor.checkPassword(model) &&
                access.checkValidity(model.getUsername(), model.getPhoneNumber())) {
            ClientUserInfo userInfo =  new ClientUserInfo(model.getEmailAddress(),
                    model.getAddress(), model.getPhoneNumber(), model.getFirstName(),
                    model.getLastName(), model.getGender());
            UserRegisterInteractor.getResult(true, userInfo, null);
            return new User(model.getStringID(), userInfo, model.getUsername());
        }

        // Failed cases
        if (!UserRegisterInteractor.checkPassword(model)){
            if (access.checkValidity(model.getUsername(), model.getPhoneNumber())) {
                UserRegisterInteractor.getResult(false, null, "the given " +
                        "passwords did not match");
            } else {
                UserRegisterInteractor.getResult(false, null, "the given " +
                        "passwords did not match and the given info has already been occupied");
            }
        }
        else{UserRegisterInteractor.getResult(false, null, "the given info has "
                + "already been occupied");}
        return null;
    }

    public static void getResult(boolean success, UserInfo userInfo, String error){
        if (success){
            presenterInt.successView(userInfo);
        }
        presenterInt.failureView(error);
    }

    /**
     * Check if the given input is valid to produce or not
     * @return the validity for making a new account for this user
     */
    boolean checkResult(){
        // return CheckRepo.validity;
        return false;
    }
}


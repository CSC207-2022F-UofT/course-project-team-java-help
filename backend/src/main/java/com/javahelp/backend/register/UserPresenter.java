package com.javahelp.backend.register;

import com.javahelp.model.user.UserInfo;

/**
 * A presenter class to report to the View about the result of this registration process
 */
public class UserPresenter implements UserPresenterInterface {

    /**
     * Report a successful registration result
     * @param userInfo the user info of the newly created user entity
     * @return a string message showing the successful result
     */
    @Override
    public String successView(UserInfo userInfo) {
        return "The registration of user " + userInfo.getEmailAddress() + " has an account type " +
                "of " + userInfo.getType() + "has been succeeded!";
    }

    /**
     * Report a failed registration result
     * @param error a description of error
     * @return a string message showing the error
     */
    @Override
    public String failureView(String error) {
        return "The registration is failed due to " + error;
    }

}

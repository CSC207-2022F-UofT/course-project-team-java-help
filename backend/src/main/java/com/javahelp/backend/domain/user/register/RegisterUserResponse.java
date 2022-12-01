package com.javahelp.backend.domain.user.register;

import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

/**
 * Response for {@link User} registration from a {@link UserRegisterInteractor}
 */
public class RegisterUserResponse {

    private User user = null;
    private UserPassword password = null;
    private String error = null;

    /**
     * For bundle up info of the result of this successful registration
     * @param user the newly created {@link User}
     * @param password the newly created {@link UserPassword}
     */
    public RegisterUserResponse(User user, UserPassword password) {
        this.user = user;
        this.password = password;
    }

    /**
     * An overloaded constructor to make a report for this failed registration
     * @param error a description of failure at registration
     */
    public RegisterUserResponse(String error) {
        this.error = error;
    }

    /**
     * @return a description of failure at registering from {@link UserRegisterInteractor}.
     * Null if succeeded
     */
    public String getErrorMessage() {
        return error;
    }

    /**
     * @return an instance of a newly created {@link User}
     * Null if failed
     */
    public User getUser() {
        return user;
    }

    /**
     * @return an instance of a newly made {@link UserPassword}
     * Null if failed
     */
    public UserPassword getPassword() {
        return password;
    }

    /**
     * @return whether the user succeeded at registering or not
     */
    public boolean isSuccess() {
        return error == null;
    }
}

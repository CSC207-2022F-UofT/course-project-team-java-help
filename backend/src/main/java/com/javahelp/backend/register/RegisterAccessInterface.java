package com.javahelp.backend.register;

import com.javahelp.backend.register.UserRequestModel;

/**
 * An interface for accessing database on the cloud
 */
public interface RegisterAccessInterface {
    /**
     * To check for any repetition of the given data inside the repo
     * @param username the username of this user (name of the potentially made account)
     * @param phoneNumber the contact info of this user
     *
     * @return the validity of this given info (true = completely new, false otherwise)
     */
    boolean checkValidity(String username, String phoneNumber);
}

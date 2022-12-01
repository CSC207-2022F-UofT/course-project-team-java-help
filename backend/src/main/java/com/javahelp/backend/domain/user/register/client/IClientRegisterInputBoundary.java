package com.javahelp.backend.domain.user.register.client;

import com.javahelp.backend.domain.user.register.IUserRegisterInputBoundary;

/**
 * Input necessary to register a client
 */
public interface IClientRegisterInputBoundary extends IUserRegisterInputBoundary {
    /**
     * @return address of the client
     */
    String getAddress();

    /**
     * @return phone number of the client
     */
    String getPhoneNumber();

    /**
     * @return first name of the client
     */
    String getFirstName();

    /**
     * @return last name of the client
     */
    String getLastName();
}

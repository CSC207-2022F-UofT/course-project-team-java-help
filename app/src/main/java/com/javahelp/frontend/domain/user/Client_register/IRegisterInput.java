package com.javahelp.frontend.domain.user.Client_register;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;

/**
 * Input boundary for a {@link User} register
 */
public interface IRegisterInput {
    /**
     * register with username password and clientUserInfo.
     * should output results through a {@link IRegisterOutput}
     * @param username    username of {@link User} to register or null
     * @param password    password of {@link User} to register
     * @param clientUserInfo         clientUserInfo of {@link User} to of register or null
     */

    void register(String username, String password, ClientUserInfo clientUserInfo);


}

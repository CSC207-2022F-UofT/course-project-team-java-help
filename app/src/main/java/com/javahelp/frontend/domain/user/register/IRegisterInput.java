package com.javahelp.frontend.domain.user.register;

import com.javahelp.model.user.User;

/**
 * Input boundary for a {@link User} register
 */
public interface IRegisterInput {
    /**
     * register with username password and clientUserInfo.
     * should output results through a {@link IRegisterOutput}
     *
     * @param user     {@link User} to register
     * @param password password of {@link User} to register
     */

    void register(User user, String password);


}

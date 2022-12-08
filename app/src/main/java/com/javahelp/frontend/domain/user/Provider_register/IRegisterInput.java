package com.javahelp.frontend.domain.user.Provider_register;

import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;

/**
 * Input boundary for a {@link User} register
 */
public interface IRegisterInput {

    /**
     * register with the username, password, and providerUserInfo.
     * Should output results through a {@link IRegisterOutput}.
     *
     * @param username     username of {@link User} to register or null
     * @param password     password of {@link User} to register
     * @param providerUserInfo        providerUserInfo of {@link User} to of register or null
     */
    void register(String username, String password, ProviderUserInfo providerUserInfo);

}
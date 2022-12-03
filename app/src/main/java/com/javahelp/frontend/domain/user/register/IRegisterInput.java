package com.javahelp.frontend.domain.user.register;

import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;

/**
 * Input boundary for a {@link User} register
 * <p>
 * One of getUsername, getEmail, or getID must return a non null {@link String}
 */
public interface IRegisterInput {

    void register(String username, String password, ProviderUserInfo providerUserInfo);

}

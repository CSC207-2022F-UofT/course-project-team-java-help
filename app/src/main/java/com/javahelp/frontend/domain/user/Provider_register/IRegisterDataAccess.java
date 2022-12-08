package com.javahelp.frontend.domain.user.Provider_register;


import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;

public interface IRegisterDataAccess {


    /**
     * Register with the provided credentials
     *
     * @param username     username to register in for, or null
     * @param password     the {@link UserPassword} to register in with
     * @param callback     the {@link FutureCallback} to call, or null
     * @param providerUserInfo    the {@link ProviderUserInfo} to register, or null
     * @return {@link Future} with {@link RegisterResult}
     */
    Future<RegisterResult> register(String username, ProviderUserInfo providerUserInfo, UserPassword password, FutureCallback<RegisterResult> callback);
}
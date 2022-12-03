package com.javahelp.frontend.domain.user.register;

import com.javahelp.frontend.domain.user.login.LoginResult;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;

public interface IRegisterDataAccess {


    /**
     * Login with the provided credentials
     *
     * @param username     username to log in for, or null
     * @param password     the {@link UserPassword} to log in with
     * @param callback     the {@link FutureCallback} to call, or null
     * @return {@link Future} with {@link LoginResult}
     */
    Future<RegisterResult> register(String username, ProviderUserInfo providerUserInfo, UserPassword password, FutureCallback<RegisterResult> callback);
}

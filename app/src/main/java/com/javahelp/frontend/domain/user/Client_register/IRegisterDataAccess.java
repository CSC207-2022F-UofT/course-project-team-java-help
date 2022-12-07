package com.javahelp.frontend.domain.user.Client_register;

import com.javahelp.model.user.ClientUserInfo;

import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;


public interface IRegisterDataAccess {

    /**
     * Register with the provided credentials
     *
     * @param username      username to log in for, or null
     * @param password      the {@link UserPassword} to register in with
     * @param callback      the {@link FutureCallback} to call, or null
     * @param clientUserInfo       the {@link ClientUserInfo} tp register, or null
     * @return {@link Future} with {@link RegisterResult}
     */
    Future<RegisterResult> register(String username, ClientUserInfo clientUserInfo, UserPassword password, FutureCallback<RegisterResult> callback);
}

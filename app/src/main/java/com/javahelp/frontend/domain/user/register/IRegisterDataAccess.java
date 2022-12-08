package com.javahelp.frontend.domain.user.register;

import com.javahelp.model.user.ClientUserInfo;

import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;


public interface IRegisterDataAccess {

    /**
     * Register with the provided credentials
     *
     * @param user          the {@link User} to register
     * @param password      the {@link UserPassword} to register in with
     * @param callback      the {@link FutureCallback} to call, or null
     * @return {@link Future} with {@link RegisterResult}
     */
    Future<RegisterResult> register(User user, UserPassword password, FutureCallback<RegisterResult> callback);
}

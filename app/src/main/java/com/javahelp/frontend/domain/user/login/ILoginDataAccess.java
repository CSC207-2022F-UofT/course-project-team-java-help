package com.javahelp.frontend.domain.user.login;

import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;

/**
 * Data access interface for {@link LoginInteractor}
 */
public interface ILoginDataAccess {

    /**
     * Login with the provided credentials
     *
     * @param username     username to log in for, or null
     * @param email        email to log in for, or null
     * @param id           {@link String} id to log in for, or null
     * @param password     the {@link UserPassword} to log in with
     * @param stayLoggedIn whether to stay logged in
     * @param callback     the {@link FutureCallback} to call, or null
     * @return {@link Future} with {@link LoginResult}
     */
    Future<LoginResult> login(String username, String email, String id, UserPassword password, boolean stayLoggedIn, FutureCallback<LoginResult> callback);

}

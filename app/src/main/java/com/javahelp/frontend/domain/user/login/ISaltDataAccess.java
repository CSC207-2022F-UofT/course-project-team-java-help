package com.javahelp.frontend.domain.user.login;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;

/**
 * Accesses the salt for a {@link LoginInteractor}
 */
public interface ISaltDataAccess {

    /**
     * Gets the salt for a user
     * @param username username of the user to get salt for, or null
     * @param email email of the user to get salt for, or null
     * @param id {@link String} id of the user to get salt for, or null
     * @param callback {@link FutureCallback} to call, or null
     * @return {@link Future} byte array with salt
     */
    Future<byte[]> getSalt(String username, String email, String id, FutureCallback<byte[]> callback);

}

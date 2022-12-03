package com.javahelp.frontend.domain.user.register;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;

public interface ISaltDataAccess {

    /**
     * Gets the salt for a user
     * @param username username of the user to get salt for, or null
     * @param callback {@link FutureCallback} to call, or null
     * @return {@link Future} byte array with salt
     */
    Future<byte[]> getSalt(String username, FutureCallback<byte[]> callback);
}

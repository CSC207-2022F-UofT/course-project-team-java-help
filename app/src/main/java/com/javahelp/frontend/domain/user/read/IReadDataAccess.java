package com.javahelp.frontend.domain.user.read;

import com.javahelp.model.user.User;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;

/**
 * Interface that supplies data to {@link ReadInteractor}
 */
public interface IReadDataAccess {

    /**
     * Gets the {@link User} with the specified id
     * @param id id of the {@link User} to get
     * @param callback {@link FutureCallback} to run on {@link User} retrieved
     * @return {@link Future} with the retrieved {@link User}
     */
    Future<User> read(String id, FutureCallback<User> callback);

}

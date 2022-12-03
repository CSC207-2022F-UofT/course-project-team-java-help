package com.javahelp.frontend.domain.user.delete;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;

/**
 * A data access interface for a {@link DeleteInteractor}.
 */
public interface IDeleteDataAccess {

    /**
     * Deletes the user with the given {@link String} userID.
     *
     * @param userID: the {@link String} userID of the user to be deleted.
     * @param callback: the {@link FutureCallback} to call, or null.
     * @return {@link Future} with {@link DeleteResult}.
     */
    Future<DeleteResult> delete(String userID, FutureCallback<DeleteResult> callback);
}

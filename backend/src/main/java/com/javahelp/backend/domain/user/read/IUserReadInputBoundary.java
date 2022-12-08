package com.javahelp.backend.domain.user.read;

import com.javahelp.model.user.User;

/**
 * Interface with input for a {@link UserReadInteractor}
 */
public interface IUserReadInputBoundary {

    /**
     *
     * @return {@link String} ID of the {@link User} to get
     */
    String getID();

}

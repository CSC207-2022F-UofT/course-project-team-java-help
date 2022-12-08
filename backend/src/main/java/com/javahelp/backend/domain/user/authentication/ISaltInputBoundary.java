package com.javahelp.backend.domain.user.authentication;

import com.javahelp.model.user.User;

/**
 * Input boundary for {@link SaltInteractor}
 */
public interface ISaltInputBoundary {

    /**
     * @return {@link String} ID of the {@link User} to get salt for
     */
    String getUserID();

    /**
     * @return {@link String} username for {@link User} to get salt for
     */
    String getUsername();

    /**
     * @return {@link String} email of {@link User} to get salt for
     */
    String getEmail();

}

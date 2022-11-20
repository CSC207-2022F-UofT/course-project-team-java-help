package com.javahelp.backend.domain.user.authentication;

import com.javahelp.model.user.User;

/**
 * Input boundary for {@link SaltInteractor}
 */
public interface ISaltInput {

    /**
     * @return {@link String} ID of the {@link User} to get salt for
     */
    String getUserID();

}

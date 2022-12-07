package com.javahelp.backend.domain.user.authentication;

import com.javahelp.model.user.User;

/**
 * Input boundary for {@link TokenAuthManager}
 */
public interface ITokenAuthInputBoundary {

    /**
     * @return ID of the {@link User} to authenticate for
     */
    String getUserID();

    /**
     * @return {@link String} token to authenticate with
     */
    String getToken();

}

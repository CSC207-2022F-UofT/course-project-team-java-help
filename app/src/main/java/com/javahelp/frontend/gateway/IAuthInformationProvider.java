package com.javahelp.frontend.gateway;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * Interface for classes that provider authentication information ({@link Token} for use by a {@link User})
 */
public interface IAuthInformationProvider {

    /**
     * @return the authenticated {@link User}'s ID
     */
    String getUserID();

    /**
     * @return the {@link Token} {@link String} used to authenticate
     */
    String getTokenString();

}

package com.javahelp.backend.domain.user.register;

import com.javahelp.model.user.User;

/**
 * Information necessary to register a {@link User}
 */
public interface IUserRegisterInputBoundary {

    /**
     * @return the {@link User}'s email address
     */
    String getEmailAddress();

    /**
     * @return the {@link User}'s username
     */
    String getUsername();

    /**
     * @return base64 encoded byte array with the first 4 bytes containing a 32 bit integer specifying
     * salt length, the next bytes up to the previously specified length of the salt specifying the salt,
     * and the last bytes specifying the hash
     */
    String getSaltAndHash();
}

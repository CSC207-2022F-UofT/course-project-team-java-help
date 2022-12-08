package com.javahelp.frontend.domain.user.register;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

/**
 * Interface for classes that wish to receive information from {@link RegisterInteractor}.
 * Acts as an output boundary for the {@link RegisterInteractor} user case.
 */
public interface IRegisterOutput {

    /**
     * Called when the provided credentials authenticate
     *
     * @param user  {@link User} authenticated for
     * @param token {@link Token} authenticated with
     */
    void success(User user, Token token);

    /**
     * Called when provided credentials did not authenticate
     */
    void failure();

    /**
     * Called when something goes wrong before authentication
     *
     * @param errorMessage {@link String} error message received
     */
    void error(String errorMessage);

    /**
     * Abort the Register attempt
     */
    void abort();


}

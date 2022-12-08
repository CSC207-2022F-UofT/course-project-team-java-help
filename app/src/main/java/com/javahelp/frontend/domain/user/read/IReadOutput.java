package com.javahelp.frontend.domain.user.read;

import com.javahelp.model.user.User;

/**
 * Interface to be implemented by classes wishing to receive output from
 * {@link IReadInput} implementing classes
 */
public interface IReadOutput {

    /**
     * Called with the retrieved {@link User}
     *
     * @param user {@link User} retrieved
     */
    void success(User user);

    /**
     * Called when something goes wrong
     *
     * @param errorMessage {@link String} error message received
     */
    void error(String errorMessage);

    /**
     * Abort the read {@link User} attempt
     */
    void abort();

}

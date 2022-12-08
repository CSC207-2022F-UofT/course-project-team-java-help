package com.javahelp.frontend.domain.user.read;

import com.javahelp.model.user.User;

/**
 * Input boundary for an interactor that reads {@link User} info
 */
public interface IReadInput {

    /**
     * Gets the information of the {@link User} with the specified id
     * @param id id of the {@link User} to get
     */
    void read(String id);

}

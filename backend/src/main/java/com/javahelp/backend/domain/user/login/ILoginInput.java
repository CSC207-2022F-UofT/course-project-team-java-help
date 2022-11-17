package com.javahelp.backend.domain.user.login;

import com.javahelp.model.user.UserPassword;

/**
 * Interface for classes that want to supply input to a {@link LoginInteractor}.
 * <p>
 * Must supply one of username, id, or email, the other two can be null
 */
public interface ILoginInput {

    /**
     * @return the desired {@link String} username for login,
     * or null if no desired username is known/specified
     */
    String getUsername();

    /**
     * @return the desired {@link String} id for login,
     * or null if no desired id is known/specified
     */
    String getID();

    /**
     * @return the desired {@link String} email for login
     * or null if no desired email is known/specified
     */
    String getEmail();

    /**
     * @return the {@link UserPassword} being used to login
     */
    UserPassword getPassword();

    /**
     * @return whether to stay logged in
     */
    boolean stayLoggedIn();

}

package com.javahelp.frontend.domain.user.login;

import com.javahelp.model.user.User;

/**
 * Input boundary for a {@link User} logging in
 * <p>
 * One of getUsername, getEmail, or getID must return a non null {@link String}
 */
public interface ILoginInput {

    /**
     * Logs in with the specified id, username, or email, and the specified password.
     * Should output results through a {@link ILoginOutput}.
     *
     * @param id           id of {@link User} to log into or null
     * @param username     username of {@link User} to log into or null
     * @param email        email of {@link User} to of into or null
     * @param password     password of {@link User} to log into
     * @param stayLoggedIn whether to stay logged in
     */
    void login(String id, String username, String email, String password, boolean stayLoggedIn);

}

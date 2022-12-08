package com.javahelp.backend.domain.user.login;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import java.time.Duration;

/**
 * Use case interactor for {@link User} login.
 * <p></p>
 * Creates a new {@link User} and creates a {@link Token} for that {@link User}
 */
public class LoginInteractor {

    private final IUserStore userStore;

    private final ITokenStore tokenStore;

    /**
     * Creates a new {@link LoginInteractor}
     *
     * @param userStore  {@link IUserStore} to use
     * @param tokenStore {@link ITokenStore} to use
     */
    public LoginInteractor(IUserStore userStore, ITokenStore tokenStore) {
        this.userStore = userStore;
        this.tokenStore = tokenStore;
    }

    /**
     * Logs in with the specified {@link ILoginInputBoundary}
     *
     * @param input {@link ILoginInputBoundary} providing login information
     */
    public LoginResult login(ILoginInputBoundary input) {
        User u = locateUser(input.getUsername(), input.getEmail(), input.getID());

        if (u == null) {
            return new LoginResult("Failed to locate user");
        }

        if (!authenticateUser(u, input.getPassword())) {
            return new LoginResult("Failed to authenticate user");
        }

        Duration d = input.stayLoggedIn() ? Duration.ofDays(120) : Duration.ofHours(8);

        String tag = input.stayLoggedIn() ? "User Access" : "User Session";

        Token t = new Token(d, tag, u.getStringID());

        tokenStore.create(t);

        return new LoginResult(u, t);
    }

    /**
     * Locates a user
     *
     * @param username {@link String} username of the user to locate, or null if a different
     *                 piece of identifying info is provided
     * @param email    {@link String} email of the user to locate, or null if a different
     *                 piece of identifying info is provided
     * @param id       {@link String} id of the user to locate, or null if a different
     *                 piece of identifying info is provided
     * @return the found {@link User} or null if none located
     */
    private User locateUser(String username, String email, String id) {
        if (username == null && email == null && id == null) {
            throw new IllegalArgumentException("One of username, email, or id must be provided");
        }

        User u = null;

        if (id != null) {
            u = userStore.read(id);
        } else if (username != null) {
            u = userStore.readByUsername(username);
        } else {
            u = userStore.readByEmail(email);
        }

        return u;
    }

    /**
     * Authenticate the provided {@link User}
     * @param u {@link User} to authenticate
     * @param p {@link UserPassword} to use
     * @return whether the {@link User} authenticated
     */
    private boolean authenticateUser(User u, UserPassword p) {
        UserPassword userPassword = userStore.readPassword(u.getStringID());
        return userPassword.getBase64SaltHash().equals(p.getBase64SaltHash());
    }

}

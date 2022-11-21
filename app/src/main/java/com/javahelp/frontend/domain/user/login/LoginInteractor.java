package com.javahelp.frontend.domain.user.login;

import com.javahelp.model.user.IPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

/**
 * Use case interactor for logging in a {@link User}
 */
public class LoginInteractor implements ILoginInput {

    ILoginOutput output;
    ISaltDataAccess saltAccess;
    ILoginDataAccess loginAccess;
    IPasswordHasher hasher;

    /**
     * Creates a new {@link LoginInteractor}
     *
     * @param output      {@link ILoginOutput} to output to
     * @param saltAccess  {@link ISaltDataAccess} to get salt from
     * @param loginAccess {@link ILoginDataAccess} to login using
     * @param hasher      {@link IPasswordHasher} to hash password
     */
    public LoginInteractor(ILoginOutput output, ISaltDataAccess saltAccess, ILoginDataAccess loginAccess,
                           IPasswordHasher hasher) {
        this.output = output;
        this.saltAccess = saltAccess;
        this.loginAccess = loginAccess;
        this.hasher = hasher;
    }

    @Override
    public void login(String id, String username, String email, String password, boolean stayLoggedIn) {

        if (username == null && email == null && id == null) {
            output.error("Must define one of username, email, and id");
        }

        saltAccess.getSalt(username, email, id, new FutureCallback<byte[]>() {
            @Override
            public void completed(byte[] result) {
                UserPassword p = new UserPassword(password, result, hasher);
                loginWithUserPassword(id, email, username, p, stayLoggedIn);
            }

            @Override
            public void failed(Exception ex) {
                output.error(ex.getMessage());
            }

            @Override
            public void cancelled() {
                output.abort();
            }
        });
    }

    /**
     * Logs in with the specified {@link UserPassword}
     *
     * @param id           {@link String} id to log into, or null
     * @param email        email to log into, or null
     * @param username     username to log into, or null
     * @param password     {@link UserPassword} to log into
     * @param stayLoggedIn whether to stay logged in
     */
    private void loginWithUserPassword(String id, String email, String username, UserPassword password, boolean stayLoggedIn) {
        loginAccess.login(username, email, id, password, stayLoggedIn, new FutureCallback<LoginResult>() {
            @Override
            public void completed(LoginResult result) {
                if (result.isSuccess()) {
                    output.success(result.getUser(), result.getToken());
                } else {
                    output.failure();
                }
            }

            @Override
            public void failed(Exception ex) {
                output.error(ex.getMessage());
            }

            @Override
            public void cancelled() {
                output.abort();
            }
        });
    }

}

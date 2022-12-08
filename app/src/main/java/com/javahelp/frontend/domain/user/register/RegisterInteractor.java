package com.javahelp.frontend.domain.user.register;

import com.javahelp.model.user.IPasswordHasher;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

/**
 * User case interactor for register a {@link User}
 */
public class RegisterInteractor implements IRegisterInput {
    IRegisterOutput output;
    IRegisterDataAccess registerAccess;
    IPasswordHasher hasher;

    /**
     * creates a new {@link RegisterInteractor}
     *
     * @param output         {@link IRegisterOutput} to output to
     * @param registerAccess {@link IRegisterDataAccess} to register using
     * @param hasher         {@link IPasswordHasher} to hash password
     */
    public RegisterInteractor(IRegisterOutput output, IRegisterDataAccess registerAccess, IPasswordHasher hasher) {
        this.hasher = hasher;
        this.output = output;
        this.registerAccess = registerAccess;
    }

    /**
     * @param user     {@link User} to register
     * @param password password to register with
     */
    @Override
    public void register(User user, String password) {
        UserPassword p = new UserPassword(password, hasher);

        registerAccess.register(user, p, new FutureCallback<RegisterResult>() {
            @Override
            public void completed(RegisterResult result) {
                if (result.isSuccess()) {
                    output.success(result.getUser(), result.getToken());
                } else {
                    output.error(result.getErrorMessage());
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

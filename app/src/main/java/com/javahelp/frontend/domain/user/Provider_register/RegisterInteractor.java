package com.javahelp.frontend.domain.user.Provider_register;


import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.IPasswordHasher;

import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

/**
 * Use case interactor for register a {@link User}
 */
public class RegisterInteractor implements IRegisterInput {

    IRegisterOutput output;
    IRegisterDataAccess registerAccess;
    IPasswordHasher hasher;

    /**
     * Creates a new {@link RegisterInteractor}
     *
     * @param output      {@link IRegisterOutput} to output to
     * @param registerAccess {@link IRegisterDataAccess} to register using
     * @param hasher      {@link IPasswordHasher} to hash password
     */
    public RegisterInteractor(IRegisterOutput output, IRegisterDataAccess registerAccess, IPasswordHasher hasher){
        this.hasher = hasher;
        this.output = output;
        this.registerAccess = registerAccess;
    }

    
    /**
     * Register with the specified {@link byte[]}
     *
     * @param username     username to register, or null
     * @param password     password to register into
     * @param providerUserInfo user's personal information to register, or null
     */
    @Override
    public void register(String username, String password, ProviderUserInfo providerUserInfo) {
        if (username == null && providerUserInfo == null) {
            output.error("Must define one of username, and userinfo");
        }
        byte[] salt = SHAPasswordHasher.getInstance().randomSalt();
        UserPassword p =new UserPassword(password, salt, hasher);
        registerAccess.register(username, providerUserInfo, p, new FutureCallback<RegisterResult>() {
            @Override
            public void completed(RegisterResult result) {
                if (result.isSuccess()) {
                    output.success(result.getUser(), result.getToken());
                } else {
                    output.failure();
                }

            }

            @Override
            public void failed(Exception ex) {output.error(ex.getMessage());

            }

            @Override
            public void cancelled() {output.abort();}
        });

    }
}

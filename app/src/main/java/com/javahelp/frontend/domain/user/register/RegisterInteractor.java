package com.javahelp.frontend.domain.user.register;


import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.IPasswordHasher;

import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

/**
 * Use case interactor for register a {@link User}
 */
public class RegisterInteractor implements IRegisterInput {

    IRegisterOutput output;
    ISaltDataAccess saltAccess;
    IRegisterDataAccess registerAccess;
    IPasswordHasher hasher;

    /**
     * Creates a new {@link RegisterInteractor}
     *
     * @param output      {@link IRegisterOutput} to output to
     * @param saltAccess  {@link ISaltDataAccess} to get salt from
     * @param registerAccess {@link IRegisterDataAccess} to register using
     * @param hasher      {@link IPasswordHasher} to hash password
     */
    public RegisterInteractor(IRegisterOutput output, ISaltDataAccess saltAccess, IRegisterDataAccess registerAccess, IPasswordHasher hasher){
        this.hasher = hasher;
        this.output = output;
        this.saltAccess = saltAccess;
        this.registerAccess = registerAccess;
    }

    @Override
    public void register(String username, String password, ProviderUserInfo providerUserInfo){
        if (username == null && providerUserInfo == null) {
            output.error("Must define one of username, email, and userinfo");
        }
        saltAccess.getSalt(username, new FutureCallback<byte[]>() {
            @Override
            public void completed(byte[] result) {
                UserPassword p =new UserPassword(password, result, hasher);
                registerWithUserPassword(username, p, providerUserInfo);
            }

            @Override
            public void failed(Exception ex) { output.error(ex.getMessage());}

            @Override
            public void cancelled() {output.abort();}
        });


    }


    /**
     * Register with the specified {@link byte[]}
     *
     * @param username     username to register, or null
     * @param password     {@link byte[]} to log into
     * @param providerUserInfo user's personal information to register, or null
     */
    private void registerWithUserPassword(String username, UserPassword password, ProviderUserInfo providerUserInfo) {
        registerAccess.register(username, providerUserInfo, password, new FutureCallback<RegisterResult>() {
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

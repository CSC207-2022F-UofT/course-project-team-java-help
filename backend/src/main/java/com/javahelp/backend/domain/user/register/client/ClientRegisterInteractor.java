package com.javahelp.backend.domain.user.register.client;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.register.RegisterUserResponse;
import com.javahelp.backend.domain.user.register.UserRegisterInteractor;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

/**
 * Interactor that creates new client {@link User}s
 */
public class ClientRegisterInteractor extends
        UserRegisterInteractor<IClientRegisterInputBoundary, ClientUserInfo> {

    /**
     * Creates a new {@link ClientRegisterInteractor} instance
     *
     * @param userStore  {@link IUserStore} to use
     */
    public ClientRegisterInteractor(IUserStore userStore) {
        super(userStore);
    }

    @Override
    protected ClientUserInfo bundleUserInfo(IClientRegisterInputBoundary boundary) {
        return new ClientUserInfo(boundary.getEmailAddress(), boundary.getAddress(),
                boundary.getPhoneNumber(), boundary.getFirstName(), boundary.getLastName());
    }

    @Override
    protected RegisterUserResponse isValidated(IClientRegisterInputBoundary boundary) {
        return null;
    }
}

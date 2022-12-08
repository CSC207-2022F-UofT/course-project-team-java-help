package com.javahelp.backend.domain.user.register.provider;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.register.UserRegisterResult;
import com.javahelp.backend.domain.user.register.UserRegisterInteractor;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;

/**
 * Interactor that registers new provider {@link User}s
 */
public class ProviderRegisterInteractor extends
        UserRegisterInteractor<IProviderRegisterInputBoundary, ProviderUserInfo> {

    /**
     * Creates a new {@link ProviderRegisterInteractor} instance
     * @param userStore {@link IUserStore} to use
     */
    public ProviderRegisterInteractor(IUserStore userStore) {
        super(userStore);
    }

    @Override
    protected ProviderUserInfo bundleUserInfo(IProviderRegisterInputBoundary boundary) {
        ProviderUserInfo p = new ProviderUserInfo(boundary.getEmailAddress(),
                boundary.getAddress(), boundary.getPhoneNumber(), boundary.getPracticeName());
        p.setCertified(boundary.getCertified());
        return p;
    }

    @Override
    protected UserRegisterResult isValidated(IProviderRegisterInputBoundary boundary) {
        return null;
    }
}

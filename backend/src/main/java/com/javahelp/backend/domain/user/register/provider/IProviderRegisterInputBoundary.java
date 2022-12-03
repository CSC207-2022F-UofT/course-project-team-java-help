package com.javahelp.backend.domain.user.register.provider;

import com.javahelp.backend.domain.user.register.IUserRegisterInputBoundary;

/**
 * Information needed for a provider to register
 */
public interface IProviderRegisterInputBoundary extends IUserRegisterInputBoundary {

    /**
     * @return the address of the provider's practice
     */
    String getAddress();

    /**
     * @return the phone number of the provider or their practice
     */
    String getPhoneNumber();

    /**
     * @return practice name
     */
    String getPracticeName();

    /**
     * @return the certification status of the provider
     */
    boolean getCertified();
}

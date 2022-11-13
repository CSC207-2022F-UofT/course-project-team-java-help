package com.javahelp.backend.register;

import com.javahelp.backend.register.UserRequestModel;
import com.javahelp.backend.register.client.ClientRequestModel;
import com.javahelp.backend.register.provider.ProviderRequestModel;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;

/**
 * An interface to be implemented by ProviderRegisterInteractor upon input
 */
public interface UserInputBoundary {
    /**
     * The input info of the User
     */
    UserRequestModel model = null;

    /**
     * Make an entity that would store all the input data of this provider
     * @param model the given model of this provider
     * @return an instance that represents Provider Userinfo to be stored in a database
     */
    User create(ProviderRequestModel model);

    /**
     * Make an entity that would store all the input data of this client
     * @param model the given model of this provider
     * @return an instance that represents Provider Userinfo to be stored in a database
     */
    User create(ClientRequestModel model);
}

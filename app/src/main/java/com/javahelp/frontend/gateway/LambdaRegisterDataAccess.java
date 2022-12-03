package com.javahelp.frontend.gateway;

import com.javahelp.frontend.domain.user.register.IRegisterDataAccess;
import com.javahelp.frontend.domain.user.register.RegisterResult;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Future;

public class LambdaRegisterDataAccess extends RESTAPIGateway<RegisterResult> implements IRegisterDataAccess {

    private static final String ENDPOINT = "";

    private static final LambdaRegisterDataAccess instance = new LambdaRegisterDataAccess();

    private static java.net.URI URI = null;

    static {
        try {
            URI = new URI(ENDPOINT);
        } catch (URISyntaxException ignored) {
            // ignore this exception, fine since the final string passed is a valid URI
        }
    }

    /**
     * @return an instance of {@link LambdaRegisterDataAccess}
     */
    public static LambdaRegisterDataAccess getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private LambdaRegisterDataAccess() {

    }


    @Override
    public Future<RegisterResult> register(String username, ProviderUserInfo providerUserInfo, UserPassword password, FutureCallback<RegisterResult> callback) {
        return null;
    }

    @Override
    protected RESTAPIGatewayResponse<RegisterResult> fromInternal(InternalRESTGatewayResponse response) {
        return null;
    }
}

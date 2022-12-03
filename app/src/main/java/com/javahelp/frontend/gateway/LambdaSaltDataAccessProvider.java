package com.javahelp.frontend.gateway;

import com.javahelp.frontend.domain.user.register.ISaltDataAccess;

import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;

public class LambdaSaltDataAccessProvider extends RESTAPIGateway<byte[]> implements ISaltDataAccess {

    private static final String ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/";

    private static final LambdaSaltDataAccessProvider instance = new LambdaSaltDataAccessProvider();

    /**
     * @return an instance of {@link LambdaSaltDataAccessProvider}
     */
    public static LambdaSaltDataAccessProvider getInstance() {
        return instance;
    }

    @Override
    public Future<byte[]> getSalt(String username, FutureCallback<byte[]> callback) {
        return null;
    }

    @Override
    protected RESTAPIGatewayResponse<byte[]> fromInternal(InternalRESTGatewayResponse response) {
        return null;
    }

    /**
     * Private constructor
     */

    }


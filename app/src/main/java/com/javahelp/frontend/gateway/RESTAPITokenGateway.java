package com.javahelp.frontend.gateway;

import com.javahelp.model.token.Token;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.core5.concurrent.FutureCallback;

import java.util.concurrent.Future;

/**
 * {@link RESTAPIGateway<T>} that makes use of {@link Token} authentication
 *
 * @param <T> type of response to expect
 */
public abstract class RESTAPITokenGateway<T> extends RESTAPIGateway<T> {

    private static final String AUTH_TYPE = "JavaHelp";

    IAuthInformationProvider provider;

    /**
     * Creates a new {@link RESTAPITokenGateway<T>}
     *
     * @param provider {@link IAuthInformationProvider} to use for authentication
     */
    public RESTAPITokenGateway(IAuthInformationProvider provider) {
        this.provider = provider;
    }

    /**
     * Makes a request using JavaHelp authorization.
     * Overwrites existing Authorization headers
     *
     * @param request  {@link SimpleHttpRequest} to make
     * @param callback {@link FutureCallback} to execute
     * @return {@link Future} of {@link RESTAPIGatewayResponse<T>}
     */
    @Override
    protected Future<RESTAPIGatewayResponse<T>> getResponse(SimpleHttpRequest request, FutureCallback<RESTAPIGatewayResponse<T>> callback) {

        String token = provider.getTokenString(), id = provider.getUserID();

        if (token == null || id == null) {
            throw new IllegalStateException("Provider for this TokenGateway is missing token or id");
        }

        request.setHeader("Authorization", getAuthString(token, id));

        return super.getResponse(request, callback);
    }

    /**
     * @param id id {@link String} to use
     * @param token token {@link String} to use
     * @return Auth {@link String} for a request
     */
    private String getAuthString(String token, String id) {
        return AUTH_TYPE +
                " id=" + id +
                " token=" + token;
    }
}

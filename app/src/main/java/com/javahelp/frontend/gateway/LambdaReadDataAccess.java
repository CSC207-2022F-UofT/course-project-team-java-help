package com.javahelp.frontend.gateway;

import com.javahelp.frontend.domain.user.login.LoginResult;
import com.javahelp.frontend.domain.user.read.IReadDataAccess;
import com.javahelp.model.user.User;
import com.javahelp.model.util.json.UserConverter;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.concurrent.FutureCallback;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of {@link com.javahelp.frontend.domain.user.read.IReadDataAccess} through AWS Lambda backend
 */
public class LambdaReadDataAccess extends RESTAPITokenGateway<User> implements IReadDataAccess {

    private static final String ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/";

    /**
     * Creates a {@link LambdaReadDataAccess}
     *
     * @param provider {@link IAuthInformationProvider} to use to provide information
     */
    public LambdaReadDataAccess(IAuthInformationProvider provider) {
        super(provider);
    }

    @Override
    public Future<User> read(String id, FutureCallback<User> callback) {

        URI uri = null;

        try {
            uri = new URI(ENDPOINT + id);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Not a valid user id");
        }

        SimpleHttpRequest request = SimpleHttpRequest.create("GET", uri);

        request.setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(15000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(15000, TimeUnit.MILLISECONDS)
                .setConnectTimeout(15000, TimeUnit.MILLISECONDS)
                .build());

        FutureCallback<RESTAPIGatewayResponse<User>> passedCallback = callback == null ? null : new FutureCallback<RESTAPIGatewayResponse<User>>() {
            @Override
            public void completed(RESTAPIGatewayResponse<User> result) {
                if (result.isSuccess()) {
                    callback.completed(result.get());
                } else {
                    callback.failed(new RuntimeException(result.getErrorMessage()));
                }
            }

            @Override
            public void failed(Exception ex) {
                callback.failed(ex);
            }

            @Override
            public void cancelled() {
                callback.cancelled();
            }
        };

        Future<RESTAPIGatewayResponse<User>> response = getResponse(request, passedCallback);

        return new Future<User>() {
            @Override
            public boolean cancel(boolean b) {
                return response.cancel(b);
            }

            @Override
            public boolean isCancelled() {
                return response.isCancelled();
            }

            @Override
            public boolean isDone() { return response.isDone(); }

            @Override
            public User get() throws ExecutionException, InterruptedException {
                return response.get().get();
            }

            @Override
            public User get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return response.get(l, timeUnit).get();
            }
        };
    }

    @Override
    protected RESTAPIGatewayResponse<User> fromInternal(InternalRESTGatewayResponse response) {
        UserConverter converter = UserConverter.getInstance();

        if (response.getResponseCode() == 200
                && response.shouldHaveBody()
                && response.isSuccessfullyParsed()) {
            User u = converter.fromJSON(response.getBody());

            if (u == null) {
                return new RESTAPIGatewayResponse<>("Error parsing user");
            }

            return new RESTAPIGatewayResponse<>(u);
        } else if (response.getResponseCode() == 403) {
            return new RESTAPIGatewayResponse<>("No access to specified user");
        } else if (response.getResponseCode() == 401) {
            return new RESTAPIGatewayResponse<>(
                    "Request must be authorized to view this information");
        } else if (response.getResponseCode() == 400) {
            return new RESTAPIGatewayResponse<>("Invalid request, must have" +
                    "correct user id and authorization");
        } else {
            return new RESTAPIGatewayResponse<>("Unspecified error fetching user");
        }
    }
}

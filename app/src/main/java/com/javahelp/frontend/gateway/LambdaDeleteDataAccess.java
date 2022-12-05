package com.javahelp.frontend.gateway;

import com.javahelp.frontend.domain.user.delete.DeleteResult;
import com.javahelp.frontend.domain.user.delete.IDeleteDataAccess;
import com.javahelp.model.user.User;
import com.javahelp.model.util.json.UserConverter;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.concurrent.CompletedFuture;
import org.apache.hc.core5.concurrent.FutureCallback;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.json.JsonObject;

/**
 * An implementation of the {@link IDeleteDataAccess} interface with lambda,
 * used for deleting accounts.
 */
public class LambdaDeleteDataAccess extends RESTAPITokenGateway<DeleteResult> implements IDeleteDataAccess {

    private static final String USER_ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/";

    /**
     * Creates a new {@link RESTAPITokenGateway<DeleteResult>}
     *
     * @param provider {@link IAuthInformationProvider} to use for authentication
     */
    public LambdaDeleteDataAccess(IAuthInformationProvider provider) {
        super(provider);
    }

    /**
     * A helper method for getting the {@link URI} for this delete request.
     * @return the {@link URI} of this user.
     */
    private URI getURI() throws URISyntaxException {
        if (provider.getUserID() == null) {
            throw new IllegalStateException("Provider for this data access is missing user id");
        }
        return new URI(USER_ENDPOINT + provider.getUserID());
    }

    @Override
    public Future<DeleteResult> delete(String userID, FutureCallback<DeleteResult> callback) {
        URI uri;
        try {
            uri = getURI();
        } catch (URISyntaxException ex) {
            callback.failed(ex);
            return new CompletedFuture<>(null);
        }

        SimpleHttpRequest request = SimpleHttpRequest.create("DELETE", uri);

        request.setHeader("Content-Type", "application/json");
        request.setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(15000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(15000, TimeUnit.MILLISECONDS)
                .setConnectTimeout(15000, TimeUnit.MILLISECONDS)
                .build());

        FutureCallback<RESTAPIGatewayResponse<DeleteResult>> passedCallback =
                callback == null ? null : new FutureCallback<RESTAPIGatewayResponse<DeleteResult>>() {
                    @Override
                    public void completed(RESTAPIGatewayResponse<DeleteResult> result) {
                        if (result.isSuccess()) {
                            callback.completed(result.get());
                        }
                        else {
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

        Future<RESTAPIGatewayResponse<DeleteResult>> response = getResponse(request, passedCallback);

        return new Future<DeleteResult>() {
            @Override
            public boolean cancel(boolean b) {
                return response.cancel(b);
            }

            @Override
            public boolean isCancelled() {
                return response.isCancelled();
            }

            @Override
            public boolean isDone() {
                return response.isDone();
            }

            @Override
            public DeleteResult get() throws ExecutionException, InterruptedException {
                return response.get().get();
            }

            @Override
            public DeleteResult get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return response.get(l, timeUnit).get();
            }
        };
    }

    @Override
    protected RESTAPIGatewayResponse<DeleteResult> fromInternal(InternalRESTGatewayResponse response) {
        if (response.shouldHaveBody() && response.isSuccessfullyParsed()) {
            JsonObject body = response.getBody();

            if (body.containsKey("user")) {
                final UserConverter userConverter = UserConverter.getInstance();
                JsonObject userO = body.getJsonObject("user");
                User user = userConverter.fromJSON(userO);

                if (user == null) {
                    return new RESTAPIGatewayResponse<>("Unable to parse user or token");
                }
                return new RESTAPIGatewayResponse<>(new DeleteResult(user));
            }
            else if (body.containsKey("errorMessage")) {
                String errorMessage = body.getString("errorMessage");
                return new RESTAPIGatewayResponse<>(new DeleteResult(errorMessage));
            }
            else {
                return new RESTAPIGatewayResponse<>("JSON missing relevant fields");
            }
        }

        return new RESTAPIGatewayResponse<>("Response could not be parsed into JSON");
    }
}

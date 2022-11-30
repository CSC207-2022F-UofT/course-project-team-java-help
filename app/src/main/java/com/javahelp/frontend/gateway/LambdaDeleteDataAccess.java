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
 * Data access for delete through lambda.
 */
public class LambdaDeleteDataAccess extends RESTAPIGateway<DeleteResult> implements IDeleteDataAccess {
    private static final IDeleteDataAccess instance = new LambdaDeleteDataAccess();
    private static final String USER_ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/users/";

    /**
     * A private constructor for {@link LambdaDeleteDataAccess}.
     */
    public LambdaDeleteDataAccess() {

    }

    @Override
    public Future<DeleteResult> delete(String userID, FutureCallback<DeleteResult> callback) {
        URI uri;
        try {
            uri = getURI(userID);
        } catch (URISyntaxException e) {
            callback.failed(e);
            return new CompletedFuture<>(null);
        }

        SimpleHttpRequest request = SimpleHttpRequest.create("DELETE", uri);

        request.setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(15000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(15000, TimeUnit.MILLISECONDS)
                .setConnectTimeout(15000, TimeUnit.MILLISECONDS)
                .build());

        FutureCallback<RESTAPIGatewayResponse<DeleteResult>> passedCallback = callback == null ? null : new FutureCallback<RESTAPIGatewayResponse<DeleteResult>>() {
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

    /**
     * A helper method to get the {@link URI} of the {@link User} with the {@link String} userID.
     *
     * @param userID: the {@link String} userID of the {@link User}.
     *
     * @return the {@link URI} of the {@link User}.
     */
    private static URI getURI(String userID) throws URISyntaxException {
        return new URI(USER_ENDPOINT + userID);
    }


    /**
     * @return an instance of {@link LambdaDeleteDataAccess}.
     */
    public static IDeleteDataAccess getInstance() {
        return instance;
    }

    @Override
    protected RESTAPIGatewayResponse<DeleteResult> fromInternal(InternalRESTGatewayResponse response) {

        if (response.shouldHaveBody() && response.isSuccessfullyParsed()) {
            JsonObject json = response.getBody();
            if (json.containsKey("user")) {
                JsonObject userO = json.getJsonObject("user");
                final UserConverter userConverter = UserConverter.getInstance();
                User user = userConverter.fromJSON(userO);

                if (user == null) {
                    return new RESTAPIGatewayResponse<>("Unable to parse user");
                }

                return new RESTAPIGatewayResponse<>(new DeleteResult(user));
            }
            else if (json.containsKey("errorMessage")) {
                String errorMessage = json.getString("errorMessage");

                return new RESTAPIGatewayResponse<>(new DeleteResult(errorMessage));
            }
            else if (response.getResponseCode() == 403) {
                return new RESTAPIGatewayResponse<>(new DeleteResult("The path parameters do not match the given current user"));
            }
            else {
                return new RESTAPIGatewayResponse<>("JSON missing relevant fields");
            }
        }

        return new RESTAPIGatewayResponse<>("Response could not be parsed into JSON");
    }
}

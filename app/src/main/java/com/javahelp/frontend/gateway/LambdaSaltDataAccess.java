package com.javahelp.frontend.gateway;

import com.javahelp.frontend.domain.user.login.ISaltDataAccess;
import com.javahelp.model.user.User;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.core5.concurrent.CompletedFuture;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.json.JsonObject;

/**
 * Gateway to access salt through lambda
 */
public class LambdaSaltDataAccess extends RESTAPIGateway<byte[]> implements ISaltDataAccess {

    private static final String ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/";

    /**
     * Gets the {@link URI} to get a {@link User}'s salt
     * @param username username of {@link User}
     * @param email email of {@link User}
     * @param id {@link String} id of {@link User}
     * @return {@link URI}
     */
    private static URI getURI(String username, String email, String id) throws URISyntaxException {
        if (id != null) {
            return new URI(ENDPOINT + "users/" + id + "/salt");
        } else if (username != null) {
            return new URIBuilder(ENDPOINT + "users/salt")
                    .addParameter("username", username)
                    .build();
        } else {
            return new URIBuilder(ENDPOINT + "users/salt")
                    .addParameter("email", email).build();
        }
    }

    @Override
    public Future<byte[]> getSalt(String username, String email, String id, FutureCallback<byte[]> callback) {
        URI uri;
        try {
            uri = getURI(username, email, id);
        } catch (URISyntaxException e) {
            callback.failed(e);
            return new CompletedFuture<>(null);
        }
        SimpleHttpRequest request = SimpleHttpRequest.create("GET", uri);

        FutureCallback<RESTAPIGatewayResponse<byte[]>> passedCallback = new FutureCallback<RESTAPIGatewayResponse<byte[]>>() {
            @Override
            public void completed(RESTAPIGatewayResponse<byte[]> result) {
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

        Future<RESTAPIGatewayResponse<byte[]>> response = getResponse(request, passedCallback);
        return new Future<byte[]>() {
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
            public byte[] get() throws ExecutionException, InterruptedException {
                return response.get().get();
            }

            @Override
            public byte[] get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return response.get(l, timeUnit).get();
            }
        };
    }

    @Override
    protected RESTAPIGatewayResponse<byte[]> fromInternal(InternalRESTGatewayResponse response) {
        if (response.shouldHaveBody() && response.isSuccessfullyParsed()) {
            JsonObject json = response.getBody();
            if (json.containsKey("salt")) {
                String saltS = json.getString("salt");
                byte[] salt = Base64.getDecoder().decode(saltS);
                return new RESTAPIGatewayResponse<>(salt);
            } else if (json.containsKey("errorMessage")) {
                return new RESTAPIGatewayResponse<>(json.getString("errorMessage"));
            } else {
                return new RESTAPIGatewayResponse<>("JSON missing relevant fields");
            }
        }
        return new RESTAPIGatewayResponse<>("Response could not be parsed into JSON");
    }
}

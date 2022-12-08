package com.javahelp.frontend.gateway;

import com.javahelp.frontend.domain.user.login.ISaltDataAccess;
import com.javahelp.model.user.User;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.config.RequestConfig;
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

    private static final LambdaSaltDataAccess instance = new LambdaSaltDataAccess();

    /**
     * @return an instance of {@link LambdaSaltDataAccess}
     */
    public static LambdaSaltDataAccess getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private LambdaSaltDataAccess() {


    }

    /**
     * Gets the {@link URI} to get a {@link User}'s salt
     *
     * @param username username of {@link User}
     * @param email    email of {@link User}
     * @param id       {@link String} id of {@link User}
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

        request.setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(15000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(15000, TimeUnit.MILLISECONDS)
                .setConnectTimeout(15000, TimeUnit.MILLISECONDS)
                .build());

        FutureCallback<RESTAPIGatewayResponse<byte[]>> passedCallback = callback == null ? null : new FutureCallback<RESTAPIGatewayResponse<byte[]>>() {
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
        if (response.getResponseCode() == 404) {
            return new RESTAPIGatewayResponse<>("User not found");
        } else if (response.shouldHaveBody() && response.isSuccessfullyParsed()) {
            return fromJSONInternal(response);
        }
        return new RESTAPIGatewayResponse<>("Response could not be parsed into JSON");
    }

    /**
     * Gets a {@link RESTAPIGatewayResponse} from a {@link com.javahelp.frontend.gateway.RESTAPIGateway.InternalRESTGatewayResponse}
     * that contains a valid {@link JsonObject} body
     * @param response {@link com.javahelp.frontend.gateway.RESTAPIGateway.InternalRESTGatewayResponse} to parse
     * @return {@link RESTAPIGatewayResponse} derived from body
     */
    private RESTAPIGatewayResponse<byte[]> fromJSONInternal(InternalRESTGatewayResponse response) {
        JsonObject json = response.getBody();
        if (response.getResponseCode() == 200 && json.containsKey("salt")) {
            String saltS = json.getString("salt");
            byte[] salt = Base64.getDecoder().decode(saltS);
            return new RESTAPIGatewayResponse<>(salt);
        } else if (json.containsKey("errorMessage")) {
            return new RESTAPIGatewayResponse<>(json.getString("errorMessage"));
        } else {
            return new RESTAPIGatewayResponse<>("JSON missing relevant fields");
        }
    }
}

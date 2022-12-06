package com.javahelp.frontend.gateway;

import com.javahelp.frontend.domain.user.Client_register.IRegisterDataAccess;
import com.javahelp.frontend.domain.user.Client_register.RegisterResult;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;
import com.javahelp.model.util.json.TokenConverter;
import com.javahelp.model.util.json.UserConverter;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class LambdaCRegisterDataAccess extends RESTAPIGateway<RegisterResult> implements IRegisterDataAccess {

    private static final String ENDPOINT = "";
    private static final LambdaCRegisterDataAccess instance = new LambdaCRegisterDataAccess();

    private static java.net.URI URI = null;

    static {
        try {
            URI = new URI(ENDPOINT);
        } catch (URISyntaxException ignored) {
            // ignore this exception, fine since the final string passed is a valid URI
        }
    }

    /**
     * @return an instance of {@link LambdaCRegisterDataAccess}
     */
    public static LambdaCRegisterDataAccess getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private LambdaCRegisterDataAccess() {

    }

    @Override
    public Future<RegisterResult> register(String username, ClientUserInfo clientUserInfo, UserPassword password, FutureCallback<RegisterResult> callback) {

        JsonObjectBuilder bodyBuilder = Json.createObjectBuilder()
                .add("saltHas", password.getBase64SaltHash());
        if (username != null) {
            bodyBuilder.add("username", username);
        } else if (clientUserInfo != null) {
            bodyBuilder.add("infotype", "PROVIDER");
            bodyBuilder.add("email", clientUserInfo.getEmailAddress());
            bodyBuilder.add("address", clientUserInfo.getAddress());
            bodyBuilder.add("phonenumber", clientUserInfo.getPhoneNumber());
            bodyBuilder.add("firstname", clientUserInfo.getFirstName());
            bodyBuilder.add("lastname", clientUserInfo.getLastName());
        }
        SimpleHttpRequest request = SimpleHttpRequest.create("POST", URI);
        request.setBody(bodyBuilder.build().toString(), ContentType.APPLICATION_JSON);
        request.setHeader("Content-Type", "application/json");

        request.setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(15000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(15000, TimeUnit.MILLISECONDS)
                .setConnectTimeout(15000, TimeUnit.MILLISECONDS)
                .build());

        FutureCallback<RESTAPIGatewayResponse<RegisterResult>> passedCallback = callback == null ? null : new FutureCallback<RESTAPIGatewayResponse<RegisterResult>>() {
            @Override
            public void completed(RESTAPIGatewayResponse<RegisterResult> result) {
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
        Future<RESTAPIGatewayResponse<RegisterResult>> response = getResponse(request, passedCallback);

        return new Future<RegisterResult>() {
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
            public RegisterResult get() throws ExecutionException, InterruptedException {
                return response.get().get();
            }

            @Override
            public RegisterResult get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return response.get(l, timeUnit).get();
            }
        };




    }

    @Override
    protected RESTAPIGatewayResponse<RegisterResult> fromInternal(InternalRESTGatewayResponse response) {

        UserConverter userConverter = UserConverter.getInstance();
        TokenConverter tokenConverter = TokenConverter.getInstance();

        if (response.shouldHaveBody() && response.isSuccessfullyParsed()) {
            JsonObject json = response.getBody();
            if (json.containsKey("user" ) && json.containsKey("token")) {
                JsonObject userO = json.getJsonObject("user");
                JsonObject tokenO = json.getJsonObject("token");
                User user = userConverter.fromJSON(userO);
                Token token = tokenConverter.fromJSON(tokenO);

                if (user == null || token == null) {
                    return new RESTAPIGatewayResponse<>("Unable to parse user or token");
                }

                return new RESTAPIGatewayResponse<>(new RegisterResult(user, token));
            } else if (response.getResponseCode() == 200) {
                return new RESTAPIGatewayResponse<>(new RegisterResult("Authentication failed"));
            }
            else if (json.containsKey("errorMessage")) {
                return new RESTAPIGatewayResponse<>(json.getString("errorMessage"));
            } else {
                return new RESTAPIGatewayResponse<>("JSON missing relevant fields");
            }
        }
        return new RESTAPIGatewayResponse<>("Response could not be parsed into JSON");
    }
}

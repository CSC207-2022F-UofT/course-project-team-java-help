package com.javahelp.frontend.gateway;

import com.javahelp.frontend.domain.user.login.ILoginDataAccess;
import com.javahelp.frontend.domain.user.login.LoginResult;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;
import com.javahelp.model.util.json.IJSONConverter;
import com.javahelp.model.util.json.TokenConverter;
import com.javahelp.model.util.json.UserConverter;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * Data access for login through lambda
 */
public class LambdaLoginDataAccess extends RESTAPIGateway<LoginResult> implements ILoginDataAccess {

    private static final String ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/login";

    private static final LambdaLoginDataAccess instance = new LambdaLoginDataAccess();

    private static URI URI = null;

    static {
        try {
            URI = new URI(ENDPOINT);
        } catch (URISyntaxException ignored) {
            // ignore this exception, fine since the final string passed is a valid URI
        }
    }

    /**
     * @return an instance of {@link LambdaLoginDataAccess}
     */
    public static LambdaLoginDataAccess getInstance() {
        return instance;
    }

    /**
     * Private constructor
     */
    private LambdaLoginDataAccess() {

    }

    @Override
    public Future<LoginResult> login(String username, String email, String id, UserPassword password, boolean stayLoggedIn, FutureCallback<LoginResult> callback) {
        JsonObjectBuilder bodyBuilder = Json.createObjectBuilder()
                .add("saltHash", password.getBase64SaltHash())
                .add("stayLoggedIn", stayLoggedIn);

        if (id != null) {
            bodyBuilder.add("id", id);
        } else if (username != null) {
            bodyBuilder.add("username", username);
        } else {
            bodyBuilder.add("email", email);
        }

        SimpleHttpRequest request = SimpleHttpRequest.create("POST", URI);
        request.setBody(bodyBuilder.build().toString(), ContentType.APPLICATION_JSON);
        request.setHeader("Content-Type", "application/json");

        FutureCallback<RESTAPIGatewayResponse<LoginResult>> passedCallback = new FutureCallback<RESTAPIGatewayResponse<LoginResult>>() {
            @Override
            public void completed(RESTAPIGatewayResponse<LoginResult> result) {
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

        Future<RESTAPIGatewayResponse<LoginResult>> response = getResponse(request, passedCallback);

        return new Future<LoginResult>() {
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
            public LoginResult get() throws ExecutionException, InterruptedException {
                return response.get().get();
            }

            @Override
            public LoginResult get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return response.get(l, timeUnit).get();
            }
        };
    }

    @Override
    protected RESTAPIGatewayResponse<LoginResult> fromInternal(InternalRESTGatewayResponse response) {
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

                return new RESTAPIGatewayResponse<>(new LoginResult(user, token));
            } else if (response.getResponseCode() == 200) {
                return new RESTAPIGatewayResponse<>(new LoginResult("Authentication failed"));
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

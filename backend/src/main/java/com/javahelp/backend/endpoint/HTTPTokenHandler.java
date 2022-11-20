package com.javahelp.backend.endpoint;

import static com.javahelp.backend.endpoint.APIGatewayResponse.FORBIDDEN;
import static com.javahelp.backend.endpoint.APIGatewayResponse.UNAUTHENTICATED;

import com.amazonaws.HttpMethod;
import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.authentication.ITokenAuthInput;
import com.javahelp.backend.domain.user.authentication.TokenAuthManager;
import com.javahelp.backend.domain.user.authentication.TokenAuthResult;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

import java.util.Map;

import jakarta.json.JsonObject;

/**
 * {@link HTTPHandler} for tokens. Requests must have token and user parameters containing
 * the token {@link String} to use to authenticate, and the id of the {@link User} to authenticate
 * for respectively.
 */
public abstract class HTTPTokenHandler extends HTTPHandler implements ITokenAuthInput {

    private String tokenString, userId;

    @Override
    public APIGatewayResponse getResponse(JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters) {
        if (!parameters.containsKey("token") || !parameters.containsKey("user")) {
            return APIGatewayResponse.error(UNAUTHENTICATED, "Request must authenticate with" +
                    "token and user query string parameters");
        }

        tokenString = parameters.get("token")[0];
        userId = parameters.get("user")[0];

        TokenAuthManager interactor = new TokenAuthManager(IUserStore.getDefaultImplementation(), ITokenStore.getDefaultImplementation());
        TokenAuthResult result = interactor.authenticate(this);

        if (result.isSuccess()) {
            return authenticatedGetResponse(result.getUser(), result.getToken(), body, method, headers, parameters);
        } else {
            return APIGatewayResponse.error(FORBIDDEN, result.getErrorMessage());
        }
    }

    /**
     * Gets the response after authentication has been confirmed
     * @param u the authenticated {@link User}
     * @param t the {@link Token} used to authenticate
     * @param body {@link JsonObject} body
     * @param method {@link HttpMethod} used
     * @param headers {@link Map} of header {@link String}s to {@link String} arrays of values
     * @param parameters {@link Map} of parameter {@link String}s to arrays of values
     * @return {@link APIGatewayResponse} to send
     */
    public abstract APIGatewayResponse authenticatedGetResponse(User u, Token t, JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters);

    @Override
    public String getUserID() {
        return userId;
    }

    @Override
    public String getToken() {
        return tokenString;
    }
}

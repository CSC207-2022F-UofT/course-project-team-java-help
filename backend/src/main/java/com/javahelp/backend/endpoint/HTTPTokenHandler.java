package com.javahelp.backend.endpoint;

import static com.javahelp.backend.endpoint.APIGatewayResponse.BAD_REQUEST;
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
 * {@link HTTPHandler} for tokens. Requests must have Authorization header of type JavaHelp containing
 * the token {@link String} to use to authenticate, and the id of the {@link User} to authenticate
 * for respectively.
 */
public abstract class HTTPTokenHandler extends HTTPHandler implements ITokenAuthInput {

    /**
     * JavaHelp authentication type identifier
     */
    private static final String AUTH_TYPE = "JavaHelp";

    private String tokenString, userId;

    @Override
    public APIGatewayResponse getResponse(JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters) {
        if (!headers.containsKey("authorization")) {
            return APIGatewayResponse.error(UNAUTHENTICATED, "Request must authenticate with" +
                    "authorization header");
        }

        String authString = headers.get("authorization")[0];

        String[] authComponents = authString.split(" ");

        if (authComponents.length == 0) {
            return APIGatewayResponse.error(BAD_REQUEST, "No authorization information found " +
                    "in provided header");
        } else if (!AUTH_TYPE.equals(authComponents[0])) {
            return APIGatewayResponse.error(BAD_REQUEST, "Authorization header must be of type " +
                    AUTH_TYPE);
        } else if (authComponents.length < 3 || !extractTokenAndUserID(authComponents)) {
            return APIGatewayResponse.error(BAD_REQUEST, "Must include user id and token " +
                    "in authorization header");
        }

        TokenAuthManager interactor = new TokenAuthManager(IUserStore.getDefaultImplementation(), ITokenStore.getDefaultImplementation());
        TokenAuthResult result = interactor.authenticate(this);

        if (result.isSuccess()) {
            return authenticatedGetResponse(result.getUser(), result.getToken(), body, method, headers, parameters);
        } else {
            return APIGatewayResponse.error(UNAUTHENTICATED, result.getErrorMessage()); // invalid credentials
        }
    }

    /**
     * Gets the response after authentication has been confirmed
     *
     * @param u          the authenticated {@link User}
     * @param t          the {@link Token} used to authenticate
     * @param body       {@link JsonObject} body
     * @param method     {@link HttpMethod} used
     * @param headers    {@link Map} of header {@link String}s to {@link String} arrays of values
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

    /**
     * Extracts the {@link Token} and {@link User} id from a valid
     * authorization header
     *
     * @param authHeader {@link String} array of values from header
     * @return whether successful
     */
    private boolean extractTokenAndUserID(String[] authHeader) {
        for (String s : authHeader) {
            if (s.startsWith("id=")) {
                userId = s.substring(3);
            } else if (s.startsWith("username=")) {
                tokenString = s.substring(9);
            }
            if (tokenString != null && userId != null) {
                return true;
            }
        }
        return false;
    }
}

package com.javahelp.backend.endpoint.user;

import static com.javahelp.backend.endpoint.APIGatewayResponse.BAD_REQUEST;
import static com.javahelp.backend.endpoint.APIGatewayResponse.NOT_FOUND;
import static com.javahelp.backend.endpoint.APIGatewayResponse.OK;

import com.amazonaws.HttpMethod;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.authentication.ISaltInputBoundary;
import com.javahelp.backend.domain.user.authentication.SaltInteractor;
import com.javahelp.backend.domain.user.authentication.SaltResult;
import com.javahelp.backend.endpoint.APIGatewayResponse;
import com.javahelp.backend.endpoint.HTTPHandler;
import com.javahelp.model.user.User;

import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Handler for getting salt for a {@link User}
 */
public class SaltHandler extends HTTPHandler implements ISaltInputBoundary {

    private String username;

    private String email;

    private String userId;

    @Override
    public String getUserID() {
        return userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public boolean requiresBody() {
        return false;
    }

    @Override
    public APIGatewayResponse getResponse(JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters, Map<String, String> pathParameters) {

        userId = null;
        username = null;
        email = null;

        if (pathParameters.containsKey("userid")) {
            userId = pathParameters.get("userid");
        }

        if (parameters.containsKey("username")) {
            username = parameters.get("username")[0];
        }

        if (parameters.containsKey("email")) {
            email = parameters.get("email")[0];
        }

        if (email == null && userId == null && username == null) {
            return APIGatewayResponse.error(BAD_REQUEST, "Must include id, email, or username query string parameter");
        }

        SaltInteractor interactor = new SaltInteractor(IUserStore.getDefaultImplementation());
        SaltResult result = interactor.get(this);

        if (result.isSuccess()) {
            String json = Json.createObjectBuilder()
                    .add("salt", result.getSaltBase64())
                    .build().toString();
            return APIGatewayResponse.builder()
                    .setJSONBody(json)
                    .setStatusCode(OK)
                    .build();
        } else {
            String json = Json.createObjectBuilder()
                    .add("errorMessage", result.getErrorMessage())
                    .build().toString();
            return APIGatewayResponse.builder()
                    .setJSONBody(json)
                    .setStatusCode(NOT_FOUND)
                    .build();
        }
    }
}

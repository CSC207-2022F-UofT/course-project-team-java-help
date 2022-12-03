package com.javahelp.backend.endpoint.user;

import static com.javahelp.backend.endpoint.APIGatewayResponse.BAD_REQUEST;
import static com.javahelp.backend.endpoint.APIGatewayResponse.FORBIDDEN;
import static com.javahelp.backend.endpoint.APIGatewayResponse.NOT_FOUND;
import static com.javahelp.backend.endpoint.APIGatewayResponse.OK;

import com.amazonaws.HttpMethod;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.read.IUserReadInput;
import com.javahelp.backend.domain.user.read.UserReadInteractor;
import com.javahelp.backend.endpoint.APIGatewayResponse;
import com.javahelp.backend.endpoint.HTTPTokenHandler;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;
import com.javahelp.model.util.json.UserConverter;

import java.util.Map;

import jakarta.json.JsonObject;

/**
 * Handler for getting {@link User} information for the logged in {@link User}
 */
public class ReadHandler extends HTTPTokenHandler implements IUserReadInput {

    private String userId;

    @Override
    public String getID() {
        return userId;
    }

    @Override
    public boolean requiresBody() {
        return false;
    }

    @Override
    public APIGatewayResponse authenticatedGetResponse(User u, Token t, JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters, Map<String, String> pathParameters) {

        userId = null;

        if (pathParameters.containsKey("userid")) {
            userId = pathParameters.get("userid");
        } else {
            return APIGatewayResponse.error(BAD_REQUEST, "Request missing user id path parameter");
        }

        if (!userId.equals(u.getStringID())) {
            return APIGatewayResponse.error(FORBIDDEN, "You do not have access to the specified user");
        }

        UserReadInteractor interactor = new UserReadInteractor(IUserStore.getDefaultImplementation());
        User result = interactor.readUser(this);

        if (result != null) {
            JsonObject userRepresentation = UserConverter.getInstance().toJSON(result);

            return APIGatewayResponse.builder()
                    .setStatusCode(OK)
                    .setJSONBody(userRepresentation)
                    .build();
        } else {
            return APIGatewayResponse.error(NOT_FOUND, "Unable to locate the specified user");
        }
    }
}

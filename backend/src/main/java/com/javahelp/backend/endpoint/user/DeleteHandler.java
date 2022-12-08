package com.javahelp.backend.endpoint.user;

import static com.javahelp.backend.endpoint.APIGatewayResponse.FORBIDDEN;
import static com.javahelp.backend.endpoint.APIGatewayResponse.OK;

import com.amazonaws.HttpMethod;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.delete.DeleteInteractor;
import com.javahelp.backend.domain.user.delete.DeleteResult;
import com.javahelp.backend.domain.user.delete.IDeleteInputBoundary;
import com.javahelp.backend.endpoint.APIGatewayResponse;
import com.javahelp.backend.endpoint.HTTPTokenHandler;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;
import com.javahelp.model.util.json.UserConverter;

import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * Handler for deleting a {@link User}
 */
public class DeleteHandler extends HTTPTokenHandler implements IDeleteInputBoundary {
    private String userID;

    @Override
    public String getStringID() {
        return userID;
    }

    /**
     * A helper method for verifying that the userID of the {@link User}
     * matches the userID in the path parameters {@link Map} of {@link String}.
     *
     * @param user: the logged in {@link User}.
     * @param pathParameters: {@link Map} of {@link String} path parameter names to {@link String} values
     * @return whether the userID of the {@link User} matches the userID of the {@link Token}.
     */
    private boolean verifyUser(User user, Map<String, String> pathParameters) {
        return user.getStringID().equals(pathParameters.get("userid"));
    }

    @Override
    public boolean requiresBody() {
        return false;
    }

    @Override
    public APIGatewayResponse authenticatedGetResponse(User u, Token t, JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters, Map<String, String> pathParameters) {

        userID = null;

        if (!verifyUser(u, pathParameters)) {
            return APIGatewayResponse.error(FORBIDDEN, "The path parameters do not match the given current user");
        }

        userID = u.getStringID();
        final DeleteInteractor deleteManager = new DeleteInteractor(IUserStore.getDefaultImplementation());
        final DeleteResult deleteResult = deleteManager.delete(this);
        String deleteResponse;

        if (deleteResult.isSuccess()) {
            JsonObject user = UserConverter.getInstance().toJSON(deleteResult.getUser());
            deleteResponse = Json.createObjectBuilder()
                    .add("user", user)
                    .add("success", true)
                    .build().toString();
        }

        else {
            deleteResponse = Json.createObjectBuilder()
                    .add("errorMessage", deleteResult.getErrorMessage())
                    .add("success", false)
                    .build().toString();
        }

        return APIGatewayResponse.builder()
                .setStatusCode(OK)
                .setJSONBody(deleteResponse)
                .build();
    }
}

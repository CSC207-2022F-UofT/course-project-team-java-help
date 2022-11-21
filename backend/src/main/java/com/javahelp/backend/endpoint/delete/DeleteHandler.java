package com.javahelp.backend.endpoint.delete;

import static com.javahelp.backend.endpoint.APIGatewayResponse.FORBIDDEN;
import static com.javahelp.backend.endpoint.APIGatewayResponse.OK;

import com.amazonaws.HttpMethod;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.delete.DeleteManager;
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

public class DeleteHandler extends HTTPTokenHandler implements IDeleteInputBoundary {
    private String userID;

    @Override
    public String getUserID() {
        return userID;
    }

    /**
     * A helper method for verifying that the userID of the {@link User}
     * matches the userID associated with the {@link Token}.
     *
     * @param user: the logged in user.
     * @param token: the token used for authentication.
     * @return whether the userID of the {@link User} matches the userID of the {@link Token}.
     */
    private boolean verifyUser(User user, Token token) {
        return user.getStringID().equals(token.getUserID());
    }

    @Override
    public APIGatewayResponse authenticatedGetResponse(User u, Token t, JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters) {

        if (!verifyUser(u, t)) {
            return APIGatewayResponse.error(FORBIDDEN, "The token doesn't match the given current user");
        }

        final DeleteManager deleteManager = new DeleteManager(IUserStore.getDefaultImplementation());
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

package com.javahelp.backend.endpoint.search;

import static com.javahelp.backend.endpoint.APIGatewayResponse.BAD_REQUEST;
import static com.javahelp.backend.endpoint.APIGatewayResponse.FORBIDDEN;
import static com.javahelp.backend.endpoint.APIGatewayResponse.INTERNAL_SERVER_ERROR;
import static com.javahelp.backend.endpoint.APIGatewayResponse.OK;

import com.amazonaws.HttpMethod;
import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.search.ISearchInputBoundary;
import com.javahelp.backend.domain.search.SearchInteractor;
import com.javahelp.backend.domain.search.SearchResult;
import com.javahelp.backend.endpoint.APIGatewayResponse;
import com.javahelp.backend.endpoint.HTTPTokenHandler;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;
import com.javahelp.model.util.json.SurveyResponseConverter;
import com.javahelp.model.util.json.UserConverter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * Handler for searches for providers
 */
public class SearchHandler extends HTTPTokenHandler implements ISearchInputBoundary {
    private String userID;
    private Set<String> constraint = new HashSet<>();
    private boolean isRanking;

    @Override
    public String[] requiredBodyFields() {
        return new String[]{"userID", "filters"};
    }

    @Override
    public APIGatewayResponse authenticatedGetResponse(User u, Token t, JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters, Map<String, String> pathParameters) {

        userID = null; // set to default in case not new handler
        constraint = new HashSet<>();
        isRanking = false;

        userID = body.getString("userID", null);

        if (!userID.equals(u.getStringID())) {
            return APIGatewayResponse.error(FORBIDDEN, "No access to search from specified user");
        }

        isRanking = body.getBoolean("ranking", false);
        JsonArray filters = body.getJsonArray("filters");

        try {
            for (int i = 0; i < filters.size(); i++) {
                String filter_key = String.format("filter_%s", i);
                String filter = filters.getJsonObject(i).getString(filter_key, null);
                if (filter == null) {
                    return APIGatewayResponse.error(BAD_REQUEST, "Incorrect syntax for filter.");
                }
                constraint.add(filter);
            }
        } catch (RuntimeException e) {
            return APIGatewayResponse.error(BAD_REQUEST, "Invalid filters");
        }

        SearchInteractor interactor = new SearchInteractor(ISurveyResponseStore.getDefaultImplementation(),
                IUserStore.getDefaultImplementation());

        SearchResult result;
        try {
            result = interactor.search(this);
        } catch (RuntimeException e) {
            return APIGatewayResponse.error(INTERNAL_SERVER_ERROR, "Failed search.");
        }

        String response;

        if (result.isSuccess()) {
            JsonArrayBuilder jsonUserBuilder = Json.createArrayBuilder();
            JsonArrayBuilder jsonResponseBuilder = Json.createArrayBuilder();
            for (int i = 0; i < result.getUsers().size(); i++) {
                jsonUserBuilder.add(Json.createObjectBuilder()
                        .add(String.format("user_%s", i),
                                UserConverter.getInstance().toJSON(result.getUsers().get(i))));
                jsonResponseBuilder.add(Json.createObjectBuilder()
                        .add(String.format("response_%s", i),
                                SurveyResponseConverter.getInstance().toJSON(result.getResponses().get(i))));
            }

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("users", jsonUserBuilder);
            jsonBuilder.add("responses", jsonResponseBuilder);
            jsonBuilder.add("success", true);
            response = jsonBuilder.build().toString();
        } else {
            return APIGatewayResponse.error(INTERNAL_SERVER_ERROR, result.getErrorMessage());
        }

        return APIGatewayResponse.builder()
                .setStatusCode(OK)
                .setJSONBody(response)
                .build();
    }

    /**
     * @return the desired token identifier of the client
     * or null if no desired token identifier is known/specified.
     */
    @Override
    public String getSearchUserID() {
        return userID;
    }

    /**
     * @return the desired {@link Map} of {@link SurveyQuestion} to {@link String} answer
     * for setting query constraint.
     * or null if no desired question is known/specified
     */
    @Override
    public Set<String> getConstraints() {
        return constraint;
    }

    /**
     * @return whether to rank list of providers based on user survey data
     */
    @Override
    public boolean getIsRanking() {
        return isRanking;
    }
}

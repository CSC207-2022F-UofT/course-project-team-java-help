package com.javahelp.backend.endpoint.search;

import static com.javahelp.backend.endpoint.APIGatewayResponse.BAD_REQUEST;
import static com.javahelp.backend.endpoint.APIGatewayResponse.OK;

import com.amazonaws.HttpMethod;
import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.endpoint.APIGatewayResponse;
import com.javahelp.backend.endpoint.HTTPHandler;
import com.javahelp.backend.search.ISearchInput;
import com.javahelp.backend.search.SearchInteractor;
import com.javahelp.backend.search.SearchResult;
import com.javahelp.backend.search.constraint.Constraint;
import com.javahelp.backend.search.constraint.IConstraint;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.user.UserPassword;
import com.javahelp.model.util.json.SurveyResponseConverter;
import com.javahelp.model.util.json.TokenConverter;
import com.javahelp.model.util.json.UserConverter;

import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class SearchHandler extends HTTPHandler implements ISearchInput {
    private String userID;
    private Set<String> constraint = new HashSet<>();
    private boolean isRanking;

    /**
     * Gets the response to the specified request
     *
     * @param body           {@link JsonObject} request body
     * @param method         {@link HttpMethod} http method called
     * @param headers        {@link Map} of {@link String} headers to {@link String} array header values
     * @param parameters     {@link Map} of {@link String} parameters to {@link String}
     *                       array parameter values
     * @param pathParameters {@link Map} of {@link String} path parameter names to {@link String} values
     * @return response object
     */
    @Override
    public APIGatewayResponse getResponse(JsonObject body, HttpMethod method, Map<String, String[]> headers, Map<String, String[]> parameters, Map<String, String> pathParameters) {

        userID = body.getString("userID", null);
        isRanking = body.getBoolean("ranking", false);
        JsonArray filters = body.getJsonArray("filters");

        if (userID == null || filters == null) {
            return APIGatewayResponse.error(BAD_REQUEST, "Request must contain \"userID\" and \"filters\"");
        }

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

        SearchInteractor interactor = new SearchInteractor(ISurveyStore.getDefaultImplementation(),
                ISurveyResponseStore.getDefaultImplementation(),
                IUserStore.getDefaultImplementation());

        SearchResult result;
        try {
            result = interactor.search(this);
        } catch (RuntimeException e) {
            return APIGatewayResponse.error(BAD_REQUEST, "Failed search.");
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
        }
        else {
            response = Json.createObjectBuilder()
                    .add("errorMessage", result.getErrorMessage())
                    .add("success", false)
                    .build().toString();
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
    public String getUserID() { return this.userID; }

    /**
     * @return the desired {@link Map} of {@link SurveyQuestion} to {@link String} answer
     * for setting query constraint.
     * or null if no desired question is known/specified
     */
    @Override
    public Set<String> getConstraints() { return this.constraint; }

    /**
     * @return whether to rank list of providers based on user survey data
     */
    @Override
    public boolean getIsRanking() { return this.isRanking; }
}

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
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.user.UserPassword;
import com.javahelp.model.util.json.SurveyResponseConverter;
import com.javahelp.model.util.json.TokenConverter;
import com.javahelp.model.util.json.UserConverter;

import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class SearchHandler extends HTTPHandler implements ISearchInput {
    private List<Constraint> constraints;
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
        JsonObject jsonQueryMap = body.getJsonObject("filters");


        if (email == null && username == null && id == null) {
            return APIGatewayResponse.error(BAD_REQUEST, "Request must contain one of \"id\", \"email\", \"username\"");
        }

        String saltHash = body.getString("saltHash");
        try {
            password = new UserPassword(saltHash);
        } catch (RuntimeException e) {
            return APIGatewayResponse.error(BAD_REQUEST, "Invalid password saltHash, cannot parse");
        }

        stayLoggedIn = body.getBoolean("stayLoggedIn");

        SearchInteractor interactor = new SearchInteractor(ISurveyStore.getDefaultImplementation(),
                ISurveyResponseStore.getDefaultImplementation(),
                IUserStore.getDefaultImplementation());

        SearchResult result = interactor.search(this);

        String response;

        if (result.isSuccess()) {
            JsonObjectBuilder jsonUserBuilder = Json.createObjectBuilder();
            JsonObjectBuilder jsonResponseBuilder = Json.createObjectBuilder();
            for (String id : result.getUsers().keySet()) {
                jsonUserBuilder.add(id,
                        UserConverter.getInstance().toJSON(result.getUsers().get(id)));
                jsonResponseBuilder.add(id,
                        SurveyResponseConverter.getInstance().toJSON(result.getResponses().get(id)));
            }

            JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
            jsonBuilder.add("users", jsonUserBuilder);
            jsonBuilder.add("responses", jsonResponseBuilder);
            response = jsonBuilder.build().toString();
        } else {
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
     * @return the desired {@link Map} of {@link SurveyQuestion} to {@link String} answer
     * for setting query constraint.
     * or null if no desired question is known/specified
     */
    @Override
    public List<Constraint> getConstraints() { return this.constraints; }

    /**
     * @return whether to rank list of providers based on user survey data
     */
    @Override
    public boolean getIsRanking() { return this.isRanking; }
}

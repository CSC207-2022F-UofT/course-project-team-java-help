package com.javahelp.frontend.gateway;

import com.javahelp.frontend.domain.user.search.ISearchDataAccess;
import com.javahelp.frontend.domain.user.search.SearchResult;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;
import com.javahelp.model.util.json.SurveyResponseConverter;
import com.javahelp.model.util.json.UserConverter;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * Data access for searching for providers
 */
public class LambdaSearchDataAccess extends RESTAPITokenGateway<SearchResult> implements ISearchDataAccess {

    private static final String ENDPOINT = "https://gwkvm1k2j5.execute-api.us-east-1.amazonaws.com/providers/search";

    private static java.net.URI URI = null;

    static {
        try {
            URI = new URI(ENDPOINT);
        } catch (URISyntaxException ignored) {
            // ignore this exception, fine since the final string passed is a valid URI
        }
    }

    /**
     * Creates a new {@link LambdaSearchDataAccess}
     * @param provider the {@link IAuthInformationProvider} to use for this {@link LambdaSearchDataAccess}
     */
    public LambdaSearchDataAccess(IAuthInformationProvider provider) {
        super(provider);
    }

    @Override
    public Future<SearchResult> search(String userID, Set<String> filters, boolean isRanking, FutureCallback<SearchResult> callback) {
        JsonObjectBuilder bodyBuilder = Json.createObjectBuilder();

        bodyBuilder.add("userID", userID);
        bodyBuilder.add("ranking", isRanking);

        JsonArrayBuilder filterBodyBuilder = Json.createArrayBuilder();
        int i = 0;
        for (String filter : filters) {
            filterBodyBuilder.add(Json.createObjectBuilder()
                    .add(String.format("filter_%s", i), filter));
            i = i + 1;
        }
        bodyBuilder.add("filters", filterBodyBuilder);

        SimpleHttpRequest request = SimpleHttpRequest.create("POST", URI);
        request.setBody(bodyBuilder.build().toString(), ContentType.APPLICATION_JSON);
        request.setHeader("Content-Type", "application/json");

        request.setConfig(RequestConfig.custom()
                .setConnectionRequestTimeout(30000, TimeUnit.MILLISECONDS)
                .setResponseTimeout(30000, TimeUnit.MILLISECONDS)
                .setConnectTimeout(30000, TimeUnit.MILLISECONDS)
                .build());

        FutureCallback<RESTAPIGatewayResponse<SearchResult>> passedCallback = callback == null ? null : new FutureCallback<RESTAPIGatewayResponse<SearchResult>>() {
            @Override
            public void completed(RESTAPIGatewayResponse<SearchResult> result) {
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

        Future<RESTAPIGatewayResponse<SearchResult>> response = getResponse(request, passedCallback);

        return new Future<SearchResult>() {
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
            public SearchResult get() throws ExecutionException, InterruptedException {
                return response.get().get();
            }

            @Override
            public SearchResult get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                return response.get(l, timeUnit).get();
            }
        };
    }

    @Override
    protected RESTAPIGatewayResponse<SearchResult> fromInternal(InternalRESTGatewayResponse response) {
        SurveyResponseConverter srConverter = SurveyResponseConverter.getInstance();
        UserConverter userConverter = UserConverter.getInstance();

        if (response.shouldHaveBody() && response.isSuccessfullyParsed()) {
            List<User> users = new ArrayList<>();
            List<SurveyResponse> surveyResponses = new ArrayList<>();

            JsonObject json = response.getBody();
            if (json.containsKey("users" ) && json.containsKey("responses")) {
                JsonArray usersArray = json.getJsonArray("users");
                JsonArray srArray = json.getJsonArray("responses");

                for (int i = 0; i < usersArray.size(); i++) {
                    JsonObject userObj = usersArray.getJsonObject(i);
                    JsonObject srObj = srArray.getJsonObject(i);
                    User user = userConverter.fromJSON(userObj.getJsonObject(String.format("user_%s", i)));
                    SurveyResponse sr = srConverter.fromJSON(srObj.getJsonObject(String.format("response_%s", i)));

                    if (user == null || sr == null) {
                        return new RESTAPIGatewayResponse<>("Unable to parse user or token");
                    }

                    users.add(user);
                    surveyResponses.add(sr);
                }
                return new RESTAPIGatewayResponse<>(new SearchResult(users, surveyResponses));
            }
            else if (response.getResponseCode() == 200) {
                return new RESTAPIGatewayResponse<>(new SearchResult("Authentication failed"));
            }
            else if (json.containsKey("errorMessage")) {
                return new RESTAPIGatewayResponse<>(json.getString("errorMessage"));
            }
            else {
                return new RESTAPIGatewayResponse<>("JSON missing relevant fields");
            }
        }
        return new RESTAPIGatewayResponse<>("Response could not be parsed into JSON");
    }
}

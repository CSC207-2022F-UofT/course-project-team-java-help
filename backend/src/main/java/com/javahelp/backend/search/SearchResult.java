package com.javahelp.backend.search;

import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.List;
import java.util.Map;

public class SearchResult {
    private final Map<String, User> users;
    private final Map<String, SurveyResponse> responses;
    private final String errorMessage;

    /**
     * Creates a new successful {@link SearchResult} with the specified {@link List} of {@link User}s
     * and {@link List} of {@link SurveyResponse}s.
     *
     * @param users {@link Map} of {@link String} id to {@link User} to bundle
     * @param responses {@link List} of {@link String} id to {@link SurveyResponse} to bundle
     */
    SearchResult(Map<String, User> users, Map<String, SurveyResponse> responses) {
        this.users = users;
        this.responses = responses;
        this.errorMessage = null;
    }

    /**
     * Creates a new failed {@link SearchResult} with the specified error message
     *
     * @param errorMessage {@link String} error message to include
     */
    SearchResult(String errorMessage) {
        this.users = null;
        this.responses = null;
        this.errorMessage = errorMessage;
    }

    /**
     * @return the {@link Map} of {@link String} id to {@link User}s.
     */
    public Map<String, User> getUsers() { return this.users; }

    /**
     * @return the {@link Map} of {@link String} id to {@link SurveyResponse}s
     */
    public Map<String, SurveyResponse> getResponses() { return this.responses; }

    /**
     * @return {@link String} error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return whether this search was successful
     */
    public boolean isSuccess() {
        return errorMessage == null;
    }
}

package com.javahelp.frontend.domain.search;

import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.List;
import java.util.Map;

public class SearchResult {
    private final List<User> users;
    private final List<SurveyResponse> responses;
    private final String errorMessage;

    /**
     * Creates a new successful {@link SearchResult} with the specified {@link List} of {@link User}s
     * and {@link List} of {@link SurveyResponse}s.
     *
     * @param users {@link Map} of {@link String} id to {@link User} to bundle
     * @param responses {@link List} of {@link String} id to {@link SurveyResponse} to bundle
     */
    public SearchResult(List<User> users, List<SurveyResponse> responses) {
        this.users = users;
        this.responses = responses;
        this.errorMessage = null;
    }

    /**
     * Creates a new failed {@link SearchResult} with the specified error message
     *
     * @param errorMessage {@link String} error message to include
     */
    public SearchResult(String errorMessage) {
        this.users = null;
        this.responses = null;
        this.errorMessage = errorMessage;
    }

    /**
     * @return the {@link Map} of {@link String} id to {@link User}s.
     */
    public List<User> getUsers() { return this.users; }

    /**
     * @return the {@link Map} of {@link String} id to {@link SurveyResponse}s
     */
    public List<SurveyResponse> getResponses() { return this.responses; }

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

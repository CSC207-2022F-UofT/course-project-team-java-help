package com.javahelp.frontend.domain.user.search;


import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.User;

import java.util.List;

public interface ISearchOutput {
    /**
     * Called when the successfully retrieved list of providers from database
     *
     * @param users {@link List} of {@link User} providers that match the client's requests.
     * @param responses {@link List} of {@link SurveyResponse}s corresponding to the {@link List}
     *                              of providers.
     */
    void success(List<User> users, List<SurveyResponse> responses);

    /**
     * Called when the list of providers are not retrieved from the database.
     */
    void failure();

    /**
     * Called when something goes wrong before authentication
     *
     * @param errorMessage {@link String} error message received
     */
    void error(String errorMessage);

    /**
     * Abort the login attempt
     */
    void abort();

}

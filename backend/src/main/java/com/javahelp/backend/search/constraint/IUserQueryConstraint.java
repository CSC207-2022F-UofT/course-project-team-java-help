package com.javahelp.backend.search.constraint;

import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IUserQueryConstraint {
    /**
     *
     * @param constraints {@link List} of {@link Constraint}s to be applied for query.
     * @return {@link Set} of Users that satisfy the list of {@link Constraint}s.
     */
    Map<String, User> getProvidersWithConstraints(List<Constraint> constraints);

    /**
     *
     * @param constraints{ @link List} of {@link Constraint}s to be applied for query.
     * @return {@link Map} of {@link String} ID of users and their corresponding
     * {@link SurveyResponse}s.
     */
    Map<String, SurveyResponse> getResponsesWithConstraints(List<Constraint> constraints);
}

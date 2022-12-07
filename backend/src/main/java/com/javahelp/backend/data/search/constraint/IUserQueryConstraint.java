package com.javahelp.backend.data.search.constraint;

import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.Map;
import java.util.Set;

public interface IUserQueryConstraint {
    /**
     *
     * @param constraint {@link Constraint} to be applied for query.
     * @return {@link Set} of Users that satisfy the list of {@link Constraint}s.
     */
    Map<String, User> getProvidersWithConstraints(IConstraint constraint);

    /**
     *
     * @param constraint {@link Constraint} to be applied for query.
     * @return {@link Map} of {@link String} ID of users and their corresponding
     * {@link SurveyResponse}s.
     */
    Map<String, SurveyResponse> getResponsesWithConstraints(IConstraint constraint);
}

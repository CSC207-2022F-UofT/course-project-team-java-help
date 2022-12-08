package com.javahelp.backend.data.search.constraint;

import java.util.Set;

/**
 * Interface for constraints on searches for {@link com.javahelp.model.survey.SurveyResponse}s
 */
public interface IConstraint {

    /**
     * Sets the constraint on this {@link IConstraint}
     *
     * @param constraint {@link String}
     */
    void addConstraint(String constraint);

    /**
     * @return a {@link Set} of all the constraint {@link String}s constraints
     */
    Set<String> getConstraints();

    /**
     * @return the size of this {@link IConstraint}
     */
    int size();
}

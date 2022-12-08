package com.javahelp.backend.data.search.constraint;

import java.util.HashSet;
import java.util.Set;

/**
 * Constraint on a query for {@link com.javahelp.model.survey.SurveyResponse}s
 */
public class Constraint implements IConstraint {

    private final Set<String> constraints = new HashSet<>();

    @Override
    public void addConstraint(String constraint) {
        constraints.add(constraint);
    }

    @Override
    public Set<String> getConstraints() {
        return constraints;
    }

    @Override
    public int size() {
        return constraints.size();
    }
}
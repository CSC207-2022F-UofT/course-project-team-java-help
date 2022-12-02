package com.javahelp.backend.search.constraint;

import java.util.Set;

public interface IConstraint {

    static IConstraint getDefaultImplementation() {
        return new Constraint();
    }

    void setConstraint(String constraint);

    Set<String> getConstraints();

    int size();
}

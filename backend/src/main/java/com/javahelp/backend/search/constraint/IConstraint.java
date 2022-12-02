package com.javahelp.backend.search.constraint;

import java.util.Set;

public interface IConstraint {

    void setConstraint(String constraint);

    Set<String> getConstraints();

    int size();
}

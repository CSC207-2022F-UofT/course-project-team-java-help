package com.javahelp.backend.data.search.constraint;

import java.util.HashSet;
import java.util.Set;

public class Constraint implements IConstraint {
    private Set<String> constraints = new HashSet<>();

    public Constraint() {}

    @Override
    public void setConstraint(String constraint) {
        this.constraints.add(constraint);
    }

    @Override
    public Set<String> getConstraints(){
        return this.constraints;
    };

    @Override
    public int size() { return this.constraints.size(); }
}
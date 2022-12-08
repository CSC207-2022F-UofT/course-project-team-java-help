package com.javahelp.backend.data.search.constraint;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MultiOptionConstraint implements IConstraint {
    private Set<String> constraints = new HashSet<>();

    public MultiOptionConstraint() {}

    public void setMultiConstraint(List<String> constraintList) {
        for (String constraint : constraintList) {
            addConstraint(constraint);
        }
    }

    @Override
    public void addConstraint(String constraint) {
        this.constraints.add(constraint);
    }

    @Override
    public Set<String> getConstraints(){
        return this.constraints;
    }

    @Override
    public int size() { return this.constraints.size(); }
}
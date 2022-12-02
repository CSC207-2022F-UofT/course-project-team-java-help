package com.javahelp.backend.search.constraint;

import java.util.List;

public class MultiOptionConstraint extends Constraint{
    public MultiOptionConstraint(String question) {
        super(question);
    }

    public void setMultiConstraint(List<String> queryList) {
        for (String query : queryList) {
            super.setConstraint(query);
        }
    }
}
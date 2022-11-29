package com.javahelp.backend.query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class Constraint {
    private String question;
    private Set<String> queryList = new HashSet<>();

    public Constraint(String question) {
        this.question = question;
    }

    public void setConstraint(String query) {
        this.queryList.add(query);
    }

    public HashMap<String, Set<String>> getConstraint(){
        HashMap<String, Set<String>> constraint = new HashMap<>();
        constraint.put(this.question, this.queryList);
        return constraint;
    };
}
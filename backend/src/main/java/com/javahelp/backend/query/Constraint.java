package com.javahelp.backend.query;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Constraint {
    private String question;
    private Integer query;

    public Constraint(String question, Integer query) {
        this.question = question;
        this.query = query;
    }

    public HashMap<String, ArrayList<Integer>> getConstraint() {
        ArrayList<Integer> answerList = new ArrayList<>();
        answerList.add(this.query);
        HashMap<String, ArrayList<Integer>> constraint = new HashMap<>();
        constraint.put(question, answerList);
        return constraint;
    }
}

package com.javahelp.backend.query;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Constraint implements IConstraint{
    private String question;
    private ArrayList<String> queryList = new ArrayList<>();

    public Constraint(String question) {
        this.question = question;
    }

    public void setConstraint(String query) {
        this.queryList.add(query);
    }

    public HashMap<String, ArrayList<String>> getConstraint(){
        HashMap<String, ArrayList<String>> constraint = new HashMap<>();
        constraint.put(this.question, this.queryList);
        return constraint;
    };
}

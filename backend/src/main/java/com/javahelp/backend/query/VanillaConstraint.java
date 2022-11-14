package com.javahelp.backend.query;

import java.util.ArrayList;
import java.util.HashMap;

public class VanillaConstraint extends Constraint{
    public VanillaConstraint(String question, Integer query) {
        super(question, query);
    }

    public HashMap<String, ArrayList<Integer>> getConstraint() {
        return super.getConstraint();
    }
}

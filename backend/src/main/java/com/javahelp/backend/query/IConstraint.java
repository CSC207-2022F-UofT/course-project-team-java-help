package com.javahelp.backend.query;

import java.util.ArrayList;
import java.util.Map;

public interface IConstraint {
    /**
     * @return a map with keys being the questions and the values being the answers to the questions.
     */
    Map<String, ArrayList<String>> getConstraint();
}

package com.javahelp.backend.query;

import java.util.ArrayList;
import java.util.HashMap;

public class RangeConstraint extends Constraint{
    public RangeConstraint(String question) {
        super(question);
    }

    public void setRangeConstraint(int rangeBegin, int rangeEnd) {
        for (int i = rangeBegin; i <= rangeEnd; i++) {
            super.setConstraint(String.valueOf(i));
        }
    }
}

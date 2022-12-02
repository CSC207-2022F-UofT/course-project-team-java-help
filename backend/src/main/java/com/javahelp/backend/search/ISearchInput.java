package com.javahelp.backend.search;

import com.javahelp.backend.search.constraint.Constraint;
import com.javahelp.model.survey.SurveyQuestion;

import java.util.List;
import java.util.Map;

public interface ISearchInput {

    /**
     * @return the desired {@link Map} of {@link SurveyQuestion} to {@link String} answer
     * for setting query constraint.
     * or null if no desired question is known/specified
     */
    List<Constraint> getConstraints();

    /**
     * @return whether to rank list of providers based on user survey data
     */
    boolean getIsRanking();

}

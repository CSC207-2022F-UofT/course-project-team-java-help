package com.javahelp.backend.search;

import com.javahelp.backend.search.constraint.IConstraint;
import com.javahelp.model.survey.SurveyQuestion;

import java.util.Map;

public interface ISearchInput {

    /**
     *
     * @return the desired ID of the client
     * or null if no desired token identifier is known/specified.
     */
    String getUserID();
    /**
     * @return the desired {@link Map} of {@link SurveyQuestion} to {@link String} answer
     * for setting query constraint.
     * or null if no desired list of constraint is known/specified
     */
    IConstraint getConstraint();

    /**
     * @return whether to rank list of providers based on user survey data
     */
    boolean getIsRanking();

}

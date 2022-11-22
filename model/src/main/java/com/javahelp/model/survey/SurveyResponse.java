package com.javahelp.model.survey;

import java.util.Map;

/**
 * A filled out {@link Survey}
 */
public class SurveyResponse {

    private Survey survey;

    private Map<SurveyQuestion, SurveyQuestionResponse> responses;

    private String id;

    /**
     * Creates a new {@link SurveyResponse}
     *
     * @param id        {@link String} id for this {@link SurveyResponse}
     * @param survey    {@link Survey} to use for this {@link SurveyResponse}
     * @param responses {@link Map} mapping {@link SurveyQuestion}s on the {@link Survey}
     * to {@link SurveyQuestionResponse}s
     */
    public SurveyResponse(String id, Survey survey, Map<SurveyQuestion,
        SurveyQuestionResponse> responses) {
        this.id = id;
        this.survey = survey;
        this.responses = responses;
    }

    /**
     *
     * @return the {@link Survey} for this {@link SurveyResponse}
     */
    public Survey getSurvey() {
        return survey;
    }

    /**
     *
     * @param question the {@link SurveyQuestion} to get the response for
     * @return {@link SurveyQuestionResponse} for the provided {@link SurveyQuestion}
     */
    public SurveyQuestionResponse getResponse(SurveyQuestion question) {
        return responses.getOrDefault(question, new SurveyQuestionResponse(question, -1));
    }

    /**
     *
     * @param i the index of the {@link SurveyQuestion} to get the response for
     * @return the ith {@link SurveyQuestion}'s {@link SurveyQuestionResponse}
     */
    public SurveyQuestionResponse getResponse(int i) {
        if (i < 0 || i >= survey.size()) {
            throw new IndexOutOfBoundsException("Survey does not have enough questions");
        }
        return getResponse(survey.get(i));
    }

    /**
     *
     * @return the number of {@link SurveyQuestion}s the {@link Survey} this is based on has
     */
    public int size() {
        return survey.size();
    }

    /**
     *
     * @return the unique {@link String} id for this {@link SurveyResponse}
     */
    public String getID() {
        return id;
    }

    /**
     *
     * @param id {@link String} new id for this {@link SurveyResponse}
     */
    public void setID(String id) { this.id = id; }
}

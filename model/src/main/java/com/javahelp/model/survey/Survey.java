package com.javahelp.model.survey;

import java.util.List;

/**
 * Representation of a survey.
 *
 * Composed of {@link SurveyQuestion}s
 */
public class Survey {

    private String id;

    private String name;

    private List<SurveyQuestion> questions;

    /**
     * Creates a new {@link Survey}
     * @param id {@link String} id of this {@link Survey}
     * @param name {@link String} human-readable name for this {@link Survey}
     * @param questions {@link List} of {@link SurveyQuestion}s
     */
    public Survey(String id, String name, List<SurveyQuestion> questions) {
        this.id = id;
        this.name = name;
        this.questions = questions;
    }

    /**
     *
     * @return unique {@link String} id for this {@link Survey}
     */
    public String getID() {
        return id;
    }

    /**
     *
     * @return human-readable name of this {@link Survey} as a {@link String}
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param i index of the {@link SurveyQuestion} to get
     * @return the ith {@link SurveyQuestion} in this {@link Survey}
     */
    public SurveyQuestion get(int i) {
        return questions.get(i);
    }

    /**
     *
     * @return {@link Iterable} of {@link SurveyQuestion}s in this {@link Survey}
     */
    public Iterable<SurveyQuestion> getQuestions() {
        return questions;
    }

    /**
     *
     * @return number of questions in this {@link Survey}
     */
    public int size() {
        return questions.size();
    }

}

package com.javahelp.model.survey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param id {@link String} of the survey.
     */
    public void setID(String id) { this.id = id; }

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
        if (i < 0 || i >= questions.size()) {
            throw new IndexOutOfBoundsException("Survey does not have enough questions");
        }
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

    /**
     * Answer this {@link Survey}
     * @param id {@link String} id for the {@link SurveyResponse}
     * @param answers int array with answer indices for the {@link Survey}
     * @return {@link SurveyResponse} corresponding to filled in answers
     */
    public SurveyResponse answer(String id, int... answers) {
        if (answers.length != questions.size()) {
            throw new IllegalArgumentException("Must provide exactly one answer for each question");
        }
        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();
        for (int i = 0; i < answers.length; i++) {
            SurveyQuestion question = questions.get(i);
            int responseIndex = answers[i];
            SurveyQuestionResponse response = question.answer(responseIndex);
            map.put(question, response);
        }
        return new SurveyResponse(id, this, map);
    }

}

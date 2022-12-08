package com.javahelp.model.survey;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Representation of a single question on an {@link Survey}
 */
public class SurveyQuestion {

    private final String question;

    private final List<String> answers;

    private final List<Set<String>> attributes;

    /**
     * Creates a new {@link SurveyQuestion}
     * @param question {@link String} question to ask
     * @param answers {@link List} of {@link String}s with potential answers
     */
    public SurveyQuestion(String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
        this.attributes = new ArrayList<>();

        for (int i = 0; i < answers.size(); i++) {
            Set<String> attr = new HashSet<>();
            attr.add("null");
            this.attributes.add(attr);
        }
    }

    /**
     *
     * @param i the index of the answer to update
     * @param attr {@link String} attribute to add to the answer
     */
    public void setAnswerAttribute(int i, String attr) {
        if (i >= answers.size()) {
            throw new IndexOutOfBoundsException("SurveyQuestion does not have enough responses");
        }
        this.attributes.get(i).remove("null");
        this.attributes.get(i).add(attr);
    }

    /**
     *
     * @param i the index of the answer to update
     * @param attrSet {@link Set} of {@link String} attributes to add to the answer
     */
    public void setAnswerAttribute(int i, Set<String> attrSet) {
        if (i >= answers.size()) {
            throw new IndexOutOfBoundsException("SurveyQuestion does not have enough responses");
        }
        this.attributes.get(i).remove("null");
        this.attributes.get(i).addAll(attrSet);
    }

    /**
     * @param i the index of the answer to get
     * @return the ith {@link Set} of {@link String} attributes for this question
     */
    public Set<String> getAnswerAttribute(int i) {
        if (i < 0 || i >= answers.size()) {
            throw new IndexOutOfBoundsException("SurveyQuestion does not have enough responses");
        }
        return this.attributes.get(i);
    }

    /**
     *
     * @param i the index of the answer to get
     * @return the ith {@link String} answer for this question
     */
    public String getAnswer(int i) {
        if (i < 0 || i >= answers.size()) {
            throw new IndexOutOfBoundsException("SurveyQuestion does not have enough responses");
        }
        return answers.get(i);
    }

    /**
     *
     * @return {@link Iterable} of potential {@link String} answers to this question
     */
    public Iterable<String> getAnswers() {
        return answers;
    }

    /**
     *
     * @return {@link String} question
     */
    public String getQuestion() {
        return question;
    }

    /**
     *
     * @return the number of responses to this question
     */
    public int getNumberOfResponses() {
        return answers.size();
    }

    /**
     * Answers this {@link SurveyQuestion}
     * @param i the index of the answer to select
     * @return {@link SurveyQuestionResponse} containing this {@link SurveyQuestion}
     * and the selected response
     */
    public SurveyQuestionResponse answer(int i) {
        if (i < 0 || i >= answers.size()) {
            throw new IndexOutOfBoundsException("SurveyQuestion does not have enough responses");
        }
        return new SurveyQuestionResponse(this, i);
    }
}

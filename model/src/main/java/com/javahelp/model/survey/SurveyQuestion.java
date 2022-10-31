package com.javahelp.model.survey;

import java.util.List;

/**
 * Representation of a single question on an {@link Survey}
 */
public class SurveyQuestion {

    private String question;

    private List<String> answers;

    /**
     * Creates a new {@link SurveyQuestion}
     * @param question {@link String} question to ask
     * @param answers {@link List} of {@link String}s with potential answers
     */
    public SurveyQuestion(String question, List<String> answers) {
        this.question = question;
        this.answers = answers;
    }

    /**
     *
     * @param i the index of the answer to get
     * @return the ith {@link String} answer for this question
     */
    public String getAnswer(int i) {
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
}
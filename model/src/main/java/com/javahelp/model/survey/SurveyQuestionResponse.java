package com.javahelp.model.survey;

/**
 * Representation of a {@link SurveyQuestion} that has been responded to
 */
public class SurveyQuestionResponse {

    private SurveyQuestion question;

    private int responseNumber;

    /**
     * Creates a new {@link SurveyQuestionResponse}
     * @param question the {@link SurveyQuestion} that has been responded to
     * @param responseNumber the index of the response that was selected
     */
    public SurveyQuestionResponse(SurveyQuestion question, int responseNumber) {
        this.question = question;
        this.responseNumber = responseNumber;
        if (this.responseNumber < 0 || this.responseNumber >= this.question.getNumberOfResponses()) {
            throw new IllegalStateException("SurveyQuestionResponse cannot have an invalid response index");
        }
    }

    /**
     *
     * @return {@link SurveyQuestion} for this {@link SurveyQuestionResponse}
     */
    public SurveyQuestion getQuestion() {
        return question;
    }

    /**
     *
     * @return index of the response to the {@link SurveyQuestion}
     */
    public int getResponseNumber() {
        return responseNumber;
    }

    /**
     *
     * @return {@link String} response to the {@link SurveyQuestion}
     */
    public String getResponse() {
        return question.getAnswer(responseNumber);
    }

}

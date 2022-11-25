package com.javahelp.frontend.gateway;

import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.Survey;

public interface ISurveyInformationProvider {

    /**
     * @return all of the {@link SurveyQuestion}s in the specified {@link Survey}
     */
    Iterable<SurveyQuestion> getQuestions();

    /**
     * @return the question of the specified {@link SurveyQuestion}
     */
    String getQuestion();

    /**
     * @return the number of responses in the specified {@link SurveyQuestion}
     */
    int getNumberOfResponses();

    /**
     * @return the answers for the specified {@link SurveyQuestion}
     */
    Iterable<String> getAnswers();

}

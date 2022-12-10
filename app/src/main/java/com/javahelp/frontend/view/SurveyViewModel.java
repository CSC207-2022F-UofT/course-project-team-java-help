package com.javahelp.frontend.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AndroidViewModel} for {@link SurveyActivity}
 */
public class SurveyViewModel extends AndroidViewModel {

    private Survey survey;
    private final List<LiveData<SurveyQuestionResponse>> surveyQuestionsLiveData = new ArrayList<>();

    /**
     * Creates a new {@link SurveyViewModel}
     *
     * @param application {@link Application} for this {@link SurveyViewModel} to use
     */
    public SurveyViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * @return {@link LiveData} for survey questions
     */
    public List<LiveData<SurveyQuestionResponse>> getSurveyQuestions() {
        return surveyQuestionsLiveData;
    }

    /**
     * Loads the {@link Survey} with the specified id
     *
     * @param surveyId {@link String} id of the {@link Survey} to load
     */
    public void loadSurvey(String surveyId) {
        // should use some kind of survey gateway to load this survey from the backend
        // the callback with the gateway should then call set survey with the received survey
        // some kind of error should be shown on the corresponding view if no survey is loaded
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Sets the {@link Survey} to use
     *
     * @param s {@link Survey} to use
     */
    private void setSurvey(Survey s) {
        survey = s;
        surveyQuestionsLiveData.clear();
        for (SurveyQuestion q : s.getQuestions()) {
            surveyQuestionsLiveData.add(new MutableLiveData<>(null));
        }
    }

    /**
     * @return the {@link Survey} this {@link SurveyViewModel} is displaying
     */
    public Survey getSurvey() {
        return survey;
    }

    /**
     * Sets the answer to a question
     *
     * @param question index of question to set the answer for
     * @param answer   index of the answer to assign
     */
    public void setResponse(int question, int answer) {
        if (question < 0 || question >= survey.size()) {
            throw new IllegalArgumentException("Insufficient questions in survey");
        }

        SurveyQuestion surveyQuestion = survey.get(question);

        if (answer < 0 || answer >= surveyQuestion.getNumberOfResponses()) {
            throw new IllegalArgumentException("Insufficient answers for question");
        }

        SurveyQuestionResponse surveyQuestionResponse = surveyQuestion.answer(answer);

        ((MutableLiveData) surveyQuestionsLiveData.get(question)).postValue(surveyQuestionResponse);
    }

}

package com.javahelp.frontend.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.javahelp.frontend.gateway.ISurveyInformationProvider;
import com.javahelp.model.survey.SurveyQuestion;

/**
 * {@link AndroidViewModel} for {@link SurveyActivity}
 */
public class SurveyViewModel extends AndroidViewModel implements ISurveyInformationProvider {

    public SurveyViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public String getQuestion() {
        return "";
    }

    @Override
    public int getNumberOfResponses() {
        return 0;
    }

    @Override
    public Iterable<String> getAnswers() {
        return null;
    }

    @Override
    public Iterable<SurveyQuestion> getQuestions() {
        return null;
    }

}

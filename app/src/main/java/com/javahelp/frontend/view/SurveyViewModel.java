package com.javahelp.frontend.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.javahelp.model.survey.SurveyQuestion;

import java.util.ArrayList;

/**
 * {@link AndroidViewModel} for {@link SurveyActivity}
 */
public class SurveyViewModel extends ViewModel {
    MutableLiveData<ArrayList<SurveyQuestion>> surveyQuestionsLiveData;
    ArrayList<SurveyQuestion> surveyQuestions;

    public SurveyViewModel() {
        surveyQuestionsLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<SurveyQuestion>> getSurveyQuestionsLiveData(){
        return surveyQuestionsLiveData;
    }

    public void init() {
        populateList();
        surveyQuestionsLiveData.setValue(surveyQuestions);
    }

    public void populateList() {
        //populate surveyQuestions list
    }

}

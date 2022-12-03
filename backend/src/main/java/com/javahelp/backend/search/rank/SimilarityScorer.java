package com.javahelp.backend.search.rank;

import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;

import java.util.Map;
import java.util.Objects;

public class SimilarityScorer implements ISimilarityScorer{
    public SimilarityScorer() {}

    @Override
    public Float getSimilarityScore(SurveyResponse sr1, SurveyResponse sr2) {
        if (!Objects.equals(sr1.getSurvey().getID(), sr2.getSurvey().getID())) {
            return 0.0F;
        }
        float match = 0;
        int total = sr1.getSurvey().size();
        for (SurveyQuestion question : sr1.getSurvey().getQuestions()) {
            SurveyQuestionResponse r1 = sr1.getResponse(question);
            SurveyQuestionResponse r2 = sr2.getResponse(question);
            if (r1.getResponseNumber() == r2.getResponseNumber()) {
                match = match + 1;
            }
        }
        return match / total;
    }
}
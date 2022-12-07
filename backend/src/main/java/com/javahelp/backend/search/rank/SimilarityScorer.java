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
        float match = 0;
        int total = sr1.getAttributes().size() + sr2.getAttributes().size();
        for (String attr : sr1.getAttributes()) {
            if (sr2.getAttributes().contains(attr)) {
                match = match + 1;
            }
        }
        return match / total;
    }
}
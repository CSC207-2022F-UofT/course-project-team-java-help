package com.javahelp.backend.search.rank;

import com.amazonaws.regions.Regions;
import com.javahelp.backend.data.DynamoDBUserStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.survey.SurveyResponse;

public interface ISimilarityScorer {
    /**
     * @return Default implementation of {@link ISimilarityScorer}
     */
    static ISimilarityScorer getDefaultImplementation() {
        return new SimilarityScorer();
    }
    Float getSimilarityScore(SurveyResponse sr1, SurveyResponse sr2);
}
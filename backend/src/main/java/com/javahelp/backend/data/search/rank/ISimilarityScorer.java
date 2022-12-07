package com.javahelp.backend.data.search.rank;

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
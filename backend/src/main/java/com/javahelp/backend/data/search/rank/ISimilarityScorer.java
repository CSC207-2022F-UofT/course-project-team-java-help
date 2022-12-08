package com.javahelp.backend.data.search.rank;

import com.javahelp.model.survey.SurveyResponse;

public interface ISimilarityScorer {
    /**
     * @return Default implementation of {@link ISimilarityScorer}
     */
    static ISimilarityScorer getDefaultImplementation() {
        return new SimilarityScorer();
    }

    /**
     * Gets the similarity score between two responses
     * @param sr1 the first {@link SurveyResponse}
     * @param sr2 the second {@link SurveyResponse}
     * @return the similarity between two {@link SurveyResponse}s
     */
    Float getSimilarityScore(SurveyResponse sr1, SurveyResponse sr2);
}
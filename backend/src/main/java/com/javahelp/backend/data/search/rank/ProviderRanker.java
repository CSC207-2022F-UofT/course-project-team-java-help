package com.javahelp.backend.data.search.rank;

import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;

/**
 * Given the SimilarityScorer and a main user, this class ranks a list of Users based on their
 * similarities with the main user, in descending order.
 */
public class ProviderRanker implements IProviderRanker{
    private ISimilarityScorer similarityScorer;

    public ProviderRanker(ISimilarityScorer similarityScorer) {
        this.similarityScorer = similarityScorer;
    }

    @Override
    public List<User> rank(SurveyResponse responseMain, Map<User, SurveyResponse> responses) {
        Map<User, Float> usersWithScores = getUsersWithScores(responseMain, responses);
        List<Float> sortedScoreList = getSortedScoreList(usersWithScores);
        List<User> sortedUserList = new ArrayList<>();

        for (Float score : sortedScoreList) {
            for (Map.Entry<User, Float> entry : usersWithScores.entrySet()) {
                if (entry.getValue().equals(score)) {
                    sortedUserList.add(entry.getKey());
                }
            }
        }
        return sortedUserList;
    }

    @Override
    public Map<User, Float> getUsersWithScores(SurveyResponse mainResponse,
                                               Map<User, SurveyResponse> responses) {
        Map<User, Float> usersWithScores = new HashMap<>();
        for (User user : responses.keySet()) {
            SurveyResponse sr = responses.get(user);
            float similarity = this.similarityScorer.getSimilarityScore(mainResponse, sr);
            usersWithScores.put(user, similarity);
        }
        return usersWithScores;
    }

    private List<Float> getSortedScoreList(Map<User, Float> userScores) {
        Set<Float> scoreSet = new HashSet<>();
        for (Map.Entry<User, Float> entry : userScores.entrySet()) {
            scoreSet.add(entry.getValue());
        }
        List<Float> scoreList = new ArrayList<>();
        for (Float score : scoreSet) {
            scoreList.add(score);
        }
        Collections.sort(scoreList, Collections.reverseOrder());
        return scoreList;
    }
}
package com.javahelp.backend.rank;

import com.javahelp.model.user.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;

/**
 * Given the SimilarityScorer and a main user, this class ranks a list of Users based on their
 * similarities with the main user, in descending order.
 */
public class ProviderRanker {
    private User userMain;
    private SimilarityScorer similarityScorer;

    public ProviderRanker(User userMain, SimilarityScorer similarityScorer) {
        this.userMain = userMain;
        this.similarityScorer = similarityScorer;
    }

    public List<User> rank(Set<User> userList) {
        Map<User, Float> usersWithScores = this.similarityScorer.getUsersWithScores(this.userMain, userList);
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

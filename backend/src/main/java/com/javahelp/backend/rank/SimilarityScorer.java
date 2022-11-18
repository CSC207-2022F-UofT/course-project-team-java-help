package com.javahelp.backend.rank;

import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Objects;

public abstract class SimilarityScorer implements ISimilarityScorer {
    public SimilarityScorer() {}

    public Map<User, Float> getUsersWithScores(User userMain, Set<User> userList) {
        Map<User, Float> usersWithScores = new HashMap<>();
        for (User user : userList) {
            float similarity = getSimilarityScore(userMain, user);
            usersWithScores.put(user, similarity);
        }
        return usersWithScores;
    }

    public Float getSimilarityScore(User user1, User user2) {
        Map<String, String> attributeMap1 = user1.getUserInfo().getAllAttribute();
        UserInfo attributeMap2 = user2.getUserInfo();
        float match = 0;
        int total = attributeMap1.size();
        for (String key : attributeMap1.keySet()) {
            if (Objects.equals(attributeMap1.get(key), attributeMap2.getSingleAttribute(key))) {
                match = match + 1;
            }
        }
        return match / total;
    }
}

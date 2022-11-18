package com.javahelp.backend.rank;

import com.javahelp.model.user.User;

import java.util.Set;
import java.util.Map;

public interface ISimilarityScorer {
    Map<User, Float> getUsersWithScores(User userMain, Set<User> userList);

    Float getSimilarityScore(User user1, User user2);
}

package com.javahelp.backend.search.rank;

import com.javahelp.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IProviderRanker {
    List<User> rank(Set<User> userList);

    Map<User, Float> getUsersWithScores(User userMain, Set<User> userList);

    List<Float> getSortedScoreList(Map<User, Float> userScores);
}

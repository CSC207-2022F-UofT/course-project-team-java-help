package com.javahelp.backend.domain.search;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.data.search.constraint.Constraint;
import com.javahelp.backend.data.search.constraint.IConstraint;
import com.javahelp.backend.data.search.constraint.SurveyQuerier;
import com.javahelp.backend.data.search.rank.IProviderRanker;
import com.javahelp.backend.data.search.rank.ISimilarityScorer;
import com.javahelp.backend.data.search.rank.ProviderRanker;
import com.javahelp.backend.data.search.rank.SimilarityScorer;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interactor for looking up {@link User}s
 */
public class SearchInteractor {

    private final ISurveyResponseStore responseStore;
    private final IUserStore userStore;

    /**
     * Creates a new {@link SearchInteractor}
     * @param responseStore {@link ISurveyResponseStore} to use
     * @param userStore {@link IUserStore} to use
     */
    public SearchInteractor(ISurveyResponseStore responseStore,
                         IUserStore userStore) {
        this.responseStore = responseStore;
        this.userStore = userStore;
    }

    /**
     * Searches for providers relevant to a client based on input
     * @param input {@link ISearchInputBoundary} to use
     * @return the relevant {@link SearchResult}
     */
    public SearchResult search(ISearchInputBoundary input) {
        IConstraint constraint = new Constraint();
        for (String c : input.getConstraints()) {
            constraint.addConstraint(c);
        }

        SurveyQuerier userQueryConstraint = new SurveyQuerier(responseStore, userStore);
        Map<String, User> users = userQueryConstraint.getUsersByConstraint(constraint);
        Map<String, SurveyResponse> responses = userQueryConstraint.getSurveyResponses(constraint);

        List<User> userList = new ArrayList<>();
        List<SurveyResponse> responseList = new ArrayList<>();
        Map<User, SurveyResponse> userToResponseMap = new HashMap<>();
        for (String userID : users.keySet()) {
            userList.add(users.get(userID));
            responseList.add(responses.get(userID));
            userToResponseMap.put(users.get(userID), responses.get(userID));
        }

        if (input.getIsRanking() && input.getSearchUserID() != null) {
            SurveyResponse responseMain = responseStore.readByUser(input.getSearchUserID()).get(0);
            ISimilarityScorer similarityScorer = new SimilarityScorer();
            IProviderRanker ranker = new ProviderRanker(similarityScorer);
            userList = ranker.rank(responseMain, userToResponseMap);
            responseList = new ArrayList<>();
            for (User user : userList) { responseList.add(userToResponseMap.get(user)); }
        }

        return new SearchResult(userList, responseList);
    }

}

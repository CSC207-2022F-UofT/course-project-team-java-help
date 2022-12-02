package com.javahelp.backend.search;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.search.constraint.UserQueryConstraint;
import com.javahelp.backend.search.rank.IProviderRanker;
import com.javahelp.backend.search.rank.ISimilarityScorer;
import com.javahelp.backend.search.rank.ProviderRanker;
import com.javahelp.backend.search.rank.SimilarityScorer;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchInteractor {
    ISurveyStore surveyStore;
    ISurveyResponseStore responseStore;
    IUserStore userStore;

    public SearchInteractor(ISurveyStore surveyStore,
                         ISurveyResponseStore responseStore,
                         IUserStore userStore) {
        this.surveyStore = surveyStore;
        this.responseStore = responseStore;
        this.userStore = userStore;
    }

    public SearchResult search(ISearchInput input) {
        SurveyResponse responseMain = this.responseStore.read(input.getUserID());

        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(this.responseStore, this.userStore);
        Map<String, User> users = userQueryConstraint.getProvidersWithConstraints(input.getConstraint());
        Map<String, SurveyResponse> responses = userQueryConstraint.getResponsesWithConstraints(input.getConstraint());

        List<User> userList = new ArrayList<>();
        List<SurveyResponse> responseList = new ArrayList<>();
        Map<User, SurveyResponse> userToResponseMap = new HashMap<>();
        for (String userID : users.keySet()) {
            userList.add(users.get(userID));
            responseList.add(responses.get(userID));
            userToResponseMap.put(users.get(userID), responses.get(userID));
        }

        if (input.getIsRanking()) {
            ISimilarityScorer similarityScorer = new SimilarityScorer();
            IProviderRanker ranker = new ProviderRanker(similarityScorer);
            userList = ranker.rank(responseMain, userToResponseMap);
            responseList = new ArrayList<>();
            for (User user : userList) { responseList.add(userToResponseMap.get(user)); }
        }

        return new SearchResult(userList, responseList);
    }

}

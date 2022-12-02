package com.javahelp.backend.search;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.search.constraint.UserQueryConstraint;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

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
        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(this.responseStore, this.userStore);
        Map<String, User> users = userQueryConstraint.getProvidersWithConstraints(input.getConstraints());
        Map<String, SurveyResponse> responses = userQueryConstraint.getResponsesWithConstraints(input.getConstraints());

        return new SearchResult(users, responses);
    }

}

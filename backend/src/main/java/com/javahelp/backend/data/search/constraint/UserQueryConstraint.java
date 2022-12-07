package com.javahelp.backend.data.search.constraint;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;
import com.javahelp.backend.data.IUserStore;

import java.util.HashMap;
import java.util.Map;

/**
 * This class subsets the list of Providers in the database based on query constraints
 * specified by the user.
 * The query constraints are indicated using filters (multiple choice) or searches (text name).
 */
public class UserQueryConstraint implements IUserQueryConstraint{
    private ISurveyResponseStore dbSRStore;
    private IUserStore dbUserStore;

    public UserQueryConstraint(ISurveyResponseStore dbSurveyStore, IUserStore dbUserStore) {
        this.dbSRStore = dbSurveyStore;
        this.dbUserStore = dbUserStore;
    }

    @Override
    public Map<String, User> getProvidersWithConstraints(IConstraint constraint) {
        Map<String, SurveyResponse> responses = getResponsesWithConstraints(constraint);
        Map<String, User> usersWithConstraint = new HashMap<>();
        for (String id : responses.keySet()) {
            User user = this.dbUserStore.read(id);
            usersWithConstraint.put(id, user);
        }
        return usersWithConstraint;
    }

    public Map<String, User> getProvidersWithConstraints(Map<String, SurveyResponse> responses) {
        Map<String, User> usersWithConstraint = new HashMap<>();
        for (String id : responses.keySet()) {
            User user = this.dbUserStore.read(id);
            usersWithConstraint.put(id, user);
        }
        return usersWithConstraint;
    }

    @Override
    public Map<String, SurveyResponse> getResponsesWithConstraints(IConstraint constraint) {
        return this.dbSRStore.readByConstraint(constraint);
    }
}
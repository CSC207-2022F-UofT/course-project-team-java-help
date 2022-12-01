package com.javahelp.backend.query;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;
import com.javahelp.backend.data.IUserStore;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public Map<String, User> getProvidersWithConstraints(List<Constraint> constraints) {
        Map<String, SurveyResponse> responses = getResponsesWithConstraints(constraints);
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
    public Map<String, SurveyResponse> getResponsesWithConstraints(List<Constraint> constraints) {
        Map<String, Set<String>> combinedConstraintMap = new HashMap<>();

        for (int i = 0; i < constraints.size(); i++) {
            Constraint constraint = constraints.get(i);
            HashMap<String, Set<String>> constraintMap = constraint.getConstraint();
            for (String attr : constraintMap.keySet()) {
                if (combinedConstraintMap.containsKey(attr)) {
                    combinedConstraintMap.get(attr).retainAll(constraintMap.get(attr));
                } else {
                    combinedConstraintMap.put(attr, constraintMap.get(attr));
                }
            }
        }

        return this.dbSRStore.readByConstraint(combinedConstraintMap);
    }
}
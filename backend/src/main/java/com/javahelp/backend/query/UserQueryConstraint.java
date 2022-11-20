package com.javahelp.backend.query;

import com.javahelp.model.user.User;
import com.javahelp.backend.data.DynamoDBUserStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class subsets the list of Providers in the database based on query constraints
 * specified by the user.
 * The query constraints are indicated using filters (multiple choice) or searches (text name).
 */
public class UserQueryConstraint {
    private DynamoDBUserStore dbUserStore;

    public UserQueryConstraint(DynamoDBUserStore dbUserStore) {
        this.dbUserStore = dbUserStore;
    }

    public Set<User> getProvidersWithConstraints(List<Constraint> constraints) {
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

        return this.dbUserStore.readByConstraint(combinedConstraintMap);
    }
}

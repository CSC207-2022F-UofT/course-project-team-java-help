package com.javahelp.backend.query;

import com.javahelp.model.user.User;
import com.javahelp.backend.data.DynamoDBUserStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.io.*;

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

    public HashSet<User> getProvidersWithMultiConstraints(ArrayList<Constraint> constraints) {
        HashSet<User> subsetProviders;
        HashSet<User> providers = new HashSet<>();

        for (int i = 0; i < constraints.size(); i++) {
            Constraint constraint = constraints.get(i);
            subsetProviders = getProvidersWithConstraint(constraint);
            if (providers.isEmpty()) {
                providers = subsetProviders;
            }
            else {
                providers.retainAll(subsetProviders);
            }
        }
        return providers;
    }

    /**
     * This method takes a subset of ProvidersForFilter using API specified by the database.
     * @param constraint: the specified key-value pair required for the provider
     * @return subset of providers based on the given constraint
     */
    public HashSet<User> getProvidersWithConstraint(Constraint constraint) {
        HashMap<String, ArrayList<Integer>> constraintMap = constraint.getConstraint();
        List<User> providersList = this.dbUserStore.query(constraintMap);

        return new HashSet<>(providersList);
    }
}

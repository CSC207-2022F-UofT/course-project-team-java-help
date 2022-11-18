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

    public HashSet<User> getProvidersWithMultiConstraints(List<Constraint> constraints) {
        HashSet<User> providers = new HashSet<>();
        HashSet<String> providersId;

        for (int i = 0; i < constraints.size(); i++) {
            providersId = IdSet(providers);

            Constraint constraint = constraints.get(i);

            HashSet<User> subsetProviders = getProvidersWithConstraint(constraint);
            HashSet<String> subsetProvidersId = IdSet(subsetProviders);
            if (providers.isEmpty()) {
                providers = subsetProviders;
            }
            else {
                providersId.retainAll(subsetProvidersId);
                providers = getUserSetFromIdSet(providers, providersId);
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
        HashMap<String, ArrayList<String>> constraintMap = constraint.getConstraint();
        List<User> providersList = this.dbUserStore.readByConstraint(constraintMap);

        return new HashSet<>(providersList);
    }

    private HashSet<String> IdSet(HashSet<User> userSet) {
        HashSet<String> idSet = new HashSet<>();
        for (User user : userSet) {
            idSet.add(user.getStringID());
        }
        return idSet;
    }

    private HashSet<User> getUserSetFromIdSet(HashSet<User> userSet, HashSet<String> idSet) {
        HashSet<User> userSubSet = new HashSet<>();
        for (User user : userSet) {
            if (idSet.contains(user.getStringID())) {
                userSubSet.add(user);
            }
        }
        return userSubSet;
    }
}

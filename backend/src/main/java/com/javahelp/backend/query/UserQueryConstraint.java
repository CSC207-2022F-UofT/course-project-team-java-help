package com.javahelp.backend.query;

import com.javahelp.model.user.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This class subsets the list of Providers in the database based on query constraints
 * specified by the user.
 * The query constraints are indicated using filters (multiple choice) or searches (text name).
 */
public class UserQueryConstraint {
    private final DbQuery dbQuery;

    public UserQueryConstraint(DbQuery dbQuery) {
        this.dbQuery = dbQuery;
    }

    public HashSet<User> getProvidersWithMultiConstraints(ArrayList<Constraint> constraints) {
        HashSet<User> subsetProviders;
        HashSet<User> providers = new HashSet<>();

        for (int i = 0; i < constraints.size(); i++) {
            HashMap<String, ArrayList<Integer>> constraint = constraints.get(i).getConstraint();
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
    public HashSet<User> getProvidersWithConstraint(HashMap<String, ArrayList<Integer>> constraint) {
        List<User> providersList = this.dbQuery.query(constraint);

        return new HashSet<>(providersList);
    }
}

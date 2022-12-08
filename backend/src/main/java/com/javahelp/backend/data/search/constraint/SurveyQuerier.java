package com.javahelp.backend.data.search.constraint;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.Map;

/**
 * This class subsets the list of Providers in the database based on query constraints
 * specified by the user.
 * The query constraints are indicated using filters (multiple choice) or searches (text name).
 */
public class SurveyQuerier {
    private final ISurveyResponseStore dbSRStore;
    private final IUserStore dbUserStore;

    /**
     * Creates a new {@link SurveyQuerier}
     *
     * @param dbSurveyStore {@link ISurveyResponseStore} to use
     * @param dbUserStore   {@link IUserStore} to use
     */
    public SurveyQuerier(ISurveyResponseStore dbSurveyStore, IUserStore dbUserStore) {
        this.dbSRStore = dbSurveyStore;
        this.dbUserStore = dbUserStore;
    }

    /**
     * Get {@link User}s matching the specified constraint
     *
     * @param constraint {@link IConstraint} to use to fetch users
     * @return {@link Map} of {@link User} id and {@link User}
     */
    public Map<String, User> getUsersByConstraint(IConstraint constraint) {
        Map<String, SurveyResponse> responses = getSurveyResponses(constraint);
        String[] ids = responses.keySet().toArray(new String[0]);
        return dbUserStore.read(ids);
    }

    /**
     * Finds the {@link SurveyResponse}s matching the specified {@link IConstraint}
     *
     * @param constraint {@link IConstraint} to use to fetch  {@link SurveyResponse}s
     * @return {@link Map} of {@link User} ids and {@link SurveyResponse}s
     */
    public Map<String, SurveyResponse> getSurveyResponses(IConstraint constraint) {
        return dbSRStore.readProviderByConstraint(constraint);
    }
}
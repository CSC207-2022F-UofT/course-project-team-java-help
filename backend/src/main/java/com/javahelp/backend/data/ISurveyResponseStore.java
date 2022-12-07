package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.javahelp.backend.data.search.constraint.IConstraint;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ISurveyResponseStore {
    /**
     * @return Default implementation of {@link ISurveyResponseStore}
     */
    static ISurveyResponseStore getDefaultImplementation() {
        ISurveyStore surveyStore = ISurveyStore.getDefaultImplementation();
        return new DynamoDBSurveyResponseStore("javahelpBackendFilledSurveys",
                Regions.US_EAST_1,
                surveyStore);
    }

    /**
     * Creates a {@link SurveyResponse}
     *
     * @param userID {@link String} id of author (user) of this surveyResponse
     * @param surveyResponse {@link SurveyResponse} to create
     * @param isProvider {@link boolean} whether the surveyResponse is completed by a provider
     * @return {@link SurveyResponse} that was created
     */
    SurveyResponse create(String userID, SurveyResponse surveyResponse, boolean isProvider);

    /**
     * @param id {@link String} id of the {@link SurveyResponse}
     * @return {@link SurveyResponse} with the specified id
     */
    SurveyResponse read(String id);

    /**
     * Updates the specified object to match the provided object
     * @param userID {@link String} id of author (user) of this surveyResponse
     * @param surveyResponse object to update, will update based on the ID field of this object
     * @param isProvider {@link boolean} whether the surveyResponse is completed by a provider
     */
    void update(String userID, SurveyResponse surveyResponse, boolean isProvider);

    /**
     * Deletes the specified {@link SurveyResponse}
     *
     * @param id {@link String} id of the {@link SurveyResponse} to delete
     */
    void delete(String id);

    /**
     *
     * @param userID {@link String} id of the user to be queried.
     * @return {@link List<SurveyResponse>} of the specified user.
     */
    List<SurveyResponse> readByUser(String userID);

    /**
     *
     * @param constraint {@link Map <>} which specifies the required attributes
     *                   from the User.
     * @return {@link Set<User>} with the specified constraints.
     */
    Map<String, SurveyResponse> readByConstraint(IConstraint constraint);

    /**
     *
     * @return all {@link Set<User>} existing in database.
     */
    Map<String, SurveyResponse> readWithoutConstraint();
}

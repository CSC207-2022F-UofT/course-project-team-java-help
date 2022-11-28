package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
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
     * @return {@link SurveyResponse} that was created
     */
    SurveyResponse create(String userID, SurveyResponse surveyResponse);

    /**
     * @param id {@link String} id of the {@link SurveyResponse}
     * @return {@link SurveyResponse} with the specified id
     */
    SurveyResponse read(String id);

    /**
     * Updates the specified object to match the provided object
     * @param surveyResponse object to update, will update based on the ID field of this object
     */
    void update(String userID, SurveyResponse surveyResponse);

    /**
     * Deletes the specified {@link SurveyResponse}
     *
     * @param id {@link String} id of the {@link SurveyResponse} to delete
     */
    void delete(String id);

    /**
     * Removes all {@link SurveyResponse}s in database.
     * ONLY use during preliminary testing!
     */
    void cleanTable();

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
    Set<String> readByConstraint(Map<String, Set<String>> constraint);

    /**
     *
     * @return all {@link Set<User>} existing in database.
     */
    Set<String> readWithoutConstraint();
}

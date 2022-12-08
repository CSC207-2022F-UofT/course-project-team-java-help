package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.javahelp.model.survey.Survey;

public interface ISurveyStore {
    /**
     * @return Default implementation of {@link ISurveyStore}
     */
    static ISurveyStore getDefaultImplementation() {
        return new DynamoDBSurveyStore("javahelpBackendSurveys", Regions.US_EAST_1);
    }

    /**
     * Creates a {@link Survey}
     *
     * @param survey {@link Survey} to create
     * @return {@link Survey} that was created
     */
    Survey create(Survey survey);

    /**
     * @param id {@link String} id of the {@link Survey}
     * @return {@link Survey} with the specified id
     */
    Survey read(String id);

    /**
     * Deletes the specified {@link Survey}
     *
     * @param id {@link String} id of the {@link Survey} to delete
     */
    void delete(String id);
}

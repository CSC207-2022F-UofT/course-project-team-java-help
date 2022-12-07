package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

/**
 * Interface for interacting with {@link User}s in a database
 */
public interface IUserStore {

    /**
     * @return Default implementation of {@link IUserStore}
     */
    static IUserStore getDefaultImplementation() {
        return new DynamoDBUserStore("javahelpBackendUsers", Regions.US_EAST_1);
    }

    /**
     * Creates a {@link User}, then mutates that {@link User}
     * to provide it with its new ID
     *
     * @param u        {@link User} to create
     * @param password {@link UserPassword} to assign to the new {@link User}
     * @return {@link User} that was created
     */
    User create(User u, UserPassword password);

    /**
     * @param id {@link String} id of the {@link User} to get
     * @return {@link User} with the specified id
     */
    User read(String id);

    /**
     *
     * @param username {@link String} username of the {@link User} to get
     * @return {@link User} with the specified username
     */
    User readByUsername(String username);

    /**
     *
     * @param email {@link String} email of the {@link User} to get
     * @return {@link User} with the specified email
     */
    User readByEmail(String email);

    /**
     * Updates the specified {@link User}
     *
     * @param u {@link User} to update
     */
    void update(User u);

    /**
     * Deletes the specified {@link User}
     *
     * @param id {@link String} id of the {@link User} to delete
     */
    void delete(String id);

    /**
     * Updates the password of a {@link User}
     *
     * @param userId   {@link String} ID of the {@link User} to update
     * @param password {@link UserPassword} to assign
     */
    void updatePassword(String userId, UserPassword password);

    /**
     * Gets the {@link UserPassword} for the specified {@link User}
     *
     * @param userId {@link String} ID of the {@link User}
     * @return the {@link UserPassword}
     */
    UserPassword readPassword(String userId);

}

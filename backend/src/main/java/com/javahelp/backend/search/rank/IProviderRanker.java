package com.javahelp.backend.search.rank;

import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.List;
import java.util.Map;

public interface IProviderRanker {
    /**
     *
     * @param responseMain {@link SurveyResponse} to be compared with (i.e. the client)
     * @param responses {@link Map} of {@link User}s and their {@link SurveyResponse}s to be ranked
     * @return {@link List} of {@link User}s ranked in descending order of similarity
     * with the main user.
     */
    List<User> rank(SurveyResponse responseMain, Map<User, SurveyResponse> responses);

    /**
     *
     * @param mainResponse {@link SurveyResponse} of {@link User} to be ranked
     * @param responses {@link Map} of {@link User}s and their {@link SurveyResponse}s
     *                            to be compared with.
     * @return {@link Map} of {@link User}s with their corresponding similarity scores
     * with userMain.
     */
    Map<User, Float> getUsersWithScores(SurveyResponse mainResponse,
                                        Map<User, SurveyResponse> responses);
}
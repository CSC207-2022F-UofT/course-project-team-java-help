package com.javahelp.backend.domain.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.data.search.RandomSurveyPopulation;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Tests the {@link SearchInteractor}
 */
public class SearchInteractorTest {

    private ISurveyStore surveyDB = ISurveyStore.getDefaultImplementation();
    private ISurveyResponseStore srDB = ISurveyResponseStore.getDefaultImplementation();
    private IUserStore userDB = IUserStore.getDefaultImplementation();

    @Test
    public void testSearchWithConstraint() {
        RandomSurveyPopulation population = new RandomSurveyPopulation(surveyDB, srDB, userDB);

        try {
            population.populate();

            Set<String> attributes = new HashSet<>();
            attributes.add("attr0");

            SearchInteractor interactor = new SearchInteractor(surveyDB, srDB, userDB);

            SearchResult result = interactor.search(new ISearchInputBoundary() {
                @Override
                public String getUserID() {
                    return population.getRandomClient().getStringID();
                }

                @Override
                public Set<String> getConstraints() {
                    return attributes;
                }

                @Override
                public boolean getIsRanking() {
                    return false;
                }
            });

            assertTrue(result.getUsers().size() > 0);
        }
        finally {
            population.delete();
        }
    }

    @Test
    public void testSearchWithoutConstraint() {
        RandomSurveyPopulation population = new RandomSurveyPopulation(surveyDB, srDB, userDB);

        Set<String> attributes = new HashSet<>();

        try {
            population.populate();

            SearchInteractor interactor = new SearchInteractor(surveyDB, srDB, userDB);

            SearchResult result = interactor.search(new ISearchInputBoundary() {
                @Override
                public String getUserID() { return population.getRandomClient().getStringID(); }
                @Override
                public Set<String> getConstraints() { return attributes; }
                @Override
                public boolean getIsRanking() { return false; }
            });

            assertEquals(population.getPopulationSize(), result.getUsers().size());

            Set<String> expectedUsers = new HashSet<>();
            Set<String> actualUsers = new HashSet<>();
            Set<String> expectedResponses = new HashSet<>();
            Set<String> actualResponses = new HashSet<>();

            for (int i = 0; i < population.getPopulationSize(); i++) {
                expectedUsers.add(population.getRandomProviders().get(i).getStringID());
                actualUsers.add(result.getUsers().get(i).getStringID());
                expectedResponses.add(population.getRandomResponses().get(i).getID());
                actualResponses.add(result.getResponses().get(i).getID());
            }

            assertEquals(expectedUsers, actualUsers);
            assertEquals(expectedResponses, actualResponses);
        }
        finally {
            population.delete();
        }
    }
}

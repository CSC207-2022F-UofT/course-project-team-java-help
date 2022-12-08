package com.javahelp.backend.domain.search;

import static org.junit.Assert.assertTrue;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.data.search.RandomSurveyPopulation;
import com.javahelp.model.user.User;

import org.junit.Test;

import java.util.HashSet;
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

            SearchInteractor interactor = new SearchInteractor(srDB, userDB);

            SearchResult result = interactor.search(new ISearchInputBoundary() {
                @Override
                public String getSearchUserID() {
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
        } finally {
            population.delete();
        }
    }

    @Test
    public void testSearchWithoutConstraint() {
        RandomSurveyPopulation population = new RandomSurveyPopulation(surveyDB, srDB, userDB);

        Set<String> attributes = new HashSet<>();

        try {
            population.populate();

            SearchInteractor interactor = new SearchInteractor(srDB, userDB);

            SearchResult result = interactor.search(new ISearchInputBoundary() {
                @Override
                public String getSearchUserID() {
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

            assertTrue(population.getPopulationSize() <= result.getUsers().size());

            for (User u : population.getRandomProviders()) {
                assertTrue(result.getUsers().stream().map(User::getStringID).anyMatch(x -> u.getStringID().equals(x)));
            }
        } finally {
            population.delete();
        }
    }
}

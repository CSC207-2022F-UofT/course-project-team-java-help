package com.javahelp.backend.domain.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.search.ISearchInput;
import com.javahelp.backend.domain.search.SearchInteractor;
import com.javahelp.backend.domain.search.SearchResult;
import com.javahelp.backend.search.RandomDataPopulater;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchInteractorTest {
    private User client;
    private List<User> providers;
    private List<SurveyResponse> responses;

    private ISurveyStore surveyDB;
    private ISurveyResponseStore srDB;
    private IUserStore userDB;

    private RandomDataPopulater dataPopulater;

    @Before
    public void setup() {
        this.dataPopulater = new RandomDataPopulater(false);
        this.client = dataPopulater.getRandomClient();
        this.providers = dataPopulater.getRandomProviders();
        this.responses = dataPopulater.getRandomResponses();

        this.surveyDB = dataPopulater.getSurveyDB();
        this.srDB = dataPopulater.getSrDB();
        this.userDB = dataPopulater.getUserDB();
    }

    @Test
    public void testSearchWithConstraint() {
        User mainClient = this.dataPopulater.getRandomClient();

        User randomProvider1 = this.providers.get(0);
        SurveyResponse randomResponse1 = this.responses.get(0);
        Set<String> attributes1 = new HashSet<>();
        attributes1.add("attr0");

        try {
            SearchInteractor interactor = new SearchInteractor(this.surveyDB,
                    this.srDB,
                    this.userDB);

            SearchResult result = interactor.search(new ISearchInput() {
                @Override
                public String getUserID() { return mainClient.getStringID(); }
                @Override
                public Set<String> getConstraints() { return attributes1; }
                @Override
                public boolean getIsRanking() { return false; }
            });

            assertTrue(result.getUsers().size() > 0);
        }
        finally {
            this.dataPopulater.deleteRandomPopulation();
        }
    }

    @Test
    public void testSearchWithoutConstraint() {
        User mainClient = this.dataPopulater.getRandomClient();
        Set<String> attributes = new HashSet<>();

        List<User> randomProviders = this.providers;
        List<SurveyResponse> randomResponses = this.responses;

        try {
            SearchInteractor interactor = new SearchInteractor(this.surveyDB,
                    this.srDB,
                    this.userDB);

            SearchResult result = interactor.search(new ISearchInput() {
                @Override
                public String getUserID() { return mainClient.getStringID(); }
                @Override
                public Set<String> getConstraints() { return attributes; }
                @Override
                public boolean getIsRanking() { return false; }
            });

            assertEquals(this.dataPopulater.getPopulationNumber(), result.getUsers().size());
            assertEquals(randomProviders.size(), result.getUsers().size());

            Set<String> expectedUsers = new HashSet<>();
            Set<String> actualUsers = new HashSet<>();
            Set<String> expectedResponses = new HashSet<>();
            Set<String> actualResponses = new HashSet<>();
            for (int i = 0; i < randomProviders.size(); i++) {
                expectedUsers.add(randomProviders.get(i).getStringID());
                actualUsers.add(result.getUsers().get(i).getStringID());
                expectedResponses.add(randomResponses.get(i).getID());
                actualResponses.add(result.getResponses().get(i).getID());
            }

            assertEquals(expectedUsers, actualUsers);
            assertEquals(expectedResponses, actualResponses);
        }
        finally {
            this.dataPopulater.deleteRandomPopulation();
        }
    }
}

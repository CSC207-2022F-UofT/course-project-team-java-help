package com.javahelp.frontend.domain.user.search;

import static org.junit.Assert.assertTrue;

import com.javahelp.frontend.gateway.LambdaSearchDataAccess;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import org.apache.hc.core5.concurrent.CompletedFuture;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchInteractorTest {
    SearchResult result;
    String errorOutput = "";
    ISearchOutput output;

    @Before
    public void setup() {
        output = new ISearchOutput() {
            @Override
            public void success(List<User> users, List<SurveyResponse> responses) {
                result = new SearchResult(users, responses);
            }

            @Override
            public void failure() {
                result = new SearchResult("Failed to authenticate");
            }

            @Override
            public void error(String errorMessage) {
                result = null;
                errorOutput = errorMessage;
            }

            @Override
            public void abort() {
                result = null;
            }
        };
    }

    @Test
    public void testSearch() throws InterruptedException {
        Set<String> filterSet = new HashSet<>();

        SearchInteractor interactor = new SearchInteractor(output, LambdaSearchDataAccess.getInstance());
        interactor.search("test", filterSet, false);

        // give enough time for response to come backv
        Thread.sleep(15000);

        assertTrue(result.isSuccess());
    }
}

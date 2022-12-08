package com.javahelp.frontend.domain.search;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import com.javahelp.frontend.domain.user.login.LoginResult;
import com.javahelp.frontend.gateway.IAuthInformationProvider;
import com.javahelp.frontend.gateway.LambdaLoginDataAccess;
import com.javahelp.frontend.gateway.LambdaSaltDataAccess;
import com.javahelp.frontend.gateway.LambdaSearchDataAccess;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SearchInteractorTest {

    private static final Object SEARCH_LOCK = new Object();

    SearchResult result = null;

    /**
     * @return {@link LoginResult} used by this test
     */
    private LoginResult loginAccessible() {
        try {
            LambdaLoginDataAccess loginAccess = LambdaLoginDataAccess.getInstance();

            LambdaSaltDataAccess saltAccess = LambdaSaltDataAccess.getInstance();

            Future<byte[]> salt = saltAccess.getSalt("jacob", null, null, null);

            return loginAccess.login("jacob", null, null,
                            new UserPassword("password",
                                    salt.get(15000, TimeUnit.MILLISECONDS),
                                    SHAPasswordHasher.getInstance()), false, null)
                    .get(15000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return null;
        }
    }

    @Test(timeout = 30000)
    public void testSearch() throws InterruptedException {

        LoginResult r = loginAccessible();

        assumeTrue(r != null && r.isSuccess());

        ISearchOutput output = new ISearchOutput() {
            @Override
            public void success(List<User> users, List<SurveyResponse> responses) {
                result = new SearchResult(users, responses);
                synchronized (SEARCH_LOCK) {
                    SEARCH_LOCK.notifyAll();
                }
            }

            @Override
            public void failure() {
                result = new SearchResult("Failed to authenticate");
                synchronized (SEARCH_LOCK) {
                    SEARCH_LOCK.notifyAll();
                }
            }

            @Override
            public void error(String errorMessage) {
                result = null;
                synchronized (SEARCH_LOCK) {
                    SEARCH_LOCK.notifyAll();
                }
            }

            @Override
            public void abort() {
                result = null;
                synchronized (SEARCH_LOCK) {
                    SEARCH_LOCK.notifyAll();
                }
            }
        };

        Set<String> filterSet = new HashSet<>();

        SearchInteractor interactor = new SearchInteractor(output, new LambdaSearchDataAccess(new IAuthInformationProvider() {
            @Override
            public String getUserID() {
                return r.getUser().getStringID();
            }

            @Override
            public String getTokenString() {
                return r.getToken().getToken();
            }
        }));

        interactor.search(r.getUser().getStringID(), filterSet, false);

        synchronized (SEARCH_LOCK) {
            SEARCH_LOCK.wait();
        }

        assertTrue(result != null && result.isSuccess());
    }
}

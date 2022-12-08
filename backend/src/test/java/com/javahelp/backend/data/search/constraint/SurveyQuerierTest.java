package com.javahelp.backend.data.search.constraint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Tests {@link Constraint}
 */
public class SurveyQuerierTest {

    ISurveyStore surveys = ISurveyStore.getDefaultImplementation();
    ISurveyResponseStore responses = ISurveyResponseStore.getDefaultImplementation();
    IUserStore users = IUserStore.getDefaultImplementation();

    /**
     * @return whether the user table is accessible
     */
    private boolean userDatabaseAccessible() {
        try {
            users.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return whether the survey table is accessible
     */
    private boolean surveyDatabaseAccessible() {
        try {
            surveys.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return whether the response table is accessible
     */
    private boolean responseDatabaseAccessible() {
        try {
            responses.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test(timeout = 5000)
    public void testQueryEmptyConstraint() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(responseDatabaseAccessible());
        assumeTrue(surveyDatabaseAccessible());

        UserPassword p = randomUserPassword();
        User u1 = setupUser1();
        User u2 = setupUser2();

        Survey survey = setupSurvey();

        SurveyResponse sr1 = setupSurveyResponse1(survey), sr2 = setupSurveyResponse2(survey);

        Constraint constraint = new Constraint();

        try {
            users.create(u1, p);
            users.create(u2, p);

            surveys.create(survey);

            responses.create(u1.getStringID(), sr1, true);
            responses.create(u2.getStringID(), sr2, true);

            SurveyQuerier userQueryConstraint = new SurveyQuerier(responses, users);
            Map<String, SurveyResponse> responses = userQueryConstraint.getSurveyResponses(constraint);

            assertTrue(responses.containsKey(u1.getStringID()));
            assertTrue(responses.containsKey(u2.getStringID()));

            assertResponsesEqual(sr1, responses.get(u1.getStringID()));
            assertResponsesEqual(sr2, responses.get(u2.getStringID()));
        } finally {
            users.delete(u1.getStringID());
            users.delete(u2.getStringID());
            surveys.delete(survey.getID());
            responses.delete(sr1.getID());
            responses.delete(sr2.getID());
        }
    }

    @Test(timeout = 5000)
    public void testQuerySingleConstraint() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(responseDatabaseAccessible());
        assumeTrue(surveyDatabaseAccessible());

        UserPassword p = randomUserPassword();
        User u1 = setupUser1();
        User u2 = setupUser2();

        Survey survey = setupSurvey();

        SurveyResponse sr1 = setupSurveyResponse1(survey);
        SurveyResponse sr2 = setupSurveyResponse2(survey);

        try {
            users.create(u1, p);
            users.create(u2, p);

            surveys.create(survey);

            responses.create(u1.getStringID(), sr1, true);
            responses.create(u2.getStringID(), sr2, true);

            Constraint constraint = new Constraint();
            constraint.addConstraint("attr1");

            SurveyQuerier userQueryConstraint = new SurveyQuerier(responses, users);
            Map<String, SurveyResponse> responses = userQueryConstraint.getSurveyResponses(constraint);

            assertTrue(responses.containsKey(u1.getStringID()));

            assertResponsesEqual(sr1, responses.get(u1.getStringID()));
        } finally {
            users.delete(u1.getStringID());
            users.delete(u2.getStringID());
            surveys.delete(survey.getID());
            responses.delete(sr1.getID());
            responses.delete(sr2.getID());
        }
    }

    @Test()
    public void testQueryMultiConstraint() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(responseDatabaseAccessible());
        assumeTrue(surveyDatabaseAccessible());

        UserPassword p = randomUserPassword();
        User u1 = setupUser1();
        User u2 = setupUser2();

        Survey survey = setupSurvey();

        SurveyResponse sr1 = setupSurveyResponse1(survey);
        SurveyResponse sr2 = setupSurveyResponse2(survey);

        try {
            users.create(u1, p);
            users.create(u2, p);

            surveys.create(survey);

            responses.create(u1.getStringID(), sr1, true);
            responses.create(u2.getStringID(), sr2, true);

            Constraint constraint = new Constraint();
            constraint.addConstraint("attr0");
            constraint.addConstraint("attr2");

            SurveyQuerier userQueryConstraint = new SurveyQuerier(responses, users);
            Map<String, SurveyResponse> responses = userQueryConstraint.getSurveyResponses(constraint);

            assertTrue(responses.containsKey(u2.getStringID()));
            assertResponsesEqual(sr2, responses.get(u2.getStringID()));
        } finally {
            users.delete(u1.getStringID());
            users.delete(u2.getStringID());
            surveys.delete(survey.getID());
            responses.delete(sr1.getID());
            responses.delete(sr2.getID());
        }
    }

    /**
     * Asserts that two {@link SurveyResponse}s are equal
     *
     * @param sr1 first {@link SurveyResponse}
     * @param sr2 second {@link SurveyResponse}
     */
    private void assertResponsesEqual(SurveyResponse sr1, SurveyResponse sr2) {
        assertEquals(sr1.getSurvey().size(), sr2.getSurvey().size());
        assertEquals(sr1.getSurvey().getID(), sr2.getSurvey().getID());
        for (int i = 0; i < sr1.getSurvey().size(); i++) {
            SurveyQuestionResponse response1 = sr1.getResponse(i);
            SurveyQuestionResponse response2 = sr2.getResponse(i);
            assertEquals(response1.getQuestion().getQuestion(), response2.getQuestion().getQuestion());
            assertEquals(response1.getResponseNumber(), response2.getResponseNumber());
            assertEquals(response1.getResponse(), response2.getResponse());
        }
    }

    /**
     * @return the first test {@link User}
     */
    private User setupUser1() {
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client1@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");

        return new User("test1", clientInfo, "test_user1");
    }

    /**
     * @return the second test {@link User}
     */
    private User setupUser2() {
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client2@mail.com",
                "321  client road",
                "1234567890",
                "Jack",
                "Dorsey");

        return new User("test2", clientInfo, "test_user2");
    }

    /**
     * @return the test {@link Survey}
     */
    private Survey setupSurvey() {
        List<String> responses = new ArrayList<>();

        responses.add("This is the first response");
        responses.add("This is the second response");
        responses.add("This is the third response");

        SurveyQuestion first = new SurveyQuestion("This is the first survey question",
                responses);
        SurveyQuestion second = new SurveyQuestion("This is the second survey question",
                responses);

        first.setAnswerAttribute(0, "attr0");
        first.setAnswerAttribute(1, "attr1");
        first.setAnswerAttribute(2, "attr2");

        second.setAnswerAttribute(0, "attr0");
        second.setAnswerAttribute(1, "attr1");
        second.setAnswerAttribute(2, "attr2");

        List<SurveyQuestion> questions = new ArrayList<>();

        questions.add(first);
        questions.add(second);

        return new Survey("PSurvey_1", "Test Survey", questions);
    }

    /**
     * @param survey {@link Survey} to respond to
     * @return the first test {@link SurveyResponse}
     */
    private SurveyResponse setupSurveyResponse1(Survey survey) {
        return survey.answer("", 1, 0);
    }

    /**
     * @param survey {@link Survey} to respond to
     * @return the second {@link SurveyResponse}
     */
    private SurveyResponse setupSurveyResponse2(Survey survey) {
        return survey.answer("", 0, 2);
    }

    /**
     * @return A randomly generated {@link UserPassword}
     */
    private static UserPassword randomUserPassword() {
        Random random = new Random();
        int saltLength = 1 + random.nextInt(31);
        int hashLength = 1 + random.nextInt(31);

        byte[] salt = new byte[saltLength];
        byte[] hash = new byte[hashLength];

        random.nextBytes(salt);
        random.nextBytes(hash);

        return new UserPassword(salt, hash);
    }
}
package com.javahelp.backend.search.constraint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import com.javahelp.backend.data.ISurveyResponseStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;


public class UserQueryConstraintTest {
    ISurveyStore surveyDB = ISurveyStore.getDefaultImplementation();
    ISurveyResponseStore srDB = ISurveyResponseStore.getDefaultImplementation();
    IUserStore userDB = IUserStore.getDefaultImplementation();

    @Test
    public void testQueryEmptyConstraint() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(srDatabaseAccessible());
        assumeTrue(surveyDatabaseAccessible());

        //this.surveyDB.cleanTable();
        //this.srDB.cleanTable();
        //this.userDB.cleanTable();

        UserPassword p = randomUserPassword();
        User u1 = setupUser1();
        User u2 = setupUser2();
        u1 = this.userDB.create(u1, p);
        u2 = this.userDB.create(u2, p);

        Survey survey = setupSurvey();
        survey = this.surveyDB.create(survey);

        SurveyResponse sr1 = setupSurveyResponse1(survey);
        SurveyResponse sr2 = setupSurveyResponse2(survey);
        sr1 = this.srDB.create(u1.getStringID(), sr1, true);
        sr2 = this.srDB.create(u2.getStringID(), sr2, true);

        Constraint constraint = new Constraint();

        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(this.srDB, this.userDB);
        Map<String, SurveyResponse> responses = userQueryConstraint.getResponsesWithConstraints(constraint);

        Map<String, SurveyResponse> expected = new HashMap<>();
        expected.put(u1.getStringID(), sr1);
        expected.put(u2.getStringID(), sr2);

        assertEquals(expected.keySet(), responses.keySet());
        for (String id : expected.keySet()) {
            testResponsesEqual(survey, expected.get(id), responses.get(id));
        }

        this.userDB.delete(u1.getStringID());
        this.userDB.delete(u2.getStringID());
        this.surveyDB.delete(survey.getID());
        this.srDB.delete(sr1.getID());
        this.srDB.delete(sr2.getID());
    }

    @Test(timeout = 5000)
    public void testQuerySingleConstraint() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(srDatabaseAccessible());
        assumeTrue(surveyDatabaseAccessible());

        //this.surveyDB.cleanTable();
        //this.srDB.cleanTable();
        //this.userDB.cleanTable();

        UserPassword p = randomUserPassword();
        User u1 = setupUser1();
        User u2 = setupUser2();
        u1 = this.userDB.create(u1, p);
        u2 = this.userDB.create(u2, p);

        Survey survey = setupSurvey();
        survey = this.surveyDB.create(survey);

        SurveyResponse sr1 = setupSurveyResponse1(survey);
        SurveyResponse sr2 = setupSurveyResponse2(survey);
        sr1 = this.srDB.create(u1.getStringID(), sr1, true);
        sr2 = this.srDB.create(u2.getStringID(), sr2, true);

        Constraint constraint = new Constraint();
        constraint.setConstraint("attr1");

        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(this.srDB, this.userDB);
        Map<String, SurveyResponse> responses = userQueryConstraint.getResponsesWithConstraints(constraint);

        Map<String, SurveyResponse> expected = new HashMap<>();
        expected.put(u1.getStringID(), sr1);

        assertEquals(expected.keySet(), responses.keySet());
        for (String id : expected.keySet()) {
            testResponsesEqual(survey, expected.get(id), responses.get(id));
        }

        this.userDB.delete(u1.getStringID());
        this.userDB.delete(u2.getStringID());
        this.surveyDB.delete(survey.getID());
        this.srDB.delete(sr1.getID());
        this.srDB.delete(sr2.getID());
    }

    @Test(timeout = 5000)
    public void testQueryMultiConstraint() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(srDatabaseAccessible());
        assumeTrue(surveyDatabaseAccessible());

        //this.surveyDB.cleanTable();
        //this.srDB.cleanTable();
        //this.userDB.cleanTable();

        UserPassword p = randomUserPassword();
        User u1 = setupUser1();
        User u2 = setupUser2();
        u1 = this.userDB.create(u1, p);
        u2 = this.userDB.create(u2, p);

        Survey survey = setupSurvey();
        survey = this.surveyDB.create(survey);

        SurveyResponse sr1 = setupSurveyResponse1(survey);
        SurveyResponse sr2 = setupSurveyResponse2(survey);
        sr1 = this.srDB.create(u1.getStringID(), sr1, true);
        sr2 = this.srDB.create(u2.getStringID(), sr2, true);

        Constraint constraint = new Constraint();
        constraint.setConstraint("attr0");
        constraint.setConstraint("attr1");

        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(this.srDB, this.userDB);
        Map<String, SurveyResponse> responses = userQueryConstraint.getResponsesWithConstraints(constraint);

        Map<String, SurveyResponse> expected = new HashMap<>();
        expected.put(u1.getStringID(), sr1);

        assertEquals(expected.keySet(), responses.keySet());
        for (String id : expected.keySet()) {
            testResponsesEqual(survey, expected.get(id), responses.get(id));
        }

        this.userDB.delete(u1.getStringID());
        this.userDB.delete(u2.getStringID());
        this.surveyDB.delete(survey.getID());
        this.srDB.delete(sr1.getID());
        this.srDB.delete(sr2.getID());
    }

    private void testUserEqual(Set<User> userSet1, Set<User> userSet2) {
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        for (User u : userSet1) {
            set1.add(u.getStringID());
        }
        for (User u : userSet2) {
            set2.add(u.getStringID());
        }
        assertEquals(set1, set2);
    }

    private void testResponsesEqual(Survey survey, SurveyResponse sr1, SurveyResponse sr2) {
        assertEquals(survey.size(), sr1.size());
        assertEquals(survey.size(), sr2.size());
        for (int i = 0; i < survey.size(); i++) {
            SurveyQuestionResponse response1 = sr1.getResponse(i);
            SurveyQuestionResponse response2 = sr2.getResponse(i);
            assertEquals(response1.getQuestion().getQuestion(), response2.getQuestion().getQuestion());
            assertEquals(response1.getResponseNumber(), response2.getResponseNumber());
            assertEquals(response1.getResponse(), response2.getResponse());
        }
    }

    private User setupUser1() {
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client1@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");

        return new User("test1", clientInfo, "test_user1");
    }

    private User setupUser2() {
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client2@mail.com",
                "321  client road",
                "1234567890",
                "Jack",
                "Dorsey");

        return new User("test2", clientInfo, "test_user2");
    }

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

    private SurveyResponse setupSurveyResponse1(Survey survey) {
        SurveyQuestionResponse firstResponse = new SurveyQuestionResponse(survey.get(0), 1);
        SurveyQuestionResponse secondResponse = new SurveyQuestionResponse(survey.get(1), 0);

        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();

        map.put(survey.get(0), firstResponse);
        map.put(survey.get(1), secondResponse);

        return new SurveyResponse("response1", survey, map);
    }

    private SurveyResponse setupSurveyResponse2(Survey survey) {
        SurveyQuestionResponse firstResponse = new SurveyQuestionResponse(survey.get(0), 2);
        SurveyQuestionResponse secondResponse = new SurveyQuestionResponse(survey.get(1), 2);

        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();

        map.put(survey.get(0), firstResponse);
        map.put(survey.get(1), secondResponse);

        return new SurveyResponse("response1", survey, map);
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

    private boolean userDatabaseAccessible() {
        try {
            this.userDB.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean surveyDatabaseAccessible() {
        try {
            this.surveyDB.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean srDatabaseAccessible() {
        try {
            this.srDB.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
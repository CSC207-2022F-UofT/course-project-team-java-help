package com.javahelp.backend.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

import com.amazonaws.regions.Regions;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
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
    ISurveyResponseStore srDB = ISurveyResponseStore.getDefaultImplementation();
    IUserStore userDB = IUserStore.getDefaultImplementation();

    @Test(timeout = 5000)
    public void testQueryEmptyConstraint() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(srDatabaseAccessible());

        this.srDB.cleanTable();
        this.userDB.cleanTable();

        UserPassword p = randomUserPassword();
        User u1 = setupUser1();
        User u2 = setupUser2();
        u1 = this.userDB.create(u1, p);
        u2 = this.userDB.create(u2, p);

        SurveyResponse sr1 = setupSurveyResponse1();
        SurveyResponse sr2 = setupSurveyResponse2();
        sr1 = this.srDB.create(u1.getStringID(), sr1);
        sr2 = this.srDB.create(u2.getStringID(), sr2);

        Constraint constraint = new VanillaConstraint(sr1.getSurvey().get(0).getQuestion());
        List<Constraint> constraintList = new ArrayList<>();
        constraintList.add(constraint);

        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(this.srDB, this.userDB);
        Set<User> providers = userQueryConstraint.getProvidersWithConstraints(constraintList);

        Set<String> expectedProvidersSet = new HashSet<>();
        expectedProvidersSet.add(u1.getStringID());
        expectedProvidersSet.add(u2.getStringID());
        Set<String> actualProvidersSet = new HashSet<>();
        for (User user : providers) {
            actualProvidersSet.add(user.getStringID());
        }

        assertEquals(expectedProvidersSet, actualProvidersSet);

        this.userDB.delete(u1.getStringID());
        this.userDB.delete(u2.getStringID());
        this.srDB.delete(sr1.getID());
        this.srDB.delete(sr2.getID());
    }

    @Test(timeout = 5000)
    public void testQuerySingleConstraint() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(srDatabaseAccessible());

        this.srDB.cleanTable();
        this.userDB.cleanTable();

        UserPassword p = randomUserPassword();
        User u1 = setupUser1();
        User u2 = setupUser2();
        u1 = this.userDB.create(u1, p);
        u2 = this.userDB.create(u2, p);

        SurveyResponse sr1 = setupSurveyResponse1();
        SurveyResponse sr2 = setupSurveyResponse2();
        sr1 = this.srDB.create(u1.getStringID(), sr1);
        sr2 = this.srDB.create(u2.getStringID(), sr2);

        Constraint constraint = new VanillaConstraint(sr1.getSurvey().get(0).getQuestion());
        constraint.setConstraint("1");
        List<Constraint> constraintList = new ArrayList<>();
        constraintList.add(constraint);

        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(this.srDB, this.userDB);
        Set<User> providers = userQueryConstraint.getProvidersWithConstraints(constraintList);

        Set<String> expectedProvidersSet = new HashSet<>();
        expectedProvidersSet.add(u1.getStringID());
        Set<String> actualProvidersSet = new HashSet<>();
        for (User user : providers) {
            actualProvidersSet.add(user.getStringID());
        }

        assertEquals(expectedProvidersSet, actualProvidersSet);

        this.userDB.delete(u1.getStringID());
        this.userDB.delete(u2.getStringID());
        this.srDB.delete(sr1.getID());
        this.srDB.delete(sr2.getID());
    }

    @Test(timeout = 5000)
    public void testQueryMultiConstraint() {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(srDatabaseAccessible());

        UserPassword p = randomUserPassword();
        User u1 = setupUser1();
        User u2 = setupUser2();
        u1 = this.userDB.create(u1, p);
        u2 = this.userDB.create(u2, p);

        SurveyResponse sr1 = setupSurveyResponse1();
        SurveyResponse sr2 = setupSurveyResponse2();
        sr1 = this.srDB.create(u1.getStringID(), sr1);
        sr2 = this.srDB.create(u2.getStringID(), sr2);

        Constraint constraint1 = new VanillaConstraint(sr1.getSurvey().get(0).getQuestion());
        constraint1.setConstraint("2");
        Constraint constraint2 = new VanillaConstraint(sr1.getSurvey().get(1).getQuestion());
        constraint2.setConstraint("2");

        List<Constraint> constraintList = new ArrayList<>();
        constraintList.add(constraint1);
        constraintList.add(constraint2);

        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(this.srDB, this.userDB);
        Set<User> providers = userQueryConstraint.getProvidersWithConstraints(constraintList);

        Set<String> expectedProvidersSet = new HashSet<>();
        expectedProvidersSet.add(u2.getStringID());
        Set<String> actualProvidersSet = new HashSet<>();
        for (User user : providers) {
            actualProvidersSet.add(user.getStringID());
        }

        assertEquals(expectedProvidersSet, actualProvidersSet);

        this.userDB.delete(u1.getStringID());
        this.userDB.delete(u2.getStringID());
        this.srDB.delete(sr1.getID());
        this.srDB.delete(sr2.getID());
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

    private SurveyResponse setupSurveyResponse1() {
        List<String> responses = new ArrayList<>();

        responses.add("This is the first response");
        responses.add("This is the second response");
        responses.add("This is the third response");

        SurveyQuestion first = new SurveyQuestion("This is the first survey question",
                responses);
        SurveyQuestion second = new SurveyQuestion("This is the second survey question",
                responses);

        List<SurveyQuestion> questions = new ArrayList<>();

        questions.add(first);
        questions.add(second);

        Survey survey = new Survey("survey1", "Test Survey", questions);

        SurveyQuestionResponse firstResponse = new SurveyQuestionResponse(first, 1);
        SurveyQuestionResponse secondResponse = new SurveyQuestionResponse(second, 0);

        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();

        map.put(first, firstResponse);
        map.put(second, secondResponse);

        return new SurveyResponse("response1", survey, map);
    }

    private SurveyResponse setupSurveyResponse2() {
        List<String> responses = new ArrayList<>();

        responses.add("This is the first response");
        responses.add("This is the second response");
        responses.add("This is the third response");

        SurveyQuestion first = new SurveyQuestion("This is the first survey question",
                responses);
        SurveyQuestion second = new SurveyQuestion("This is the second survey question",
                responses);

        List<SurveyQuestion> questions = new ArrayList<>();

        questions.add(first);
        questions.add(second);

        Survey survey = new Survey("survey1", "Test Survey", questions);

        SurveyQuestionResponse firstResponse = new SurveyQuestionResponse(first, 2);
        SurveyQuestionResponse secondResponse = new SurveyQuestionResponse(second, 2);

        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();

        map.put(first, firstResponse);
        map.put(second, secondResponse);

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

    private boolean srDatabaseAccessible() {
        try {
            this.srDB.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
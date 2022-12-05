package com.javahelp.backend.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;

import com.amazonaws.regions.Regions;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBSurveyResponseStoreTest {
    String tableName = "javahelpBackendFilledSurveys";

    Regions regions = Regions.US_EAST_1;

    ISurveyStore surveyDB = ISurveyStore.getDefaultImplementation();
    DynamoDBSurveyResponseStore responseDB = new DynamoDBSurveyResponseStore(tableName,
            regions,
            surveyDB);

    @Test(timeout = 5000)
    public void testCreateRead() {
        assumeTrue(surveyDatabaseAccessible());
        assumeTrue(responseDatabaseAccessible());

        User user = setupUser();
        Survey survey = setupSurvey();
        survey = this.surveyDB.create(survey);

        SurveyResponse sr = setupSurveyResponse(survey);

        sr = this.responseDB.create(user.getStringID(), sr, true);

        SurveyResponse read = this.responseDB.read(sr.getID());

        try {
            testSurveyEqual(sr.getSurvey(), read.getSurvey());
            testResponsesEqual(survey, sr, read);
        }
        finally {
            this.surveyDB.delete(survey.getID());
            this.responseDB.delete(sr.getID());
        }
    }

    @Test(timeout = 5000)
    public void testDelete() {
        assumeTrue(surveyDatabaseAccessible());
        assumeTrue(responseDatabaseAccessible());

        User user = setupUser();
        Survey survey = setupSurvey();
        survey = this.surveyDB.create(survey);
        SurveyResponse sr = setupSurveyResponse(survey);

        this.responseDB.create(user.getStringID(), sr, true);

        SurveyResponse read = this.responseDB.read(sr.getID());
        try {
            assertNotNull(read);
        }
        finally {
            this.surveyDB.delete(survey.getID());
            this.responseDB.delete(sr.getID());
            SurveyResponse deleted = this.responseDB.read(sr.getID());
            assertNull(deleted);
        }
    }

    @Test(timeout = 5000)
    public void testCreateExistingResponse() {
        assumeTrue(surveyDatabaseAccessible());
        assumeTrue(responseDatabaseAccessible());

        //this.surveyDB.cleanTable();
        //this.responseDB.cleanTable();

        User user = setupUser();
        Survey survey = setupSurvey();
        survey = this.surveyDB.create(survey);
        SurveyResponse sr1 = setupSurveyResponse(survey);
        SurveyResponse sr2 = setupSurveyResponseAlt(survey);

        try {
            sr1 = this.responseDB.create(user.getStringID(), sr1, true);
            SurveyResponse read1 = this.responseDB.read(sr1.getID());
            testSurveyEqual(sr1.getSurvey(), read1.getSurvey());
            testResponsesEqual(sr1.getSurvey(), sr1, read1);

            sr2 = this.responseDB.create(user.getStringID(), sr2, true);
            assertEquals(sr2.getID(), sr1.getID());
            SurveyResponse read2 = this.responseDB.read(sr1.getID());
            testSurveyEqual(sr1.getSurvey(), read2.getSurvey());
            testResponsesEqual(sr2.getSurvey(), sr2, read2);
        }
        finally {
            this.surveyDB.delete(survey.getID());
            this.responseDB.delete(sr1.getID());
        }

    }

    private void testSurveyEqual(Survey survey1, Survey survey2) {
        assertEquals(survey1.getID(), survey2.getID());
        assertEquals(survey1.size(), survey2.size());
        for (int i = 0; i < survey1.size(); i++) {
            SurveyQuestion surveyQuestion = survey1.get(i);
            SurveyQuestion readQuestion = survey2.get(i);
            assertEquals(surveyQuestion.getNumberOfResponses(), readQuestion.getNumberOfResponses());
            for (int j = 0; j < surveyQuestion.getNumberOfResponses(); j++) {
                assertEquals(surveyQuestion.getAnswer(j), readQuestion.getAnswer(j));
                assertEquals(surveyQuestion.getAnswerAttribute(j), readQuestion.getAnswerAttribute(j));
            }
        }
    }

    private void testResponsesEqual(Survey survey, SurveyResponse sr1, SurveyResponse sr2) {
        assertEquals(survey.size(), sr1.size());
        assertEquals(survey.size(), sr2.size());
        assertEquals(sr1.getAttributes(), sr1.getAttributes());
        for (int i = 0; i < survey.size(); i++) {
            SurveyQuestionResponse response1 = sr1.getResponse(i);
            SurveyQuestionResponse response2 = sr2.getResponse(i);
            assertEquals(response1.getQuestion().getQuestion(), response2.getQuestion().getQuestion());
            assertEquals(response1.getResponseNumber(), response2.getResponseNumber());
            assertEquals(response1.getResponse(), response2.getResponse());
        }
    }

    private SurveyResponse setupSurveyResponse(Survey survey) {
        SurveyQuestion first = survey.get(0);
        SurveyQuestion second = survey.get(1);

        SurveyQuestionResponse firstResponse = new SurveyQuestionResponse(first, 1);
        SurveyQuestionResponse secondResponse = new SurveyQuestionResponse(second, 0);

        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();

        map.put(first, firstResponse);
        map.put(second, secondResponse);

        return new SurveyResponse("response1", survey, map);
    }

    private SurveyResponse setupSurveyResponseAlt(Survey survey) {
        SurveyQuestion first = survey.get(0);
        SurveyQuestion second = survey.get(1);

        SurveyQuestionResponse firstResponse = new SurveyQuestionResponse(first, 0);
        SurveyQuestionResponse secondResponse = new SurveyQuestionResponse(second, 1);

        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();

        map.put(first, firstResponse);
        map.put(second, secondResponse);

        return new SurveyResponse("response2", survey, map);
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

        first.setAnswerAttribute(0, "attr1");
        first.setAnswerAttribute(1, "attr2");
        first.setAnswerAttribute(2, "attr3");

        List<SurveyQuestion> questions = new ArrayList<>();

        questions.add(first);
        questions.add(second);

        return new Survey("survey1", "Test Survey", questions);
    }

    private User setupUser() {
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");
        User u = new User("test", clientInfo, "test_user");

        return u;
    }

    private boolean surveyDatabaseAccessible() {
        try {
            this.surveyDB.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean responseDatabaseAccessible() {
        try {
            this.responseDB.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
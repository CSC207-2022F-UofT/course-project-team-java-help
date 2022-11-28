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

    DynamoDBSurveyResponseStore db = new DynamoDBSurveyResponseStore(tableName, regions);

    @Test(timeout = 5000)
    public void testCreateRead() {
        assumeTrue(databaseAccessible());

        this.db.cleanTable();

        User user = setupUser();
        SurveyResponse sr = setupSurveyResponse();

        sr = this.db.create(user.getStringID(), sr);

        SurveyResponse read = this.db.read(sr.getID());

        try {
            testSurveyEqual(sr.getSurvey(), read.getSurvey());
            testResponsesEqual(sr.getSurvey(), sr, read);
        }
        finally {
            this.db.delete(sr.getID());
        }
    }

    @Test(timeout = 5000)
    public void testDelete() {
        assumeTrue(databaseAccessible());

        this.db.cleanTable();

        User user = setupUser();
        SurveyResponse sr = setupSurveyResponse();

        this.db.create(user.getStringID(), sr);

        SurveyResponse read = this.db.read(sr.getID());
        try {
            assertNotNull(read);
        }
        finally {
            this.db.delete(sr.getID());
            SurveyResponse deleted = this.db.read(sr.getID());
            assertNull(deleted);
        }
    }

    @Test(timeout = 5000)
    public void testCreateExistingResponse() {
        assumeTrue(databaseAccessible());

        this.db.cleanTable();

        User user = setupUser();
        SurveyResponse sr1 = setupSurveyResponse();
        SurveyResponse sr2 = setupSurveyResponseAlt();

        try {
            sr1 = this.db.create(user.getStringID(), sr1);
            SurveyResponse read1 = this.db.read(sr1.getID());
            testSurveyEqual(sr1.getSurvey(), read1.getSurvey());
            testResponsesEqual(sr1.getSurvey(), sr1, read1);

            sr2 = this.db.create(user.getStringID(), sr2);
            assertEquals(sr2.getID(), sr1.getID());
            SurveyResponse read2 = this.db.read(sr1.getID());
            testSurveyEqual(sr1.getSurvey(), read2.getSurvey());
            testResponsesEqual(sr2.getSurvey(), sr2, read2);
        }
        finally {
            this.db.delete(sr1.getID());
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
            }
        }
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

    private SurveyResponse setupSurveyResponse() {
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

    private SurveyResponse setupSurveyResponseAlt() {
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

        SurveyQuestionResponse firstResponse = new SurveyQuestionResponse(first, 0);
        SurveyQuestionResponse secondResponse = new SurveyQuestionResponse(second, 1);

        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();

        map.put(first, firstResponse);
        map.put(second, secondResponse);

        return new SurveyResponse("response2", survey, map);
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

    private boolean databaseAccessible() {
        try {
            this.db.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
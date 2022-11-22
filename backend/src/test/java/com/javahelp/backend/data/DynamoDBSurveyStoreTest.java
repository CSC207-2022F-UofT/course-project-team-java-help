package com.javahelp.backend.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.amazonaws.regions.Regions;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DynamoDBSurveyStoreTest {
    String tableName = "javahelpBackendSurveys";

    Regions regions = Regions.US_EAST_1;

    DynamoDBSurveyStore db = new DynamoDBSurveyStore(tableName, regions);

    @Test(timeout = 5000)
    public void testCreateRead() {
        this.db.cleanTable();

        Survey survey = setupSurvey();

        this.db.create(survey);

        Survey read = this.db.read(survey.getID());

        assertEquals(survey.size(), read.size());
        for (int i = 0; i < survey.size(); i++) {
            SurveyQuestion surveyQuestion = survey.get(i);
            SurveyQuestion readQuestion = read.get(i);
            assertEquals(surveyQuestion.getNumberOfResponses(), readQuestion.getNumberOfResponses());
            for (int j = 0; j < surveyQuestion.getNumberOfResponses(); j++) {
                assertEquals(surveyQuestion.getAnswer(j), readQuestion.getAnswer(j));
            }
        }

        this.db.delete(survey.getID());
    }

    @Test(timeout = 5000)
    public void testDelete() {
        this.db.cleanTable();

        Survey survey = setupSurvey();

        this.db.create(survey);

        Survey read = this.db.read(survey.getID());
        assertNotNull(read);

        this.db.delete(survey.getID());

        Survey deleted = this.db.read(survey.getID());
        assertNull(deleted);
    }

    private Survey setupSurvey() {
        List<SurveyQuestion> questions = new ArrayList<>();
        List<String> responses1 = new ArrayList<>();
        responses1.add("This is a response option");
        responses1.add("This is a second response option");

        List<String> responses2 = new ArrayList<>();
        responses2.add("This question has three answers / 1");
        responses2.add("This question has three answers / 2");
        responses2.add("This question has three answers / 3");

        List<String> outOf10Responses = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            outOf10Responses.add(Integer.toString(i));
        }

        questions.add(new SurveyQuestion("This is the first question", responses1));
        questions.add(new SurveyQuestion("This is the second question", responses2));
        for (int i = 3; i <= 5; i++) {
            questions.add(new SurveyQuestion("This is the " + i + "th question", outOf10Responses));
        }

        Survey survey = new Survey("survey1", "Test Survey", questions);

        return survey;
    }

}
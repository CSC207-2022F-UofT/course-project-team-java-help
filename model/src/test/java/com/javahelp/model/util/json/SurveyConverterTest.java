package com.javahelp.model.util.json;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.JsonObject;

public class SurveyConverterTest {
    private Survey survey = setupSurvey();
    private SurveyConverter converter = SurveyConverter.getInstance();

    @Test
    public void convert() {
        JsonObject converted = converter.toJSON(survey);
        Survey restored = converter.fromJSON(converted);

        assertEquals(survey.getID(), restored.getID());
        assertEquals(survey.getName(), restored.getName());
        for (int i = 0; i < survey.size(); i++) {
            testSurveyQuestionEqual(survey.get(i), restored.get(i));
        }
    }

    @Test
    public void convertString() {
        String converted = converter.toJSONString(survey);
        Survey restored = converter.fromJSONString(converted);

        assertEquals(survey.getID(), restored.getID());
        assertEquals(survey.getName(), restored.getName());
        for (int i = 0; i < survey.size(); i++) {
            testSurveyQuestionEqual(survey.get(i), restored.get(i));
        }
    }

    private void testSurveyQuestionEqual(SurveyQuestion q1, SurveyQuestion q2) {
        assertEquals(q1.getQuestion(), q2.getQuestion());
        for (int i = 0; i < q1.getNumberOfResponses(); i++) {
            assertEquals(q1.getAnswer(i), q2.getAnswer(i));
            assertEquals(q1.getAnswerAttribute(i), q2.getAnswerAttribute(i));
        }
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

        List<SurveyQuestion> questions = new ArrayList<>();

        questions.add(first);
        questions.add(second);

        return new Survey("survey1", "Test Survey", questions);
    }
}

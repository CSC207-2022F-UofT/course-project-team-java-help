package com.javahelp.model.util.json;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.json.JsonObject;

public class SurveyResponseConverterTest {
    private Survey survey = setupSurvey();
    private SurveyResponse response = setupSurveyResponse(survey);
    private SurveyResponseConverter converter = SurveyResponseConverter.getInstance();

    @Test
    public void convert() {
        JsonObject converted = converter.toJSON(response);
        SurveyResponse restored = converter.fromJSON(converted);

        assertEquals(response.getID(), restored.getID());
        testResponsesEqual(survey, response, restored);
    }

    @Test
    public void convertString() {
        String converted = converter.toJSONString(response);
        SurveyResponse restored = converter.fromJSONString(converted);

        assertEquals(response.getID(), restored.getID());
        testResponsesEqual(survey, response, restored);
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

    private SurveyResponse setupSurveyResponse(Survey survey) {
        SurveyQuestionResponse firstResponse = new SurveyQuestionResponse(survey.get(0), 1);
        SurveyQuestionResponse secondResponse = new SurveyQuestionResponse(survey.get(1), 0);

        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();

        map.put(survey.get(0), firstResponse);
        map.put(survey.get(1), secondResponse);

        return new SurveyResponse("response1", survey, map);
    }
}

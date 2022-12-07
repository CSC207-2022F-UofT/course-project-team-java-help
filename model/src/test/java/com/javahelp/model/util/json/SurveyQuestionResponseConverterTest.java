package com.javahelp.model.util.json;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.JsonObject;

public class SurveyQuestionResponseConverterTest {
    private SurveyQuestionResponse response = setupQuestionResponse();
    private SurveyQuestionResponseConverter converter = SurveyQuestionResponseConverter.getInstance();

    @Test
    public void convert() {
        JsonObject converted = converter.toJSON(response);
        SurveyQuestionResponse restored = converter.fromJSON(converted);

        assertEquals(response.getResponseNumber(), restored.getResponseNumber());
        testSurveyQuestionEqual(response.getQuestion(), restored.getQuestion());
    }

    @Test
    public void convertString() {
        String converted = converter.toJSONString(response);
        SurveyQuestionResponse restored = converter.fromJSONString(converted);

        assertEquals(response.getResponseNumber(), restored.getResponseNumber());
        testSurveyQuestionEqual(response.getQuestion(), restored.getQuestion());
    }

    private void testSurveyQuestionEqual(SurveyQuestion q1, SurveyQuestion q2) {
        assertEquals(q1.getQuestion(), q2.getQuestion());
        for (int i = 0; i < q1.getNumberOfResponses(); i++) {
            assertEquals(q1.getAnswer(i), q2.getAnswer(i));
            assertEquals(q1.getAnswerAttribute(i), q2.getAnswerAttribute(i));
        }
    }

    private SurveyQuestionResponse setupQuestionResponse() {
        List<String> responses = new ArrayList<>();

        responses.add("This is the first response");
        responses.add("This is the second response");
        responses.add("This is the third response");

        SurveyQuestion question = new SurveyQuestion("This is the first survey question",
                responses);

        return new SurveyQuestionResponse(question, 1);
    }
}

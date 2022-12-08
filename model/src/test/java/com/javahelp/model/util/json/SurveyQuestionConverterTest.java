package com.javahelp.model.util.json;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.survey.SurveyQuestion;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.JsonObject;

public class SurveyQuestionConverterTest {
    private final SurveyQuestion question = setupQuestion();
    private final SurveyQuestionConverter converter = SurveyQuestionConverter.getInstance();

    @Test
    public void convert() {
        JsonObject converted = converter.toJSON(question);
        SurveyQuestion restored = converter.fromJSON(converted);

        assertEquals(question.getQuestion(), restored.getQuestion());
        for (int i = 0; i < question.getNumberOfResponses(); i++) {
            assertEquals(question.getAnswer(i), restored.getAnswer(i));
            assertEquals(question.getAnswerAttribute(i), restored.getAnswerAttribute(i));
        }
    }

    @Test
    public void convertString() {
        String converted = converter.toJSONString(question);
        SurveyQuestion restored = converter.fromJSONString(converted);

        assertEquals(question.getQuestion(), restored.getQuestion());
        for (int i = 0; i < question.getNumberOfResponses(); i++) {
            assertEquals(question.getAnswer(i), restored.getAnswer(i));
            assertEquals(question.getAnswerAttribute(i), restored.getAnswerAttribute(i));
        }
    }

    private SurveyQuestion setupQuestion() {
        List<String> responses = new ArrayList<>();
        responses.add("This is the first response");
        responses.add("This is the second response");
        responses.add("This is the third response");

        return new SurveyQuestion("This is the first survey question", responses);
    }

}

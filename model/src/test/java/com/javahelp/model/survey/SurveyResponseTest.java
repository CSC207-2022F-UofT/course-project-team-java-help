package com.javahelp.model.survey;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for {@link SurveyResponse}
 */
public class SurveyResponseTest {

    SurveyResponse response;

    SurveyQuestion second;

    @Before
    public void setUp() {

        List<String> responses = new ArrayList<>();

        responses.add("This is the first response");
        responses.add("This is the second response");
        responses.add("This is the third response");

        SurveyQuestion first = new SurveyQuestion("This is the first survey question",
                responses);
        second = new SurveyQuestion("This is the second survey question",
                responses);

        List<SurveyQuestion> questions = new ArrayList<>();

        questions.add(first);
        questions.add(second);

        Survey survey = new Survey("this is an id", "Test Survey", questions);

        SurveyQuestionResponse firstResponse = new SurveyQuestionResponse(first, 1);
        SurveyQuestionResponse secondResponse = new SurveyQuestionResponse(second, 0);

        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();

        map.put(first, firstResponse);
        map.put(second, secondResponse);

        response = new SurveyResponse("this is a survey response id", survey, map);
    }

    @Test
    public void getSurvey() {
        assertEquals("this is an id", response.getSurvey().getID());
    }

    @Test
    public void getResponse() {

        assertEquals("This is the first survey question",
            response.getResponse(0).getQuestion().getQuestion());

        assertEquals("This is the second survey question",
            response.getResponse(second).getQuestion().getQuestion());
    }

    @Test
    public void getNonExistingResponse() {
        List<String> responses = new ArrayList<>();

        responses.add("test");
        SurveyQuestion sq = new SurveyQuestion("Third survey question", responses);
        SurveyQuestionResponse expected = new SurveyQuestionResponse(sq, -1);

        assertEquals(expected.getResponseNumber(), response.getResponse(sq).getResponseNumber());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getResponse_throwsIndexOutOfBoundsException() {
        response.getResponse(7);
    }

    @Test
    public void size() {
        assertEquals(2, response.size());
    }

    @Test
    public void getID() {
        assertEquals("this is a survey response id", response.getID());
    }
}
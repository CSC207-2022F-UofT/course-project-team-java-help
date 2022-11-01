package com.javahelp.model.survey;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link SurveyQuestionResponse}
 */
public class SurveyQuestionResponseTest {

    SurveyQuestionResponse response;

    @Before
    public void setUp() {
        List<String> responses = new ArrayList<>();

        responses.add("This is the first response");
        responses.add("This is the second response");

        SurveyQuestion question = new SurveyQuestion("Which response do you choose?",
                responses);

        response = new SurveyQuestionResponse(question, 1);
    }

    @Test
    public void getQuestion() {
        assertEquals("Which response do you choose?", response.getQuestion().getQuestion());
    }

    @Test
    public void getResponseNumber() {
        assertEquals(1, response.getResponseNumber());
    }

    @Test
    public void getResponse() {
        assertEquals("This is the second response", response.getResponse());
    }
}
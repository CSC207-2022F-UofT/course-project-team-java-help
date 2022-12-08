package com.javahelp.model.survey;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link Survey}
 */
public class SurveyTest {

    Survey survey;

    @Before
    public void setupSurvey() {

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

        survey = new Survey("this is a random id", "Test Survey", questions);
    }

    @Test
    public void getID() {
        assertEquals("this is a random id", survey.getID());
    }

    @Test
    public void getName() {
        assertEquals("Test Survey", survey.getName());
    }

    @Test
    public void get() {
        assertEquals("This is the 4th question", survey.get(3).getQuestion());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_throwsIndexOutOfBoundsException() {
        survey.get(5);
    }

    @Test
    public void getQuestions() {
        assertEquals(2, survey.getQuestions().iterator().next().getNumberOfResponses());
    }

    @Test
    public void size() {
        assertEquals(5, survey.size());
    }

    @Test
    public void answer() {
        SurveyResponse response = survey.answer("test id", 0, 1, 7, 8, 9);
        assertEquals("10", response.getResponse(4).getResponse());
    }

    @Test(expected = IllegalArgumentException.class)
    public void answer_throwsIllegalArgumentException() {
        survey.answer("test", 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }
}
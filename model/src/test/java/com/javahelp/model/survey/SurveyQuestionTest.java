package com.javahelp.model.survey;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for {@link SurveyQuestion}
 */
public class SurveyQuestionTest {

    SurveyQuestion question, emptyQuestion;

    @Before
    public void setupQuestion() {
        emptyQuestion = new SurveyQuestion("This is an example question", new ArrayList<>());

        List<String> responses = new ArrayList<>();
        responses.add("This is an example response option");
        responses.add("This is another example response option");

        question = new SurveyQuestion("This is another example question", responses);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void getAnswer_throwsIndexOutOfBounds() {
        emptyQuestion.getAnswer(0);
    }

    @Test
    public void getAnswers_size() {
        int emptyLength = 0, nonEmptyLength = 0;

        for (String s : emptyQuestion.getAnswers()) {
            emptyLength++;
        }

        for (String s : question.getAnswers()) {
            nonEmptyLength++;
        }

        assertEquals(0, emptyLength);

        assertEquals(2, nonEmptyLength);
    }

    @Test
    public void getAnswer() {
        assertEquals("This is an example response option", question.getAnswer(0));
    }

    @Test
    public void getQuestion() {
        assertEquals("This is an example question", emptyQuestion.getQuestion());
    }

}

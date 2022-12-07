package com.javahelp.model.survey;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Unit tests for {@link SurveyQuestion}
 */
public class SurveyQuestionTest {

    SurveyQuestion question, emptyQuestion;
    Set<String> attrSet1 = new HashSet<>();
    Set<String> attrSet2 = new HashSet<>();

    @Before
    public void setupQuestion() {
        emptyQuestion = new SurveyQuestion("This is an example question", new ArrayList<>());

        List<String> responses = new ArrayList<>();
        responses.add("This is an example response option");
        responses.add("This is another example response option");

        question = new SurveyQuestion("This is another example question", responses);

        attrSet1.add("attr1");
        attrSet1.add("attr2");
        attrSet2.add("attr3");
        question.setAnswerAttribute(0, attrSet1);
        question.setAnswerAttribute(1, attrSet2);
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
    public void getAnswerAttributes() {
        assertEquals(attrSet1, question.getAnswerAttribute(0));
        assertEquals(attrSet2, question.getAnswerAttribute(1));
    }

    @Test
    public void getNumberOfResponses() {
        assertEquals(2, question.getNumberOfResponses());
    }

    @Test
    public void getAnswer() {
        assertEquals("This is an example response option", question.getAnswer(0));
    }

    @Test
    public void getQuestion() {
        assertEquals("This is an example question", emptyQuestion.getQuestion());
    }

    @Test
    public void answer() {
        assertEquals("This is an example response option", question.answer(0).getResponse());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void answer_throwsIndexOutOfBoundsException() {
        question.answer(100);
    }

}

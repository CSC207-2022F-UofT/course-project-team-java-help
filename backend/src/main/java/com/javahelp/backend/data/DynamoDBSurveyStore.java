package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DynamoDBSurveyStore extends DynamoDBStore implements ISurveyStore{
    private String tableName;
    DynamoDBSurveyStore(String tableName, Regions region) {
        super(region);
        this.tableName = tableName;
    }

    /**
     * Creates a {@link Survey}
     *
     * @param survey {@link Survey} to create
     * @return {@link Survey} that was created
     */
    @Override
    public Survey create(Survey survey) {
        if (read(survey.getID()) == null) {
            Map<String, AttributeValue> item = fromSurvey(survey);

            PutItemRequest request = new PutItemRequest()
                    .withTableName(tableName)
                    .withItem(item);

            getClient().putItem(request);
        }
        return survey;
    }

    /**
     * @param id {@link String} id of the {@link Survey}
     * @return {@link Survey} with the specified id
     */
    @Override
    public Survey read(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));

        GetItemRequest request = new GetItemRequest()
                .withTableName(tableName)
                .withKey(key);

        GetItemResult result = getClient().getItem(request);

        if (result.getItem() == null) {
            return null;
        }

        return surveyFromDynamo(result.getItem());
    }

    /**
     * Deletes the specified {@link Survey}
     *
     * @param id {@link String} id of the {@link Survey} to delete
     */
    @Override
    public void delete(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));

        DeleteItemRequest request = new DeleteItemRequest()
                .withTableName(tableName)
                .withKey(key);

        getClient().deleteItem(request);
    }

    @Override
    public void cleanTable() {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(this.tableName);
        ScanResult result = getClient().scan(scanRequest);

        for (Map<String, AttributeValue> item : result.getItems()) {
            if (item != null) {
                delete(item.get("id").getS());
            }
        }
    }

    private static Map<String, AttributeValue> fromSurvey(Survey s) {
        Map<String, AttributeValue> survey = new HashMap<>();

        survey.put("id", new AttributeValue().withS(String.join("-", s.getID().split(" "))));
        survey.put("name", new AttributeValue().withS(s.getName()));

        List<AttributeValue> questionList = new ArrayList<>();
        Map<String, AttributeValue> questionAnswerMap = new HashMap<>();
        for (SurveyQuestion question : s.getQuestions()) {
            String formattedQuestion = String.join("-", question.getQuestion().split(" "));
            questionList.add(new AttributeValue().withS(formattedQuestion));
            List<AttributeValue> answerList = new ArrayList<>();
            for (String answer : question.getAnswers()) {
                answerList.add(new AttributeValue().withS(answer));
            }
            questionAnswerMap.put(formattedQuestion, new AttributeValue().withL(answerList));
        }

        survey.put("questions", new AttributeValue().withL(questionList));
        survey.put("answers", new AttributeValue().withM(questionAnswerMap));

        return survey;
    }

    private static Survey surveyFromDynamo(Map<String, AttributeValue> item) {
        String id = String.join(" ", item.get("id").getS().split("-"));
        String name = item.get("name").getS();

        List<SurveyQuestion> questionList = new ArrayList<>();

        for (AttributeValue question : item.get("questions").getL()) {
            String questionPrompt = String.join(" ", question.getS().split("-"));
            List<String> answers = new ArrayList<>();
            for (AttributeValue answer : item.get("answers").getM().get(question.getS()).getL()) {
                answers.add(answer.getS());
            }

            SurveyQuestion surveyQuestion = new SurveyQuestion(questionPrompt, answers);
            questionList.add(surveyQuestion);
        }

        return new Survey(id, name, questionList);
    }
}

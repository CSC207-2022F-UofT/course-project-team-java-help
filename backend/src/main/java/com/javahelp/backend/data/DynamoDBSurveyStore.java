package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class DynamoDBSurveyStore extends DynamoDBStore implements ISurveyStore{
    private final String tableName;
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
        String id = UUID.randomUUID().toString();

        survey.setID(id);

        Map<String, AttributeValue> item = fromSurvey(survey);

        PutItemRequest request = new PutItemRequest()
                .withTableName(tableName)
                .withItem(item);

        getClient().putItem(request);

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
                .withKey(key)
                .withConsistentRead(true);

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

    private static Map<String, AttributeValue> fromSurvey(Survey s) {
        Map<String, AttributeValue> survey = new HashMap<>();

        survey.put("id", new AttributeValue().withS(s.getID()));
        survey.put("name", new AttributeValue().withS(s.getName()));

        List<AttributeValue> questionList = new ArrayList<>();
        Map<String, AttributeValue> questionAttrMap = new HashMap<>();
        Map<String, AttributeValue> questionAnswerMap = new HashMap<>();
        for (SurveyQuestion question : s.getQuestions()) {
            String formattedQuestion = String.join("_", question.getQuestion().split(" "));
            questionList.add(new AttributeValue().withS(formattedQuestion));
            List<AttributeValue> answerList = new ArrayList<>();
            List<AttributeValue> attrList = new ArrayList<>();
            for (int i = 0; i < question.getNumberOfResponses(); i++) {
                answerList.add(new AttributeValue().withS(question.getAnswer(i)));
                attrList.add(new AttributeValue().withSS(question.getAnswerAttribute(i)));
            }
            questionAnswerMap.put(formattedQuestion, new AttributeValue().withL(answerList));
            questionAttrMap.put(formattedQuestion, new AttributeValue().withL(attrList));
        }

        survey.put("questions", new AttributeValue().withL(questionList));
        survey.put("answers", new AttributeValue().withM(questionAnswerMap));
        survey.put("attributes", new AttributeValue().withM(questionAttrMap));

        return survey;
    }

    private static Survey surveyFromDynamo(Map<String, AttributeValue> item) {
        String id = item.get("id").getS();
        String name = item.get("name").getS();

        List<SurveyQuestion> questionList = new ArrayList<>();

        for (AttributeValue question : item.get("questions").getL()) {
            String questionPrompt = String.join(" ", question.getS().split("_"));
            List<String> answers = new ArrayList<>();
            for (AttributeValue answer : item.get("answers").getM().get(question.getS()).getL()) {
                answers.add(answer.getS());
            }

            SurveyQuestion surveyQuestion = new SurveyQuestion(questionPrompt, answers);
            int n = 0;
            for (AttributeValue attrSet : item.get("attributes").getM().get(question.getS()).getL()) {
                surveyQuestion.setAnswerAttribute(n, new HashSet<>(attrSet.getSS()));
                n = n + 1;
            }

            questionList.add(surveyQuestion);
        }

        return new Survey(id, name, questionList);
    }
}

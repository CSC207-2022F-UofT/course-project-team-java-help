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
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class DynamoDBSurveyResponseStore extends DynamoDBStore implements ISurveyResponseStore{
    private String tableName;
    private static ISurveyStore surveyStore = ISurveyStore.getDefaultImplementation();

    DynamoDBSurveyResponseStore(String tableName, Regions region) {
        super(region);
        this.tableName = tableName;
    }

    /**
     * Creates a {@link SurveyResponse}
     *
     * @param userID {@link String} id of author (user) of this surveyResponse
     * @param surveyResponse {@link SurveyResponse} to create
     * @return {@link SurveyResponse} that was created
     */
    @Override
    public SurveyResponse create(String userID, SurveyResponse surveyResponse) {
        String id = UUID.randomUUID().toString();

        surveyResponse.setID(id);

        Map<String, AttributeValue> item = fromSurveyResponse(userID, surveyResponse);

        PutItemRequest request = new PutItemRequest()
                .withTableName(tableName)
                .withItem(item);

        getClient().putItem(request);

        return surveyResponse;
    }

    /**
     * @param id {@link String} id of the {@link SurveyResponse}
     * @return {@link SurveyResponse} with the specified id
     */
    @Override
    public SurveyResponse read(String id){
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(id));

        GetItemRequest request = new GetItemRequest()
                .withTableName(tableName)
                .withKey(key);

        GetItemResult result = getClient().getItem(request);

        if (result.getItem() == null) {
            return null;
        }

        return surveyResponseFromDynamo(result.getItem());
    }

    /**
     * Deletes the specified {@link SurveyResponse}
     *
     * @param id {@link String} id of the {@link SurveyResponse} to delete
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

    /**
     * Removes all {@link SurveyResponse}s in database.
     * ONLY use during preliminary testing!
     */
    @Override
    public void cleanTable() {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(this.tableName);
        ScanResult result = getClient().scan(scanRequest);

        for (Map<String, AttributeValue> item : result.getItems()) {
            SurveyResponse surveyResponse = surveyResponseFromDynamo(item);
            if (surveyResponse != null) {
                delete(surveyResponse.getID());
            }
        }
    }

    /**
     *
     * @param userID {@link String} id of the user to be queried.
     * @return {@link List <SurveyResponse>} of the specified user.
     */
    @Override
    public List<SurveyResponse> readByUser(String userID) {
        return null;
    }

    /**
     *
     * @param constraint {@link Map <>} which specifies the required attributes
     *                   from the User.
     * @return {@link Set <User>} with the specified constraints.
     */
    @Override
    public Set<String> readByConstraint(Map<String, Set<String>> constraint) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();

        int n_questions = 0;
        int n_answers = 0;
        StringBuilder conditionBuilder = new StringBuilder();
        for (String question : constraint.keySet()) {
            n_questions = n_questions + 1;
            Set<String> answers = constraint.get(question);

            Set<String> expressions = new HashSet<>();
            for (String answer: answers) {
                String keyString = String.format(":answer_%s", n_answers);
                expressions.add(keyString);
                expressionAttributeValues.put(keyString, new AttributeValue().withS(answer));
                n_answers = n_answers + 1;
            }
            String attrKeyString = String.format("attr_%s", question);
            String attrKeySetString = String.format("(%s)", String.join(", ", expressions));
            conditionBuilder.append("(");
            conditionBuilder.append(attrKeyString);
            conditionBuilder.append(" IN ");
            conditionBuilder.append(attrKeySetString);
            conditionBuilder.append(")");
            if (n_questions < constraint.size()) {
                conditionBuilder.append(" AND ");
            }
        }

        /**
         * keyConditionExpression Format:
         *  - "(attr_question1 IN (:answer_0, :answer_1, ...)) AND (attr_question2 IN (:answer_3, ...)) AND ..."
         */
        String keyConditionExpression = conditionBuilder.toString();

        if (n_answers == 0) {
            return readWithoutConstraint();
        }

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(this.tableName)
                .withFilterExpression(keyConditionExpression)
                .withExpressionAttributeValues(expressionAttributeValues);
        ScanResult result = getClient().scan(scanRequest);

        Set<String> userList = new HashSet<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            String userId = String.valueOf(item.get("user_id"));
            userList.add(userId);
        }

        return userList;
    }

    /**
     *
     * @return all {@link Set<User>} existing in database.
     */
    @Override
    public Set<String> readWithoutConstraint() {
        ScanRequest scanRequest = new ScanRequest()
                .withTableName(this.tableName);
        ScanResult result = getClient().scan(scanRequest);

        Set<String> userList = new HashSet<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            String userId = String.valueOf(item.get("user_id"));
            userList.add(userId);
        }

        return userList;
    }

    private static Map<String, AttributeValue> fromSurveyResponse(String userID, SurveyResponse sr) {
        Map<String, AttributeValue> surveyResponse = new HashMap<>();

        surveyResponse.put("id", new AttributeValue().withS(sr.getID()));
        surveyResponse.put("survey_id", new AttributeValue().withS(sr.getSurvey().getID()));
        surveyResponse.put("user_id", new AttributeValue().withS(userID));

        Map<String, AttributeValue> questionResponseMap = new HashMap<>();
        for (SurveyQuestion question : sr.getSurvey().getQuestions()) {
            SurveyQuestionResponse response = sr.getResponse(question);
            questionResponseMap.put(question.getQuestion(), new AttributeValue().withN(response.getResponse()));
        }
        surveyResponse.put("responses", new AttributeValue().withM(questionResponseMap));

        return surveyResponse;
    }

    private static SurveyResponse surveyResponseFromDynamo(Map<String, AttributeValue> item) {
        String id = item.get("id").getS();
        String surveyID = item.get("survey_id").getS();
        String userID = item.get("user_id").getS();

        Survey survey = surveyStore.read(surveyID);

        Map<SurveyQuestion, SurveyQuestionResponse> responses = new HashMap<>();
        for (SurveyQuestion surveyQuestion : survey.getQuestions()) {
            String responseS = item.get("responses").getM().get(surveyQuestion.getQuestion()).getN();
            int response = Integer.parseInt(responseS);
            SurveyQuestionResponse questionResponse = new SurveyQuestionResponse(surveyQuestion, response);

            responses.put(surveyQuestion, questionResponse);
        }

        return new SurveyResponse(id, survey, responses);
    }
}
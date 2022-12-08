package com.javahelp.backend.data;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.javahelp.backend.data.search.constraint.IConstraint;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class DynamoDBSurveyResponseStore extends DynamoDBStore implements ISurveyResponseStore{
    private String tableName;
    private ISurveyStore surveyStore;

    DynamoDBSurveyResponseStore(String tableName, Regions region, ISurveyStore surveyStore) {
        super(region);
        this.tableName = tableName;
        this.surveyStore = surveyStore;
    }

    /**
     * Creates a {@link SurveyResponse}
     *
     * @param userID {@link String} id of author (user) of this surveyResponse
     * @param surveyResponse {@link SurveyResponse} to create
     * @return {@link SurveyResponse} that was created
     */
    @Override
    public SurveyResponse create(String userID, SurveyResponse surveyResponse, boolean isProvider) {
        String id = UUID.randomUUID().toString();
        surveyResponse.setID(id);

        SurveyResponse existingSr = readByUserAndSurvey(userID, surveyResponse.getSurvey().getID());
        if (existingSr != null) {
            surveyResponse.setID(existingSr.getID());
            update(userID, surveyResponse, isProvider);
        }
        else {
            Map<String, AttributeValue> item = fromSurveyResponse(userID, surveyResponse, isProvider);
            item.put("provider_survey", new AttributeValue().withBOOL(isProvider));
            PutItemRequest request = new PutItemRequest()
                    .withTableName(tableName)
                    .withItem(item);

            getClient().putItem(request);
        }
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

    @Override
    public void update(String userID, SurveyResponse surveyResponse, boolean isProvider) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue(surveyResponse.getID()));

        UpdateItemRequest request = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withUpdateExpression(getUpdateString())
                .withExpressionAttributeValues(updateFromSurveyResponse(userID, surveyResponse, isProvider));

        getClient().updateItem(request);
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
     *
     * @param userID {@link String} id of the user to be queried.
     * @return {@link List <SurveyResponse>} of the specified user.
     */
    @Override
    public List<SurveyResponse> readByUser(String userID) {
        String keyConditionExpression = "user_id = :user_id_val";
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":user_id_val", new AttributeValue().withS(userID));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(this.tableName)
                .withFilterExpression(keyConditionExpression)
                .withExpressionAttributeValues(expressionAttributeValues);

        ScanResult result = getClient().scan(scanRequest);

        List<SurveyResponse> surveyResponseList = new ArrayList<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            if (item != null) {
                SurveyResponse surveyResponse = surveyResponseFromDynamo(item);
                surveyResponseList.add(surveyResponse);
            }
        }

        return surveyResponseList;
    }

    /**
     * Obtains the survey response of a particular survey completed by a user.
     * Note that each user will only have one survey response of a particular survey.
     *
     * @param userID {@link String} id of the user to be queried.
     * @param surveyID {@link String} id of the specific survey completed by this user.
     * @return {@link SurveyResponse} of the specified user and survey, or null if no
     * such survey response is found.
     */
    public SurveyResponse readByUserAndSurvey(String userID, String surveyID) {
        String keyConditionExpression = "user_id = :id AND survey_id = :surveyId";
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":id", new AttributeValue().withS(userID));
        expressionAttributeValues.put(":surveyId", new AttributeValue().withS(surveyID));

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(this.tableName)
                .withFilterExpression(keyConditionExpression)
                .withExpressionAttributeValues(expressionAttributeValues);

        ScanResult result = getClient().scan(scanRequest);

        List<SurveyResponse> surveyResponseList = new ArrayList<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            if (item != null) {
                SurveyResponse surveyResponse = surveyResponseFromDynamo(item);
                surveyResponseList.add(surveyResponse);
            }
        }
        if (surveyResponseList.size() == 0) {
            return null;
        }
        else {
            return surveyResponseList.get(0);
        }
    }

    @Override
    public Map<String, SurveyResponse> readProviderByConstraint(IConstraint constraint) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":provider_survey_val", new AttributeValue().withBOOL(true));

        int n_attr = 1;
        StringBuilder conditionBuilder = new StringBuilder();
        for (String attr : constraint.getConstraints()) {
            String keyString = String.format(":attr_%s", n_attr);
            expressionAttributeValues.put(keyString, new AttributeValue().withS(attr));

            conditionBuilder.append(String.format("(contains(attributes, %s))", keyString));
            conditionBuilder.append(" AND ");

            n_attr = n_attr + 1;
        }
        conditionBuilder.append("provider_survey=:provider_survey_val");

        String keyConditionExpression = conditionBuilder.toString();

        if (constraint.size() == 0) {
            return readWithoutConstraint();
        }

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(this.tableName)
                .withFilterExpression(keyConditionExpression)
                .withExpressionAttributeValues(expressionAttributeValues);
        ScanResult result = getClient().scan(scanRequest);

        Map<String, SurveyResponse> usersAndSurveyR = new HashMap<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            if (item != null) {
                String userId = String.valueOf(item.get("user_id").getS());
                SurveyResponse sr = surveyResponseFromDynamo(item);
                usersAndSurveyR.put(userId, sr);
            }
        }

        return usersAndSurveyR;
    }

    /**
     *
     * @return {@link Map} of all provivders and their corresponding survey responses
     * existing in database.
     */
    @Override
    public Map<String, SurveyResponse> readWithoutConstraint() {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":provider_survey_val", new AttributeValue().withBOOL(true));
        String keyConditionExpression = "provider_survey=:provider_survey_val";

        ScanRequest scanRequest = new ScanRequest()
                .withTableName(this.tableName)
                .withFilterExpression(keyConditionExpression)
                .withExpressionAttributeValues(expressionAttributeValues);
        ScanResult result = getClient().scan(scanRequest);

        Map<String, SurveyResponse> usersAndSurveyR = new HashMap<>();
        for (Map<String, AttributeValue> item : result.getItems()) {
            if (item != null) {
                String userId = String.valueOf(item.get("user_id").getS());
                SurveyResponse sr = surveyResponseFromDynamo(item);
                usersAndSurveyR.put(userId, sr);
            }
        }

        return usersAndSurveyR;
    }

    /**
     * @param userID {@link String} id of specified user.
     * @param sr {@link SurveyResponse} survey response to be saved to database.
     * @return {@link Map} containing the representation
     */
    private static Map<String, AttributeValue> fromSurveyResponse(String userID, SurveyResponse sr, boolean isProvider) {
        Map<String, AttributeValue> surveyResponse = new HashMap<>();

        Survey survey = sr.getSurvey();
        String formattedSurveyID = survey.getID();

        surveyResponse.put("id", new AttributeValue().withS(sr.getID()));
        surveyResponse.put("survey_id", new AttributeValue().withS(formattedSurveyID));
        surveyResponse.put("user_id", new AttributeValue().withS(userID));
        surveyResponse.put("provider_survey", new AttributeValue().withBOOL(isProvider));

        Map<String, AttributeValue> questionResponseMap = new HashMap<>();
        for (int i = 0; i < survey.size(); i++) {
            SurveyQuestionResponse response = sr.getResponse(i);
            String formattedQuestion = String.join("_", survey.get(i).getQuestion().split(" "));
            questionResponseMap.put(formattedQuestion, new AttributeValue().withN(String.valueOf(response.getResponseNumber())));
        }

        surveyResponse.put("responses", new AttributeValue().withM(questionResponseMap));
        surveyResponse.put("attributes", new AttributeValue().withSS(sr.getAttributes()));

        return surveyResponse;
    }

    /**
     * @param item {@link Map} containing representation of {@link SurveyResponse}
     * @return {@link SurveyResponse} contained
     */
    private SurveyResponse surveyResponseFromDynamo(Map<String, AttributeValue> item) {
        String id = item.get("id").getS();
        String surveyID = item.get("survey_id").getS();
        String userID = item.get("user_id").getS();

        Survey survey = this.surveyStore.read(surveyID);

        Map<SurveyQuestion, SurveyQuestionResponse> responses = new HashMap<>();
        for (SurveyQuestion surveyQuestion : survey.getQuestions()) {
            String formattedQuestion = String.join("_", surveyQuestion.getQuestion().split(" "));
            String responseS = item.get("responses").getM().get(formattedQuestion).getN();
            int response = Integer.parseInt(responseS);
            SurveyQuestionResponse questionResponse = new SurveyQuestionResponse(surveyQuestion, response);

            responses.put(surveyQuestion, questionResponse);
        }

        return new SurveyResponse(id, survey, responses);
    }

    /**
     * @param userID {@link String} id of specified user.
     * @param sr {@link SurveyResponse} survey response to be saved to database.
     * @return {@link Map} containing the representation to be updated.
     */
    private static Map<String, AttributeValue> updateFromSurveyResponse(String userID, SurveyResponse sr, boolean isProvider) {
        Map<String, AttributeValue> surveyResponse = new HashMap<>();

        String formattedSurveyID = sr.getSurvey().getID();

        surveyResponse.put(":survey_id_val", new AttributeValue().withS(formattedSurveyID));
        surveyResponse.put(":user_id_val", new AttributeValue().withS(userID));
        surveyResponse.put(":provider_survey_val", new AttributeValue().withBOOL(isProvider));

        Map<String, AttributeValue> questionResponseMap = new HashMap<>();
        for (SurveyQuestion question : sr.getSurvey().getQuestions()) {
            SurveyQuestionResponse response = sr.getResponse(question);
            String formattedQuestion = String.join("_", question.getQuestion().split(" "));
            questionResponseMap.put(formattedQuestion, new AttributeValue().withN(String.valueOf(response.getResponseNumber())));
        }

        surveyResponse.put(":responses_val", new AttributeValue().withM(questionResponseMap));
        surveyResponse.put(":attributes_val", new AttributeValue().withSS(sr.getAttributes()));

        return surveyResponse;
    }

    /**
     * @return {@link String} string for updating existing entry in database.
     */
    private static String getUpdateString() {
        return "SET survey_id=:survey_id_val, " +
                "user_id=:user_id_val, " +
                "provider_survey=:provider_survey_val, " +
                "responses=:responses_val, " +
                "attributes=:attributes_val";
    }
}
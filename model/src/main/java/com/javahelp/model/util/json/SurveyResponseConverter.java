package com.javahelp.model.util.json;

import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;

import java.util.HashMap;
import java.util.Map;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class SurveyResponseConverter implements IJSONConverter<SurveyResponse> {

    private static final SurveyResponseConverter instance = new SurveyResponseConverter(SurveyConverter.getInstance(),
            SurveyQuestionConverter.getInstance(),
            SurveyQuestionResponseConverter.getInstance());

    /**
     * Private constructor
     *
     * @param s {@link IJSONConverter} for {@link Survey} to use
     * @param q {@link IJSONConverter} for {@link SurveyQuestion} to use
     * @param r {@link IJSONConverter} for {@link SurveyQuestionResponse} to use
     */
    private SurveyResponseConverter(IJSONConverter<Survey> s,
                                    IJSONConverter<SurveyQuestion> q,
                                    IJSONConverter<SurveyQuestionResponse> r) {
        surveyConverter = s;
        surveyQuestionConverter = q;
        surveyQuestionResponseConverter = r;
    }

    IJSONConverter<Survey> surveyConverter;
    IJSONConverter<SurveyQuestion> surveyQuestionConverter;
    IJSONConverter<SurveyQuestionResponse> surveyQuestionResponseConverter;

    /**
     * @return instance of {@link SurveyResponse}
     */
    public static SurveyResponseConverter getInstance() {
        return instance;
    }

    /**
     * Converts the provided object to a {@link JsonObject}
     *
     * @param input input object
     * @return {@link JsonObject} or null if conversion failed
     */
    @Override
    public JsonObject toJSON(SurveyResponse input) {
        JsonArrayBuilder jsonResponsesBuilder = Json.createArrayBuilder();
        for (int i = 0; i < input.size(); i++) {
            JsonObject responseObj = surveyQuestionResponseConverter.toJSON(input.getResponse(i));
            jsonResponsesBuilder.add(Json.createObjectBuilder()
                    .add(String.format("question_response_%s", i), responseObj));
        }

        return Json.createObjectBuilder()
                .add("response_id", input.getID())
                .add("survey", surveyConverter.toJSON(input.getSurvey()))
                .add("question_responses", jsonResponsesBuilder)
                .build();
    }

    /**
     * Converts a {@link JsonObject} to an output
     *
     * @param object {@link JsonObject} to convert
     * @return output object, or null if conversion failed
     */
    @Override
    public SurveyResponse fromJSON(JsonObject object) {
        if (object.containsKey("response_id")
                && object.containsKey("survey")
                && object.containsKey("question_responses")) {
            String id = object.getString("response_id");
            Survey survey = surveyConverter.fromJSON(object.getJsonObject("survey"));

            Map<SurveyQuestion, SurveyQuestionResponse> responses = new HashMap<>();
            JsonArray questionResponseArray = object.getJsonArray("question_responses");
            for (int i = 0; i < questionResponseArray.size(); i++) {
                SurveyQuestionResponse question_response = surveyQuestionResponseConverter.fromJSON(questionResponseArray
                        .getJsonObject(i)
                        .getJsonObject(String.format("question_response_%s", i)));
                // Note that SurveyQuestionResponse is implemented in a way that its SurveyResponses
                // are referenced using SurveyQuestions' pointer. Hence, it is important to ensure that
                // the SurveyQuestions have identical pointers to those in the Survey.
                SurveyQuestionResponse question_response_alt = new SurveyQuestionResponse(survey.get(i), question_response.getResponseNumber());
                responses.put(survey.get(i), question_response_alt);
            }

            return new SurveyResponse(id, survey, responses);
        }
        return null;
    }
}

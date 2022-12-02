package com.javahelp.model.util.json;

import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class SurveyQuestionResponseConverter implements IJSONConverter<SurveyQuestionResponse> {
    private static SurveyQuestionResponseConverter instance = new SurveyQuestionResponseConverter(SurveyQuestionConverter.getInstance());

    /**
     * Private constructor
     *
     * @param q {@link IJSONConverter} for {@link SurveyQuestionResponseConverter} to use
     */
    private SurveyQuestionResponseConverter(IJSONConverter<SurveyQuestion> q) {
        surveyQuestionConverter = q;
    }

    IJSONConverter<SurveyQuestion> surveyQuestionConverter;

    /**
     * @return instance of {@link SurveyQuestionResponseConverter}
     */
    public static SurveyQuestionResponseConverter getInstance() {
        return instance;
    }

    /**
     * Converts the provided object to a {@link JsonObject}
     *
     * @param input input object
     * @return {@link JsonObject} or null if conversion failed
     */
    @Override
    public JsonObject toJSON(SurveyQuestionResponse input) {
        JsonObject questionObj = surveyQuestionConverter.toJSON(input.getQuestion());
        return Json.createObjectBuilder()
                .add("question", questionObj)
                .add("response", input.getResponseNumber())
                .build();
    }

    /**
     * Converts a {@link JsonObject} to an output
     *
     * @param object {@link JsonObject} to convert
     * @return output object, or null if conversion failed
     */
    @Override
    public SurveyQuestionResponse fromJSON(JsonObject object) {
        if (object.containsKey("question")
                && object.containsKey("response")) {
            SurveyQuestion question = surveyQuestionConverter.fromJSON(object.getJsonObject("question"));
            int response = object.getInt("response");
            return new SurveyQuestionResponse(question, response);
        }
        return null;
    }
}

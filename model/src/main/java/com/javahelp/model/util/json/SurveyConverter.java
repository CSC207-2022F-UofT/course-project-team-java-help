package com.javahelp.model.util.json;

import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.user.UserInfo;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

/**
 * Converts {@link Survey} to {@link JsonObject}
 */
public class SurveyConverter implements IJSONConverter<Survey> {

    private static SurveyConverter instance = new SurveyConverter(SurveyQuestionConverter.getInstance());

    /**
     * Private constructor
     *
     * @param q {@link IJSONConverter} for {@link SurveyQuestion} to use
     */
    private SurveyConverter(IJSONConverter<SurveyQuestion> q) {
        surveyQuestionConverter = q;
    }

    IJSONConverter<SurveyQuestion> surveyQuestionConverter;

    /**
     * @return instance of {@link SurveyConverter}
     */
    public static SurveyConverter getInstance() {
        return instance;
    }

    /**
     * Converts the provided object to a {@link JsonObject}
     *
     * @param input input object
     * @return {@link JsonObject} or null if conversion failed
     */
    @Override
    public JsonObject toJSON(Survey input) {
        JsonArrayBuilder jsonQuestionsBuilder = Json.createArrayBuilder();
        int n = 0;
        for (SurveyQuestion question : input.getQuestions()) {
            JsonObject questionObj = surveyQuestionConverter.toJSON(question);
            jsonQuestionsBuilder.add(Json.createObjectBuilder()
                    .add(String.format("question_%s", n), questionObj));
            n = n + 1;
        }
        return Json.createObjectBuilder()
                .add("survey_id", input.getID())
                .add("survey_name", input.getName())
                .add("questions", jsonQuestionsBuilder)
                .build();
    }

    /**
     * Converts a {@link JsonObject} to an output
     *
     * @param object {@link JsonObject} to convert
     * @return output object, or null if conversion failed
     */
    @Override
    public Survey fromJSON(JsonObject object) {
        if (object.containsKey("survey_id")
                && object.containsKey("survey_name")
                && object.containsKey("questions")) {
            String id = object.getString("survey_id");
            String name = object.getString("survey_name");
            List<SurveyQuestion> questions = new ArrayList<>();
            JsonArray questionArray = object.getJsonArray("questions");
            for (int i = 0; i < questionArray.size(); i++) {
                SurveyQuestion question = surveyQuestionConverter.fromJSON(questionArray
                        .getJsonObject(i)
                        .getJsonObject(String.format("question_%s", i)));
                questions.add(question);
            }
            return new Survey(id, name, questions);
        }
        return null;
    }
}

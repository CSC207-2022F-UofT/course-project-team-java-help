package com.javahelp.model.util.json;

import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;

import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

/**
 * Converts {@link SurveyQuestion} to {@link JsonObject}
 */
public class SurveyQuestionConverter implements IJSONConverter<SurveyQuestion> {
    private static SurveyQuestionConverter instance = new SurveyQuestionConverter();

    /**
     * Private constructor
     */
    private SurveyQuestionConverter() {}

    /**
     * @return instance of {@link SurveyQuestionConverter}
     */
    public static SurveyQuestionConverter getInstance() { return instance; }

    /**
     * Converts the provided object to a {@link JsonObject}
     *
     * @param input input object
     * @return {@link JsonObject} or null if conversion failed
     */
    @Override
    public JsonObject toJSON(SurveyQuestion input) {
        JsonArrayBuilder jsonAnswerBuilder = Json.createArrayBuilder();
        int n = 0;
        for (String answer : input.getAnswers()) {
            jsonAnswerBuilder.add(Json.createObjectBuilder()
                    .add(String.format("answer_%s", n), answer));
            n = n + 1;
        }
        return Json.createObjectBuilder()
                .add("question", input.getQuestion())
                .add("answers", jsonAnswerBuilder)
                .build();
    }

    /**
     * Converts a {@link JsonObject} to an output
     *
     * @param object {@link JsonObject} to convert
     * @return output object, or null if conversion failed
     */
    @Override
    public SurveyQuestion fromJSON(JsonObject object) {
        if (object.containsKey("question")
                && object.containsKey("answers")) {
            String question = object.getString("question");
            List<String> answers = new ArrayList<>();
            JsonArray answerArray = object.getJsonArray("answers");
            for (int i = 0; i < answerArray.size(); i++) {
                String answer = answerArray.getJsonObject(i).getString(String.format("answer_%s", i));
                answers.add(answer);
            }
            return new SurveyQuestion(question, answers);
        }
        return null;
    }
}

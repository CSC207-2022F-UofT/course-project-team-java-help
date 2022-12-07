package com.javahelp.model.util.json;

import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        JsonArrayBuilder jsonAttrBuilder = Json.createArrayBuilder();
        int n = 0;
        for (String answer : input.getAnswers()) {
            jsonAnswerBuilder.add(Json.createObjectBuilder()
                    .add(String.format("answer_%s", n), answer));
            JsonArrayBuilder jsonSubAttrBuilder = Json.createArrayBuilder();
            int i = 0;
            for (String subAttr : input.getAnswerAttribute(n)) {
                jsonSubAttrBuilder.add(Json.createObjectBuilder()
                        .add(String.format("subAttr_%s", i), subAttr));
                i = i + 1;
            }
            jsonAttrBuilder.add(jsonSubAttrBuilder);
            n = n + 1;
        }
        return Json.createObjectBuilder()
                .add("question", input.getQuestion())
                .add("answers", jsonAnswerBuilder)
                .add("attributes", jsonAttrBuilder)
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
                && object.containsKey("answers")
                && object.containsKey("attributes")) {
            String question = object.getString("question");
            List<String> answers = new ArrayList<>();
            List<Set<String>> attributes = new ArrayList<>();
            JsonArray answerArray = object.getJsonArray("answers");
            JsonArray attrArray = object.getJsonArray("attributes");
            for (int i = 0; i < answerArray.size(); i++) {
                String answer = answerArray.getJsonObject(i).getString(String.format("answer_%s", i));
                answers.add(answer);

                Set<String> subAttributes = new HashSet<>();
                JsonArray subAttrArray = attrArray.getJsonArray(i);
                for (int j = 0; j < subAttrArray.size(); j++) {
                    subAttributes.add(subAttrArray.getJsonObject(j).getString(String.format("subAttr_%s", j)));
                }
                attributes.add(subAttributes);
            }
            SurveyQuestion surveyQuestion = new SurveyQuestion(question, answers);
            for (int k = 0; k < attributes.size(); k++) {
                surveyQuestion.setAnswerAttribute(k, attributes.get(k));
            }
            return surveyQuestion;
        }
        return null;
    }
}

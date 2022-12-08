package com.javahelp.backend.data.search;

import com.javahelp.backend.data.ISurveyResponseStore;
import com.javahelp.backend.data.ISurveyStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.survey.Survey;
import com.javahelp.model.survey.SurveyQuestion;
import com.javahelp.model.survey.SurveyQuestionResponse;
import com.javahelp.model.survey.SurveyResponse;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Populates a single client user, several provider users,
 * a survey, and several survey responses with random information for testing
 */
public class RandomSurveyPopulation {
    private final int populationSize;
    private final int surveySize;

    private Survey survey;
    private User randomClient;
    private SurveyResponse randomClientResponse;
    private List<User> randomProviders;
    private List<SurveyResponse> randomProviderResponses;

    private final ISurveyStore surveyDB;
    private final ISurveyResponseStore srDB;
    private final IUserStore userDB;

    /**
     * Creates a new {@link RandomSurveyPopulation}
     *
     * @param populationSize population size to use
     * @param surveySize     survey size to use
     * @param surveys        {@link ISurveyStore} to use
     * @param responses      {@link ISurveyResponseStore} to use
     * @param users          {@link IUserStore} to use
     */
    public RandomSurveyPopulation(int populationSize, int surveySize, ISurveyStore surveys,
                                  ISurveyResponseStore responses, IUserStore users) {
        this.populationSize = populationSize;
        this.surveySize = surveySize;
        this.surveyDB = surveys;
        this.srDB = responses;
        this.userDB = users;
    }

    /**
     * Creates a new {@link RandomSurveyPopulation}
     *
     * @param surveys   the {@link ISurveyStore} to use
     * @param responses the {@link ISurveyResponseStore} to use
     * @param users     the {@link IUserStore} to use
     */
    public RandomSurveyPopulation(ISurveyStore surveys, ISurveyResponseStore responses,
                                  IUserStore users) {
        this(10, 10, surveys, responses, users);
    }

    /**
     * Populates tables with randomly generated users
     */
    public void populate() {

        survey = setupSurvey();
        surveyDB.create(survey);

        UserPassword p = randomUserPassword();
        randomClient = generateRandomClient();
        userDB.create(randomClient, p);

        randomClientResponse = generateRandomSurveyResponse(setupSurvey());
        srDB.create(randomClient.getStringID(), randomClientResponse, false);

        randomProviders = new ArrayList<>();
        randomProviderResponses = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            p = randomUserPassword();
            User user = generateRandomProvider();
            SurveyResponse response = generateRandomSurveyResponse(survey);

            userDB.create(user, p);
            srDB.create(user.getStringID(), response, true);

            randomProviders.add(user);
            randomProviderResponses.add(response);
        }
    }

    /**
     * @return the created {@link Survey}
     */
    public Survey getSurvey() {
        return survey;
    }

    /**
     * @return the created client {@link User}
     */
    public User getRandomClient() {
        return randomClient;
    }

    /**
     * @return the response to the {@link Survey} by the created client {@link User}
     */
    public SurveyResponse getRandomClientResponse() {
        return randomClientResponse;
    }

    /**
     * @return {@link List} of created provider {@link User}s
     */
    public List<User> getRandomProviders() {
        return randomProviders;
    }

    /**
     * @return the {@link List} of {@link SurveyResponse}s filled out by providers
     */
    public List<SurveyResponse> getRandomResponses() {
        return randomProviderResponses;
    }

    /**
     * @return the size of the population of provider {@link User}s created by this
     * {@link RandomSurveyPopulation}
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * @return the size of the {@link Survey} created by this {@link RandomSurveyPopulation}
     */
    public int getSurveySize() {
        return surveySize;
    }

    /**
     * Deletes the {@link User}'s randomly added by this {@link RandomSurveyPopulation}
     */
    public void delete() {
        surveyDB.delete(survey.getID());
        userDB.delete(randomClient.getStringID());
        for (int i = 0; i < randomProviders.size(); i++) {
            userDB.delete(randomProviders.get(i).getStringID());
            srDB.delete(randomProviderResponses.get(i).getID());
        }
    }

    /**
     * @return a random new client {@link User}
     */
    private User generateRandomClient() {
        Random random = new Random();
        int userID = random.nextInt(100);
        int phoneNumber = random.nextInt(9999);
        ClientUserInfo clientInfo = new ClientUserInfo(
                String.format("test.client.%s@mail.com", userID),
                String.format("%s Client road", userID),
                String.format("+1-647-674-%04d", phoneNumber),
                String.format("Number %s", userID),
                "Client");

        return new User("", clientInfo, "test_client_" + userID);
    }

    /**
     * @return a random new provider {@link User}
     */
    private User generateRandomProvider() {
        Random random = new Random();
        int userID = random.nextInt(100);
        int phoneNumber = random.nextInt(9999);
        ProviderUserInfo providerInfo = new ProviderUserInfo(
                String.format("test.client.%s@mail.com", userID),
                String.format("%s Client road", userID),
                String.format("+1-647-674-%04d", phoneNumber),
                String.format("Number %s Provider", userID)
        );

        return new User("", providerInfo, "test_provider_" + userID);
    }

    /**
     * Generates a random {@link SurveyResponse} for the provided {@link Survey}
     *
     * @param survey the {@link Survey} to respond to
     * @return {@link SurveyResponse} for the specified {@link Survey}
     */
    private SurveyResponse generateRandomSurveyResponse(Survey survey) {
        Random random = new Random();
        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();
        for (int i = 0; i < survey.size(); i++) {
            int randAnswer = random.nextInt(4);
            SurveyQuestionResponse response = new SurveyQuestionResponse(survey.get(i), randAnswer);
            map.put(survey.get(i), response);
        }

        return new SurveyResponse("", survey, map);
    }

    /**
     * @return a test {@link Survey}
     */
    private Survey setupSurvey() {
        List<String> responses = new ArrayList<>();
        responses.add("This is the first response");
        responses.add("This is the second response");
        responses.add("This is the third response");
        responses.add("This is the fourth response");

        List<SurveyQuestion> questions = new ArrayList<>();
        for (int i = 0; i < surveySize; i++) {
            SurveyQuestion question = new SurveyQuestion(String.format("Question %s", i), responses);
            question.setAnswerAttribute(0, "attr0");
            question.setAnswerAttribute(1, "attr1");
            question.setAnswerAttribute(2, "attr2");
            question.setAnswerAttribute(3, "attr3");

            questions.add(question);
        }
        return new Survey("", "Test Survey", questions);
    }

    /**
     * @return A randomly generated {@link UserPassword}
     */
    private static UserPassword randomUserPassword() {
        Random random = new Random();
        int saltLength = 1 + random.nextInt(31);
        int hashLength = 1 + random.nextInt(31);

        byte[] salt = new byte[saltLength];
        byte[] hash = new byte[hashLength];

        random.nextBytes(salt);
        random.nextBytes(hash);

        return new UserPassword(salt, hash);
    }
}

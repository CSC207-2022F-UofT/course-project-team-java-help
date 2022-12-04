package com.javahelp.backend.search;

import static org.junit.Assume.assumeTrue;

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

public class RandomDataPopulater {
    private final int N_POPULATION = 10;
    private final int N_SURVEY_QUESTIONS = 10;

    private Survey survey;
    private final User randomClient;
    private final SurveyResponse randomClientResponses;
    private final List<User> randomProviders;
    private final List<SurveyResponse> randomProviderResponses;

    private ISurveyStore surveyDB = ISurveyStore.getDefaultImplementation();
    private ISurveyResponseStore srDB = ISurveyResponseStore.getDefaultImplementation();
    private IUserStore userDB = IUserStore.getDefaultImplementation();

    public RandomDataPopulater(boolean cleanTables) {
        assumeTrue(userDatabaseAccessible());
        assumeTrue(srDatabaseAccessible());
        assumeTrue(surveyDatabaseAccessible());

        if (cleanTables) {
            this.surveyDB.cleanTable();
            this.srDB.cleanTable();
            this.userDB.cleanTable();
        }

        this.survey = setupSurvey();
        this.survey = this.surveyDB.create(survey);

        UserPassword p = randomUserPassword();
        User client = generateRandomClient();
        this.randomClient = this.userDB.create(client, p);
        SurveyResponse sr = generateRandomSurveyResponse(setupSurvey());
        this.randomClientResponses = this.srDB.create(client.getStringID(), sr, false);

        this.randomProviders = new ArrayList<>();
        this.randomProviderResponses = new ArrayList<>();

        for (int i = 0; i < N_POPULATION; i++) {
            p = randomUserPassword();
            User user = generateRandomProvider();
            SurveyResponse response = generateRandomSurveyResponse(this.survey);

            user = this.userDB.create(user, p);
            response = this.srDB.create(user.getStringID(), response, true);

            this.randomProviders.add(user);
            this.randomProviderResponses.add(response);
        }
    }

    public Survey getSurvey() { return this.survey; }

    public User getRandomClient() { return this.randomClient; }

    public List<User> getRandomProviders() { return this.randomProviders; }

    public List<SurveyResponse> getRandomResponses() { return this.randomProviderResponses; }

    public IUserStore getUserDB() { return this.userDB; }

    public ISurveyStore getSurveyDB() { return this.surveyDB; }

    public ISurveyResponseStore getSrDB() { return this.srDB; }

    public int getPopulationNumber() { return this.N_POPULATION; }

    public int getQuestionNumber() { return this.N_SURVEY_QUESTIONS; }

    public void deleteRandomPopulation() {
        this.surveyDB.delete(this.survey.getID());
        this.userDB.delete(this.randomClient.getStringID());
        for (int i = 0; i < this.randomProviders.size(); i++) {
            this.userDB.delete(this.randomProviders.get(i).getStringID());
            this.srDB.delete(this.randomProviderResponses.get(i).getID());
        }
    }

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

        return new User(String.format("test_client_%s", userID),
                clientInfo,
                String.format("test_client_%s", userID));
    }

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

        return new User(String.format("test_provider_%s", userID),
                providerInfo,
                String.format("test_provider_%s", userID));
    }

    private SurveyResponse generateRandomSurveyResponse(Survey survey) {
        Random random = new Random();
        Map<SurveyQuestion, SurveyQuestionResponse> map = new HashMap<>();
        for (int i = 0; i < survey.size(); i++) {
            int randAnswer = random.nextInt(4);
            SurveyQuestionResponse response = new SurveyQuestionResponse(survey.get(i), randAnswer);
            map.put(survey.get(i), response);
        }

        int randResponseID = random.nextInt(999);
        return new SurveyResponse(String.format("response_%s", randResponseID), survey, map);
    }

    private Survey setupSurvey() {
        List<String> responses = new ArrayList<>();
        responses.add("This is the first response");
        responses.add("This is the second response");
        responses.add("This is the third response");
        responses.add("This is the fourth response");

        List<SurveyQuestion> questions = new ArrayList<>();
        for (int i = 0; i < N_SURVEY_QUESTIONS; i++) {
            SurveyQuestion question = new SurveyQuestion(String.format("Question %s", i), responses);
            question.setAnswerAttribute(0, String.format("attr%s_0", i));
            question.setAnswerAttribute(1, String.format("attr%s_1", i));
            question.setAnswerAttribute(2, String.format("attr%s_2", i));
            question.setAnswerAttribute(3, String.format("attr%s_3", i));

            questions.add(question);
        }
        return new Survey("survey_v1", "Test Survey", questions);
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

    private boolean userDatabaseAccessible() {
        try {
            this.userDB.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean surveyDatabaseAccessible() {
        try {
            this.surveyDB.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean srDatabaseAccessible() {
        try {
            this.srDB.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

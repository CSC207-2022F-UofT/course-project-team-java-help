package com.javahelp.backend.rank;

import static org.junit.Assert.assertEquals;

import com.amazonaws.regions.Regions;
import com.javahelp.backend.data.DynamoDBUserStore;
import com.javahelp.backend.query.Constraint;
import com.javahelp.backend.query.UserQueryConstraint;
import com.javahelp.backend.query.VanillaConstraint;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

import java.security.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ProviderRankerTest {
    String tableName = "javahelpBackendUsers";
    Regions regions = Regions.US_EAST_1;
    DynamoDBUserStore db = new DynamoDBUserStore(tableName, regions);

    /**
    @Test
    public void testProviderRanker() {
        db.cleanTable();
        User client = createTestClient1();
        List<User> providersList = createTestProviders1();
        addUsersToDB(providersList);

        Constraint constraint = new VanillaConstraint("Specialty");
        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(db);
        List<Constraint> constraintList = new ArrayList<>();
        constraintList.add(constraint);
        Set<User> providers = userQueryConstraint.getProvidersWithConstraints(constraintList);

        SimilarityScorer similarityScorer = new VanillaScorer();
        ProviderRanker providerRanker = new ProviderRanker(client, similarityScorer);
        List<User> rankedProviderList = providerRanker.rank(providers);

        List<String> expectedSortedProvidersList = new ArrayList<>();
        for (User user : providersList) {
            expectedSortedProvidersList.add(user.getStringID());
        }
        List<String> actualSortedProvidersList = new ArrayList<>();
        for (User user : rankedProviderList) {
            actualSortedProvidersList.add(user.getStringID());
        }

        assertEquals(expectedSortedProvidersList, actualSortedProvidersList);

        this.db.delete(client.getStringID());
        deleteUsersInDB(providersList);
    }

    @Test
    public void testRulseBasedProviderRanker() {
        db.cleanTable();
        User client = createTestClient2();
        List<User> providersList = createTestProviders2();
        addUsersToDB(providersList);

        Constraint constraint = new VanillaConstraint("Specialty");
        List<Constraint> constraintList = new ArrayList<>();
        constraintList.add(constraint);
        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(db);
        Set<User> providers = userQueryConstraint.getProvidersWithConstraints(constraintList);

        RuleBasedScorer similarityScorer = new RuleBasedScorer();
        List<String> rule1 = new ArrayList<>();
        rule1.add("Specialty-Counselling");
        rule1.add("Education-Psychology");

        similarityScorer.setRules("Symptom-Anxiety", rule1);
        ProviderRanker providerRanker = new ProviderRanker(client, similarityScorer);
        List<User> rankedProviderList = providerRanker.rank(providers);

        List<String> expectedSortedProvidersList = new ArrayList<>();
        for (User user : providersList) {
            expectedSortedProvidersList.add(user.getStringID());
        }
        List<String> actualSortedProvidersList = new ArrayList<>();
        for (User user : rankedProviderList) {
            actualSortedProvidersList.add(user.getStringID());
        }

        assertEquals(expectedSortedProvidersList, actualSortedProvidersList);

        this.db.delete(client.getStringID());
        deleteUsersInDB(providersList);
    }

    private void addUsersToDB(List<User> userList) {
        UserPassword p = randomUserPassword();
        for (User user : userList) {
            this.db.create(user, p);
        }
    }

    private void deleteUsersInDB(List<User> userList) {
        for (User user : userList) {
            this.db.delete(user.getStringID());
        }
    }

    private User createTestClient1() {
        ClientUserInfo clientInfo = new ClientUserInfo("uoft@utoronto.ca",
                "University of Toronto", "000-123-4567",
                "Johnny", "Meng");

        clientInfo.setAttribute("Age", "35");
        clientInfo.setAttribute("Symptoms", "Pain");
        clientInfo.setAttribute("Location", "US");
        clientInfo.setAttribute("Language", "English");

        User client = new User("client1", clientInfo, "test_client_1");

        return client;
    }

    private User createTestClient2() {
        ClientUserInfo clientInfo = new ClientUserInfo("uoft@utoronto.ca",
                "University of Toronto", "000-123-4567",
                "Johnny", "Meng");

        clientInfo.setAttribute("Age", "35");
        clientInfo.setAttribute("Symptoms", "Anxiety");
        clientInfo.setAttribute("Location", "US");
        clientInfo.setAttribute("Language", "English");

        User client = new User("client1", clientInfo, "test_client_1");

        return client;
    }

    private List<User> createTestProviders1() {
        ProviderUserInfo providerInfo1 = new ProviderUserInfo(
                "johndoe@gmail.com",
                "123 provider road",
                "6667771052",
                "John");
        providerInfo1.setAttribute("Age", "35");
        providerInfo1.setAttribute("Specialty", "Pain");
        providerInfo1.setAttribute("Location", "US");
        providerInfo1.setAttribute("Language", "English");

        ProviderUserInfo providerInfo2 = new ProviderUserInfo(
                "jenifer@gmail.com",
                "321 provider road",
                "7776661052",
                "Jenifer");
        providerInfo2.setAttribute("Age", "55");
        providerInfo2.setAttribute("Specialty", "Extreme Pain");
        providerInfo2.setAttribute("Location", "US");
        providerInfo2.setAttribute("Language", "French");

        ProviderUserInfo providerInfo3 = new ProviderUserInfo(
                "jackychan@gmail.com",
                "999 provider road",
                "1234567890",
                "Jacky");
        providerInfo3.setAttribute("Age", "40");
        providerInfo3.setAttribute("Specialty", "Extreme Pain");
        providerInfo3.setAttribute("Location", "US");
        providerInfo3.setAttribute("Language", "English");

        User u1 = new User("provider1", providerInfo1, "test_provider_1");
        User u2 = new User("provider2", providerInfo2, "test_provider_2");
        User u3 = new User("provider3", providerInfo3, "test_provider_3");

        List<User> userList = new ArrayList<>();
        userList.add(u1);
        userList.add(u3);
        userList.add(u2);

        return userList;
    }

    private List<User> createTestProviders2() {
        ProviderUserInfo providerInfo1 = new ProviderUserInfo(
                "johndoe@gmail.com",
                "123 provider road",
                "6667771052",
                "John");
        providerInfo1.setAttribute("Age", "35");
        providerInfo1.setAttribute("Specialty", "Counselling");
        providerInfo1.setAttribute("Education", "Psychology");
        providerInfo1.setAttribute("Language", "English");

        ProviderUserInfo providerInfo2 = new ProviderUserInfo(
                "jenifer@gmail.com",
                "321 provider road",
                "7776661052",
                "Jenifer");
        providerInfo2.setAttribute("Age", "55");
        providerInfo2.setAttribute("Specialty", "Pain");
        providerInfo2.setAttribute("Education", "Mathematics");
        providerInfo2.setAttribute("Language", "French");

        ProviderUserInfo providerInfo3 = new ProviderUserInfo(
                "jackychan@gmail.com",
                "999 provider road",
                "1234567890",
                "Jacky");
        providerInfo3.setAttribute("Age", "40");
        providerInfo3.setAttribute("Specialty", "Counselling");
        providerInfo3.setAttribute("Education", "Neuroscience");
        providerInfo3.setAttribute("Language", "English");

        User u1 = new User("provider1", providerInfo1, "test_provider_1");
        User u2 = new User("provider2", providerInfo2, "test_provider_2");
        User u3 = new User("provider3", providerInfo3, "test_provider_3");

        List<User> userList = new ArrayList<>();
        userList.add(u1);
        userList.add(u3);
        userList.add(u2);

        return userList;
    }
    */

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

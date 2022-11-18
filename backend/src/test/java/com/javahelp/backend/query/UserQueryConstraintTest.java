package com.javahelp.backend.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import com.amazonaws.regions.Regions;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import com.javahelp.backend.data.DynamoDBUserStore;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class UserQueryConstraintTest {
    String tableName = "javahelpBackendUsers";
    Regions regions = Regions.US_EAST_1;
    DynamoDBUserStore db = new DynamoDBUserStore(tableName, regions);

    @Test
    public void testQuerySingleConstraint() {
        UserPassword p = randomUserPassword();
        ProviderUserInfo providerInfo1 = new ProviderUserInfo(
                "johndoe@gmail.com",
                "123 provider road",
                "6667771052",
                "John");
        providerInfo1.setAttribute("Age", 35);
        providerInfo1.setAttribute("Specialty", 1);

        ProviderUserInfo providerInfo2 = new ProviderUserInfo(
                "jenifer@gmail.com",
                "321 provider road",
                "7776661052",
                "Jenifer");
        providerInfo2.setAttribute("Age", 55);
        providerInfo2.setAttribute("Specialty", 2);

        User u1 = new User("test1", providerInfo1, "test_user_1");
        User u2 = new User("test2", providerInfo2, "test_user_2");

        db.create(u1, p);
        db.create(u2, p);

        Constraint constraint = new VanillaConstraint("Specialty", 2);
        UserQueryConstraint userQueryConstraint = new UserQueryConstraint(db);
        Set<User> providers = userQueryConstraint.getProvidersWithConstraint(constraint);

        Set<String> expectedProvidersSet = new HashSet<>();
        expectedProvidersSet.add(u2.getStringID());
        Set<String> actualProvidersSet = new HashSet<>();
        for (User user : providers) {
            actualProvidersSet.add(user.getStringID());
        }

        assertEquals(expectedProvidersSet, actualProvidersSet);

        db.delete(u1.getStringID());
        db.delete(u2.getStringID());
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

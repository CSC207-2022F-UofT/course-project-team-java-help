package com.javahelp.backend.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;

import com.amazonaws.regions.Regions;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

/**
 * Test suite for {@link DynamoDBUserStore}
 */
public class DynamoDBUserStoreTest {

    String tableName = "javahelpBackendUsers";

    Regions regions = Regions.US_EAST_1;

    DynamoDBUserStore db = new DynamoDBUserStore(tableName, regions);

    /**
     * @return whether the database is accessible from the current process
     */
    public boolean databaseAccessible() {
        try {
            db.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test(timeout = 5000)
    public void testCreateRead() {
        assumeTrue(databaseAccessible());

        UserPassword p = randomUserPassword();
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");
        User u = new User("test", clientInfo, "test_user");

        try {
            db.create(u, p);

            assertNotEquals("test", u.getStringID());

            User read = db.read(u.getStringID());

            assertEquals(u.getStringID(), read.getStringID());
            assertEquals(u.getUsername(), read.getUsername());
            assertEquals(u.getUserInfo().getType(), read.getUserInfo().getType());
            ClientUserInfo base = (ClientUserInfo) u.getUserInfo(),
                    readUserInfo = (ClientUserInfo) read.getUserInfo();

            assertEquals(base.getEmailAddress(), readUserInfo.getEmailAddress());
            assertEquals(base.getAddress(), readUserInfo.getAddress());
            assertEquals(base.getFirstName(), readUserInfo.getFirstName());
            assertEquals(base.getLastName(), readUserInfo.getLastName());
        } finally {
            db.delete(u.getStringID());
        }
    }

    @Test(timeout = 5000)
    public void testUpdate() {
        assumeTrue(databaseAccessible());

        UserPassword p = randomUserPassword();
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");
        User u = new User("test", clientInfo, "test_user");

        try {
            db.create(u, p);

            ClientUserInfo c = (ClientUserInfo) u.getUserInfo();
            c.setFirstName("john");

            u.setUsername("boo!");

            db.update(u);

            u = db.read(u.getStringID());

            assertEquals("boo!", u.getUsername());

            c = (ClientUserInfo) u.getUserInfo();

            assertEquals("john", c.getFirstName());
        } finally {
            db.delete(u.getStringID());
        }
    }

    @Test(timeout = 5000)
    public void testDelete() {
        assumeTrue(databaseAccessible());

        UserPassword p = randomUserPassword();
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");
        User u = new User("test", clientInfo, "test_user");

        try {
            db.create(u, p);

            User read = db.read(u.getStringID());

            assertNotNull(read);

            db.delete(u.getStringID());
        } finally {
            User deleted = db.read(u.getStringID());

            assertNull(deleted);
        }
    }

    @Test(timeout = 5000)
    public void testPasswordUpdateRead() {
        assumeTrue(databaseAccessible());

        UserPassword p = randomUserPassword();
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");
        User u = new User("test", clientInfo, "test_user");

        try {
            db.create(u, p);

            UserPassword read = db.readPassword(u.getStringID());

            assertEquals(read.getBase64SaltHash(), p.getBase64SaltHash());

            UserPassword newPassword = randomUserPassword();

            db.updatePassword(u.getStringID(), newPassword);

            read = db.readPassword(u.getStringID());

            assertEquals(newPassword.getBase64SaltHash(), read.getBase64SaltHash());
        } finally {
            db.delete(u.getStringID());
        }
    }

    @Test(timeout = 5000)
    public void testReadByEmail() {
        assumeTrue(databaseAccessible());

        UserPassword p = randomUserPassword();
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");
        User u = new User("test", clientInfo, "test_user");

        User read = u;

        try {
            db.create(u, p);

            read = db.readByEmail("test.client@mail.com");

            assertEquals(u.getStringID(), read.getStringID());
        } finally {
            db.delete(read.getStringID());
        }
    }

    @Test(timeout = 5000)
    public void testReadByUsername() {
        assumeTrue(databaseAccessible());

        UserPassword p = randomUserPassword();
        ClientUserInfo clientInfo = new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald");
        User u = new User("test", clientInfo, "test_user");

        User read = u;

        try {
            db.create(u, p);

            read = db.readByUsername("test_user");

            assertEquals(u.getStringID(), read.getStringID());
        } finally {
            db.delete(read.getStringID());

        }
    }

    @Test
    public void testMultiRead() {
        assumeTrue(databaseAccessible());

        UserPassword p1 = randomUserPassword();
        UserPassword p2 = randomUserPassword();

        User u1 = new User("", new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald"), "first_user");

        User u2 = new User("", new ClientUserInfo(
                "test.client@mail.com",
                "123  client road",
                "289375034875093",
                "Erin",
                "McDonald"), "second_user");

        Map<String, User> users = null;

        try {
            db.create(u1, p1);
            db.create(u2, p2);

            users = db.read(u1.getStringID(), u2.getStringID());

            assertEquals(u1.getUsername(), users.get(u1.getStringID()).getUsername());
            assertEquals(u2.getUsername(), users.get(u2.getStringID()).getUsername());
        } finally {
            db.delete(u1.getStringID());
            db.delete(u2.getStringID());
        }
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

package com.javahelp.backend.domain.user.delete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class DeleteManagerTest {
    IUserStore userStore;
    User user;

    ClientUserInfo clientUserInfo;

    DeleteInteractor deleteManager;

    byte[] salt;
    byte[] password;
    UserPassword userPassword;

    /**
     *
     * @return whether the database is accessible from the current process
     */
    public boolean databaseAccessible() {
        try {
            userStore.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Before
    public void setUp() {

        // Setting up user, userStore, and deleteManager.
        userStore = IUserStore.getDefaultImplementation();
        clientUserInfo = new ClientUserInfo("uoft@uoft.ca", "University of Toronto",
                "123-456-7890", "J", "M");
        user = new User("123456QWERTY", clientUserInfo, "cs207");

        deleteManager = new DeleteInteractor(userStore);

        // Same setup as in UserPasswordTest
        Random rand = new Random();
        byte[] salt = {5, 6, 8, 2, 6, 4, 1};
        byte[] password = {5, 6, 1, 8, 5, 6, 'a', '!', 6, 5};
        this.salt = salt;
        this.password = password;

        rand.nextBytes(salt);
        rand.nextBytes(password);

        this.userPassword = new UserPassword(salt, password);
    }

    @Test
    public void testDelete() {
        assumeTrue(databaseAccessible());

        IDeleteInputBoundary input = null;

        try {
            userStore.create(user, userPassword);
            assertNotNull(userStore.read(user.getStringID()));

            input = new IDeleteInputBoundary() {
                final String userID = user.getStringID();

                @Override
                public String getStringID() {
                    return userID;
                }
            };

        } finally {
            DeleteResult deleteResult1 = deleteManager.delete(input);
            assertNotNull(deleteResult1.getUser());
            assertNull(deleteResult1.getErrorMessage());
            assertTrue(deleteResult1.isSuccess());

            // Tries to delete again (expected failure).
            DeleteResult deleteResult2 = deleteManager.delete(input);
            assertNull(deleteResult2.getUser());
            assertEquals("User does not exist", deleteResult2.getErrorMessage());
            assertFalse(deleteResult2.isSuccess());
        }
    }
}

package com.javahelp.backend.domain.user.delete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.delete.DeleteManager;
import com.javahelp.backend.domain.user.delete.DeleteResult;
import com.javahelp.backend.domain.user.delete.IDeleteInputBoundary;
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

    DeleteManager deleteManager;

    byte[] salt;
    byte[] password;
    UserPassword userPassword;

    @Before
    public void setUp() {

        // Setting up user, userStore, and deleteManager.
        userStore = IUserStore.getDefaultImplementation();
        clientUserInfo = new ClientUserInfo("uoft@uoft.ca", "University of Toronto",
                "123-456-7890", "J", "M");
        user = new User("123456QWERTY", clientUserInfo, "cs207");

        deleteManager = new DeleteManager(userStore);

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
        userStore.create(user, userPassword);
        assertNotNull(userStore.read(user.getStringID()));

        IDeleteInputBoundary input = new IDeleteInputBoundary() {
            final String userID = user.getStringID();

            @Override
            public String getUserID() {
                return userID;
            }
        };

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

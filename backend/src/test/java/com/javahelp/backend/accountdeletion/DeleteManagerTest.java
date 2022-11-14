package com.javahelp.backend.accountdeletion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

    DeleteManager deleteManager;

    IDeleteInputBoundary input;

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

        input = new IDeleteInputBoundary() {
            final String userID = user.getStringID();

            @Override
            public String getUserID() {
                return userID;
            }
        };

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
    public void testGetUserID() {
        assertEquals("123456QWERTY", input.getUserID());
    }

    @Test
    public void testDelete() {
        userStore.create(user, userPassword);
        assertNotNull(userStore.read(user.getStringID()));
        DeleteResult deleteResult = deleteManager.delete(input);
        assertNotNull(deleteResult.getUser());
        assertEquals("Account deletion successful", deleteResult.getSuccessMessage());
        assertNull(userStore.read(user.getStringID()));
    }
}

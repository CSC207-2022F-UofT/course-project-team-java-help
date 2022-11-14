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
    UserPassword registeredPassword;
    UserPassword enteredCorrectPassword;
    UserPassword enteredIncorrectPassword;
    ClientUserInfo clientUserInfo;

    DeleteManager deleteManager;

    IDeleteInputBoundary correctPasswordInputBoundary;
    IDeleteInputBoundary incorrectPasswordInputBoundary;

    byte[] salt1;
    byte[] salt2;

    byte[] password1;
    byte[] password2;

    @Before
    public void setUp() {
        // Same setup as in UserPasswordTest.
        Random rand = new Random();
        byte[] salt1 = {5, 6, 8, 2, 6, 4, 1};
        byte[] salt2 = {7, 8, 9, 15, 22, 29, 3};

        byte[] password1 = {5, 6, 1, 8, 5, 6, 'a', '!', 6, 5};
        byte[] password2 = {'S', 'e', 'c', 'o', 'n', 'd'};

        this.salt1 = salt1;
        this.salt2 = salt2;
        this.password1 = password1;
        this.password2 = password2;

        rand.nextBytes(salt1);
        rand.nextBytes(salt2);
        rand.nextBytes(password1);
        rand.nextBytes(password2);

        // Setting up user, iUserStore, and deleteManager.
        userStore = IUserStore.getDefaultImplementation();
        clientUserInfo = new ClientUserInfo("uoft@uoft.ca", "University of Toronto",
                "123-456-7890", "J", "M");
        user = new User("12345Q6QWERTY", clientUserInfo, "cs207");
        registeredPassword = new UserPassword(salt1, password1);
        enteredCorrectPassword = new UserPassword(salt1, password1);
        enteredIncorrectPassword = new UserPassword(salt1, password2);
        deleteManager = new DeleteManager(userStore);
        correctPasswordInputBoundary = new DeleteInputBoundaryImplementation(user.getStringID(), enteredCorrectPassword);
        incorrectPasswordInputBoundary = new DeleteInputBoundaryImplementation(user.getStringID(), enteredIncorrectPassword);
    }

    @Test
    public void testPassword(){
        Random rand = new Random();

        rand.nextBytes(password1);
        rand.nextBytes(password2);

        assertNotEquals(password2, registeredPassword.getHash());
        assertEquals(password1, registeredPassword.getHash());
        assertEquals(registeredPassword.getHash(), enteredCorrectPassword.getHash());
        assertNotEquals(registeredPassword.getHash(), enteredIncorrectPassword.getHash());
        assertEquals(registeredPassword.getSalt(), enteredCorrectPassword.getSalt());
        assertEquals(registeredPassword.getBase64SaltHash(), enteredCorrectPassword.getBase64SaltHash());
        assertNotEquals(registeredPassword.getBase64SaltHash(), enteredIncorrectPassword.getBase64SaltHash());
    }

    @Test
    public void testDeleteManagerCorrectPassword() {
        userStore.create(user, registeredPassword);
        assertNotNull(userStore.read(user.getStringID()));
        DeleteResult deleteResult = deleteManager.delete(correctPasswordInputBoundary);
        assertTrue(deleteResult.isSuccess());
        assertNull(deleteResult.getErrorMessage());
        assertNotNull(deleteResult.getUser());
        assertNull(userStore.read(user.getStringID()));
    }

    @Test
    public void testDeleteManagerIncorrectPassword() {
        userStore.create(user, registeredPassword);
        assertNotNull(userStore.read(user.getStringID()));
        DeleteResult deleteResult = deleteManager.delete(incorrectPasswordInputBoundary);
        assertFalse(deleteResult.isSuccess());
        assertEquals("The password is incorrect", deleteResult.getErrorMessage());
        assertNull(deleteResult.getUser());
        assertNotNull(userStore.read(user.getStringID()));
    }
}

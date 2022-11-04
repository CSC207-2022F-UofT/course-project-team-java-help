package com.javahelp.model.userEntityTest;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.javahelp.model.user.UserInfo;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.UserType;
import com.javahelp.model.user.ProviderUserInfo;

import java.util.Random;

public class UserPasswordTest {
    Random rand;

    byte[] saltOne;

    byte[] saltTwo;

    byte[] passWord1;

    byte[] passWord2;

    UserPassword user1Pass;

    UserPassword user2Pass;

    User user1;

    User user2;

    @Before
    public void setUp() {
        // New random object
        this.rand = new Random();

        // Current given 2 salts
        byte[] saltOne = {5, 6, 8, 2, 6, 4, 1};
        byte[] saltTwo = {7, 8, 9, 15, 22, 29, 3};

        // Current given 2 passwords
        byte[] passWord1 = {5, 6, 1, 8, 5, 6, 'a', '!', 6, 5};
        byte[] passWord2 = {'S', 'e', 'c', 'o', 'n', 'd'};

        this.saltOne = saltOne;
        this.saltTwo = saltTwo;
        this.passWord1 = passWord1;
        this.passWord2 = passWord2;

        // Generate a random value for each of the salts and the passwords
        rand.nextBytes(saltOne);
        rand.nextBytes(saltTwo);
        rand.nextBytes(passWord1);
        rand.nextBytes(passWord2);

        ClientUserInfo userOne = new ClientUserInfo("abcdef@mail.utoronto.ca",
                "1 Yonge St., Toronto", "0123459876",
                "Client", "Info");

        ClientUserInfo userTwo = new ClientUserInfo("myself1@gmail.com",
                "0 This St., Porters Lake", "0000000001",
                "First", "Last");

        // Initialize
        this.user1 = new User("ABC75736", userOne);
        this.user2 = new User(" ", userTwo);
        this.user1Pass = new UserPassword(user1, saltOne, passWord1);
        this.user2Pass = new UserPassword(user2, saltTwo, passWord2);
    }

    @Test(timeout = 50)
    public void testUserHashPassword(){
        rand.nextBytes(passWord2);
        rand.nextBytes(passWord1);

        assertNotEquals(passWord2, user1Pass.getHash());
        assertEquals(passWord2, user2Pass.getHash());
    }

    @Test(timeout = 50)
    public void testUserSalt(){
        byte[] password1 = {5, 6, 1, 8, 5, 6, 'a', '!', '6', 5};

        rand.nextBytes(password1);
        rand.nextBytes(saltTwo);

        assertNotEquals(password1, user1Pass.getSalt());
        assertEquals(saltTwo, user2Pass.getSalt());
    }

    @Test(timeout = 50)
    public void testSetHashPassword(){
        byte[] newPass1 = {7, 1, 4};
        rand.nextBytes(newPass1);

        this.user1Pass.setHash(newPass1);
        assertEquals(newPass1, user1Pass.getHash());

        // Not updating the password yet
        byte[] newPass2 = {7, 1, 4, 2};
        rand.nextBytes(newPass2);

        assertNotEquals("The given password is not updated to the given password2 yet!",
                newPass2, user1Pass.getHash());

        // Until now
        user1Pass.setHash(newPass2);
        assertEquals(newPass2, user1Pass.getHash());
    }

    @Test(timeout = 50)
    public void testSetSalt(){
        byte[] newSalt1 = {5, 6, 8, 2, 6, 4, 1, 3};
        rand.nextBytes(newSalt1);

        assertNotEquals("The current salt has not yet been updated to newSalt1!",
                newSalt1, user1Pass.getSalt());

        user1Pass.setSalt(newSalt1);
        assertEquals(newSalt1, user1Pass.getSalt());
    }
}

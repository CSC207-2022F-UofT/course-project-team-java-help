package com.javahelp.backend;

import org.junit.*;


import static org.junit.Assert.*;

public class UserPasswordTest {
    UserPassword user1Pass;

    UserPassword user2Pass;

    User user1;

    User user2;

    @Before
    public void setUp() {
        this.user1 = new User("ABC75736", "This is the first user");
        this.user2 = new User(" ", "This is the second user");
        this.user1Pass = new UserPassword(user1, "ABC".getBytes(), "First Password I guess".getBytes());
        this.user2Pass = new UserPassword(user2, "DEF".getBytes(), "Second Password I guess".getBytes());
    }

    @Test(timeout = 50)
    public void testUserHashPassword(){
        assertNotEquals("".getBytes(), user1Pass.getHash());
        assertEquals("Second Password I guess".getBytes(), user2Pass.getHash());
    }

    @Test(timeout = 50)
    public void testUserSalt(){
        assertNotEquals("ABC ".getBytes(), user1Pass.getSalt());
        assertEquals("DEF".getBytes(), user2Pass.getSalt());
    }

    @Test(timeout = 50)
    public void testSetHashPassword(){
        this.user1Pass.setHash("New password for me!".getBytes());
        assertEquals("New password for me!".getBytes(), user1Pass.getHash());

        user2Pass.setHash("Second Password I guess".getBytes());
        assertEquals("Second Password I guess".getBytes(), user2Pass.getHash());
    }

    @Test(timeout = 50)
    public void testSetSalt(){
        user1Pass.setSalt(" ".getBytes());
        assertEquals(" ".getBytes(), user1Pass.getSalt());

        user2Pass.setSalt(" 2nd".getBytes());
        assertNotEquals("2nd".getBytes(), user2Pass.getSalt());
    }

    @Test(timeout = 50)
    public void testGetBase64SaltHash(){
        assertEquals("ABCFirst Password I guess",
                UserPassword.getBase64SaltHash(user1Pass));
        assertNotEquals("DEFSecond Password I guess",
                UserPassword.getBase64SaltHash(user2Pass));
    }
}

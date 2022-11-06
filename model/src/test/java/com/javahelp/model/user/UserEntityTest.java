package com.javahelp.model.user;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * These are tests for User Entity
 */
public class UserEntityTest {

    /**
     * The first user object
     */
    private User user1;

    /**
     * The second user object
     */
    private User user2;

    /**
     * The info of the first user
     */
    private UserInfo userinfo1;

    /**
     * The info of the second user
     */
    private UserInfo userinfo2;

    @Before
    public void setUp() {
        this.userinfo1 = new ClientUserInfo("abcdef@mail.utoronto.ca",
                "1 Yonge St., Toronto", "0123459876",
                "Client", "Info");

        this.userinfo2 = new ClientUserInfo("myself1@gmail.com",
                "0 This St., Porters Lake", "0000000001",
                "First", "Last");

        this.user1 = new User("ABC75736", userinfo1, "OnceInABlueMoon101");
        this.user2 = new User(" ", userinfo2, "S6C0ND57687378");
    }

    @Test(timeout = 50)
    public void testUserInfo() {
        assertEquals(userinfo1, user1.getUserInfo());
        assertNotEquals("This is not the user info of the current user!", userinfo2,
                user1.getUserInfo());
    }

    @Test(timeout = 50)
    public void testUserID(){
        assertEquals(" ", user2.getStringID());
        assertNotEquals(" ", user1.getStringID());
    }

    @Test(timeout = 50)
    public void testUsername(){
        assertEquals("S6C0ND57687378", user2.getUsername());
        assertNotEquals("OnceInABlueMoon101", user2.getUsername());
    }

    @Test(timeout = 50)
    public void testSetUserInfo(){
        ClientUserInfo newInfo = new ClientUserInfo("abcdef2@mail.utoronto.ca",
                "100 Yonge St., Toronto", "0123459876",
                "Client", "Info");
        user1.setUserInfo(newInfo);
        assertNotEquals("The information has not been updated!",
                userinfo1, user1.getUserInfo());
        assertEquals(newInfo, user1.getUserInfo());
    }

    @Test(timeout = 50)
    public void testSetUsername(){
        // Not setting a new username yet
        String newUsername = "I2E4SG789";
        assertNotEquals(newUsername, user1.getUsername());

        // Until now
        user1.setUsername(newUsername);
        assertEquals(newUsername, user1.getUsername());
    }
}

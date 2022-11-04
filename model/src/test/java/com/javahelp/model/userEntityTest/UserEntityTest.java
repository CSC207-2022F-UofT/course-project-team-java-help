package com.javahelp.model.userEntityTest;

import com.javahelp.model.user.UserInfo;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.UserType;
import com.javahelp.model.user.ProviderUserInfo;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * These are tests for User Entity
 */
public class UserEntityTest {

    /**
     * --- Attributes ----
     * user1 - The first user object
     * user2 - the second user object
     * userinfo1 - the information of the first user
     * userinfo2 - the information of the second user
     */
    private User user1;

    private User user2;

    private UserInfo userinfo1;

    private UserInfo userinfo2;

    @Before
    public void setUp() {
        this.userinfo1 = new ClientUserInfo("abcdef@mail.utoronto.ca",
                "1 Yonge St., Toronto", "0123459876",
                "Client", "Info");

        this.userinfo2 = new ClientUserInfo("myself1@gmail.com",
                "0 This St., Porters Lake", "0000000001",
                "First", "Last");

        this.user1 = new User("ABC75736", userinfo1);
        this.user2 = new User(" ", userinfo2);
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
    public void testSetUserID(){
        ClientUserInfo newInfo = new ClientUserInfo("abcdef2@mail.utoronto.ca",
                "100 Yonge St., Toronto", "0123459876",
                "Client", "Info");
        user1.setUserInfo(newInfo);
        assertNotEquals("The information has not been updated!", userinfo1, user1.getUserInfo());
        assertEquals(newInfo, user1.getUserInfo());
    }

    @Test(timeout = 50)
    public void testSetUsername(){
        assertNotEquals("This is not the correct username!", "ABC7573 ", user1.getStringID());
        user1.setStringID("ABC7573 ");
        assertEquals("ABC7573 ", user1.getStringID());
        user2.setStringID("SECOND");
        assertEquals("SECOND", user2.getStringID());
    }
}

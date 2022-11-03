package com.javahelp.backend;
import org.junit.*;

import static org.junit.Assert.*;

public class UserEntityTest {

    User user1;
    User user2;

    @Before
    public void setUp() {
        this.user1 = new User("ABC75736", "This is the first user");
        this.user2 = new User(" ", "This is the second user");
    }

    @Test(timeout = 50)
    public void testUserInfo() {
        assertEquals("This is the first user", user1.getUserInfo());
        assertNotEquals("This is not the user info of the user!", "This is the first user",
                user2.getUserInfo());
    }

    @Test(timeout = 50)
    public void testUserID(){
        assertEquals(" ", user2.getStringID());
        assertNotEquals(" ", user1.getStringID());
    }

    @Test(timeout = 50)
    public void testSetUserID(){
        user1.setUserInfo("This is *still* the first user");
        assertNotEquals("The information has not been updated!", "This is the first user",
                user1.getUserInfo());
        user2.setUserInfo("This is now a third user");
        assertEquals("This is now a third user", user2.getUserInfo());
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

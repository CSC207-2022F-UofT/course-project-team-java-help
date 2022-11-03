package com.javahelp.model.userTest;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.user.ClientInfo;
import com.javahelp.model.user.UserType;

import org.junit.Before;
import org.junit.Test;

public class ClientInfoTest {

    ClientInfo c;

    @Before
    public void setUp() {
        c = new ClientInfo("uoft@utoronto.ca",
                "University of Toronto", "000-123-4567",
                "Johnny", "Meng");
    }

    @Test(timeout = 50)
    public void testSetEmailAddress() {
        assertEquals("uoft@utoronto.ca", c.getEmailAddress());
        c.setEmailAddress("cs@utoronto.ca");
        assertEquals("cs@utoronto.ca", c.getEmailAddress());
    }

    @Test(timeout = 50)
    public void testSetPhoneNumber() {
        assertEquals("000-123-4567", c.getPhoneNumber());
        c.setPhoneNumber("111-222-3456");
        assertEquals("111-222-3456", c.getPhoneNumber());
    }

    @Test(timeout = 50)
    public void testSetFirstName() {
        assertEquals("Johnny", c.getFirstName());
        c.setFirstName("Jacob");
        assertEquals("Jacob", c.getFirstName());
    }

    @Test(timeout = 50)
    public void testSetLastName() {
        assertEquals("Meng", c.getLastName());
        c.setLastName("Lam");
        assertEquals("Lam", c.getLastName());
    }

    @Test(timeout = 50)
    public void testSetAddress() {
        assertEquals("University of Toronto", c.getAddress());
        c.setAddress("Bahen Center");
        assertEquals("Bahen Center", c.getAddress());
    }

    @Test(timeout = 50)
    public void testGetType() {
        assertEquals(UserType.CLIENT, c.getType());
    }
}

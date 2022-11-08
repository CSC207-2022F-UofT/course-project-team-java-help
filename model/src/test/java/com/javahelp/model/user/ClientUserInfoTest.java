package com.javahelp.model.user;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class ClientUserInfoTest {

    ClientUserInfo c;

    @Before
    public void setUp() {
        c = new ClientUserInfo("uoft@utoronto.ca",
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
    public void testSetSingleAttributeExist() {
        c.setAttribute("Gender", 0);
        assertEquals(Integer.valueOf(0), c.getSingleAttribute("Gender"));
    }

    @Test(timeout = 50)
    public void testSetSingleAttributeNonExist() {
        c.setAttribute("Gender", 0);
        assertEquals(Integer.valueOf(-1), c.getSingleAttribute("Age"));
    }

    @Test(timeout = 50)
    public void testSetMultipleAttribute() {
        c.setAttribute("Gender", 0);
        c.setAttribute("Age", 20);
        c.setAttribute("Symptom", 3);

        HashMap<String, Integer> attributes = new HashMap<String, Integer>();
        attributes.put("Gender", 0);
        attributes.put("Age", 20);
        attributes.put("Symptom", 3);

        assertEquals(attributes, c.getAllAttribute());
    }

    @Test(timeout = 50)
    public void testGetType() {
        assertEquals(UserType.CLIENT, c.getType());
    }
}

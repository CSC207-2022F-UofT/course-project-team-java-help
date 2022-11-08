package com.javahelp.model.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class ProviderUserInfoTest {
    ProviderUserInfo p;

    @Before
    public void setUp() {
        p = new ProviderUserInfo("uoft@utoronto.ca", "St. George",
                "111-234-5678", "University of Toronto");
    }

    @Test(timeout = 50)
    public void testSetEmailAddress() {
        assertEquals("uoft@utoronto.ca", p.getEmailAddress());
        p.setEmailAddress("cs@utoronto.ca");
        assertEquals("cs@utoronto.ca", p.getEmailAddress());
    }

    @Test(timeout = 50)
    public void testSetPracticeName() {
        assertEquals("University of Toronto", p.getPracticeName());
        p.setPracticeName("CS Department");
        assertEquals("CS Department", p.getPracticeName());
    }

    @Test(timeout = 50)
    public void testSetAddress() {
        assertEquals("St. George", p.getAddress());
        p.setAddress("Scarborough");
        assertEquals("Scarborough", p.getAddress());
    }

    @Test(timeout = 50)
    public void testPhoneNumber() {
        assertEquals("111-234-5678", p.getPhoneNumber());
        p.setPhoneNumber("999-888-7654");
        assertEquals("999-888-7654", p.getPhoneNumber());
    }

    @Test(timeout = 50)
    public void testSetCertified() {
        assertFalse(p.isCertified());
        p.setCertified(true);
        assertTrue(p.isCertified());
    }

    @Test(timeout = 50)
    public void testSetSingleAttributeNonExist() {
        p.setAttribute("Gender", 0);
        assertEquals(Integer.valueOf(-1), p.getSingleAttribute("Age"));
    }

    @Test(timeout = 50)
    public void testSetMultipleAttribute() {
        p.setAttribute("Gender", 0);
        p.setAttribute("Age", 20);
        p.setAttribute("Symptom", 3);

        HashMap<String, Integer> attributes = new HashMap<String, Integer>();
        attributes.put("Gender", 0);
        attributes.put("Age", 20);
        attributes.put("Symptom", 3);

        assertEquals(attributes, p.getAllAttribute());
    }

    @Test(timeout = 50)
    public void testGetType() {
        assertEquals(UserType.PROVIDER, p.getType());
    }
}

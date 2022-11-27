package com.javahelp.model.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ProviderUserInfoTest {
    ProviderUserInfo p;
    ProviderUserInfo p2;
    ProviderUserInfo p3;
    ProviderUserInfo p4;

    @Before
    public void setUp() {
        p = new ProviderUserInfo("uoft@utoronto.ca", "St. George",
                "111-234-5678", "University of Toronto", Gender.MALE);
        p2 = new ProviderUserInfo("uofm@umanitoba.ca", "Winnipeg",
                "118-975-4532", "University of Manitoba", true);
        p3 = new ProviderUserInfo("uofv@uvictoria.ca", "Victoria",
                "100-000-000", "University of Victoria", true,
                Gender.FEMALE);
        p4 = new ProviderUserInfo("uoft@utoronto.ca", "St. George",
                "114-234-5678", "University of Toronto");
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
    public void testSetGender() {
        assertNotEquals(Gender.MALE, p2.getGender());
        p2.setGender(Gender.MALE);
        assertEquals(Gender.MALE, p2.getGender());
        assertEquals(Gender.UNMENTIONED, p4.getGender());
    }

    @Test(timeout = 50)
    public void testGetType() {
        assertEquals(UserType.PROVIDER, p.getType());
    }
}

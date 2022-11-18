package com.javahelp.model.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.security.Provider;

public class ProviderUserInfoTest {
    ProviderUserInfo p;

    ProviderUserInfo p2;

    ProviderUserInfo p3;

    ProviderUserInfo p4;

    @Before
    public void setUp() {
        p = new ProviderUserInfo("uoft@utoronto.ca", "St. George",
                "111-234-5678", "University of Toronto", Gender.FEMALE);

        p2 = new ProviderUserInfo("uofm@utoronto.ca", "St. George",
                "190-234-5678", "University of Manitoba", Gender.MALE, true);

        p3 = new ProviderUserInfo("uoft@utoronto.ca", "St. George",
                "125-234-5678", "University of Toronto");

        p4 = new ProviderUserInfo("uofm@utoronto.ca", "St. George",
                "190-234-5678", "University of Manitoba", true);
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
        assertTrue(p2.isCertified());
        assertFalse(p3.isCertified());
        assertTrue(p4.isCertified());
    }

    @Test(timeout = 50)
    public void testSetGender(){
        p.setGender(Gender.OTHERS);
        assertEquals(Gender.OTHERS, p.getGender());
        assertEquals(Gender.UNMENTIONED, p3.getGender());
        assertEquals(Gender.UNMENTIONED, p4.getGender());
    }

    @Test(timeout = 50)
    public void testGetType() {
        assertEquals(UserType.PROVIDER, p.getType());
    }
}

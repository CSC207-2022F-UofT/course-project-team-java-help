package com.javahelp.model.user;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Random;

/**
 * Test {@link SHAPasswordHasher}
 */
public class SHAPasswordHasherTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidKey() {
        SHAPasswordHasher.getInstance().hash("test", new byte[]{1, 2, 3});
    }

    @Test
    public void test_setSalt_noException() {
        Random r = new Random();
        byte[] salt = new byte[64];
        r.nextBytes(salt);
        SHAPasswordHasher.getInstance().hash("test", salt);
    }

    @Test
    public void test_randomSalt_noException() {
        IPasswordHasher hasher = SHAPasswordHasher.getInstance();
        hasher.hash("test", hasher.randomSalt());
    }

    @Test
    public void test_randomSalt_length() {
        assertEquals(64, SHAPasswordHasher.getInstance().randomSalt().length);
    }
}

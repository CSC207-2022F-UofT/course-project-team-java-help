package com.javahelp.frontend.util.auth;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Test {@link SharedPreferencesAuthInformationProvider}
 */
@RunWith(AndroidJUnit4.class)
public class SharedPreferencesAuthInformationProviderTest {

    SharedPreferencesAuthInformationProvider provider;

    @Before
    public void setup() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        provider = new SharedPreferencesAuthInformationProvider(appContext);
    }

    @Test
    public void testUserID() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String id = "this is a randomId";
        provider.setUserID(id);
        assertEquals(id, provider.getUserID());
    }

    @Test
    public void testToken() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String token = "this is a random token";
        provider.setTokenString(token);
        assertEquals(token, provider.getTokenString());
    }

    @Test
    public void testEncryptDecrypt() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Random r = new Random();
        byte[] bytes = new byte[32];
        for (int i = 0; i < 10; i++) {
            r.nextBytes(bytes);
            String encoded = Base64.getEncoder().encodeToString(bytes);
            String encrypted = provider.encryptString(encoded);
            String decrypted = provider.decryptString(encrypted);
            assertEquals(encoded, decrypted);
        }
    }

}

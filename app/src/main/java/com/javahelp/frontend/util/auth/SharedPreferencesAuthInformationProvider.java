package com.javahelp.frontend.util.auth;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.javahelp.frontend.gateway.IAuthInformationProvider;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * {@link IAuthInformationProvider} implemented through shared preferences using
 * advice from https://medium.com/google-developer-experts/a-follow-up-on-how-to-store-tokens-securely-in-android-e84ac5f15f17
 */
public class SharedPreferencesAuthInformationProvider implements IAuthInformationProvider {

    private static final String PREFS = "authProviderPrefs";

    private static final String TOKEN = "token";
    private static final String USER_ID = "userId";

    private Context context;

    static {
        System.loadLibrary("javahelp");
    }

    /**
     * Create a new {@link SharedPreferencesAuthInformationProvider}
     *
     * @param context {@link Context} to create from
     */
    public SharedPreferencesAuthInformationProvider(Context context) {
        this.context = context;
    }


    /**
     * @return encryption key
     */
    public native String getKey();

    /**
     * @return {@link SharedPreferences} for storage
     */
    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFS, MODE_PRIVATE);
    }

    /**
     * Sets the user id
     *
     * @param id {@link String} user id to assign
     */
    public void setUserID(String id) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        id = encryptString(id);
        getSharedPreferences().edit().putString(USER_ID, id).commit();
    }

    /**
     * Sets the token
     *
     * @param token token {@link String} to assign
     */
    public void setTokenString(String token) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        token = encryptString(token);
        getSharedPreferences().edit().putString(TOKEN, token).commit();
    }

    /**
     * @return the key's byte representation
     */
    private byte[] getKeyBytes() {
        return getKey().getBytes();
    }

    /**
     * Encrypts the specified {@link String}
     *
     * @param s {@link String} to encrypt
     * @return Base 64 representation of encrypted {@link String}
     */
    String encryptString(String s) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec skeySpec = new SecretKeySpec(getKeyBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(s.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Decrypts a {@link String}
     *
     * @param s Base 64 {@link String} representation of bytes to decrypt
     * @return {@link String} decrypted
     */
    String decryptString(String s) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec skeySpec = new SecretKeySpec(getKeyBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted = Base64.getDecoder().decode(s);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted);
    }

    @Override
    public String getUserID() {
        String id = getSharedPreferences().getString(USER_ID, null);
        if (id != null) {
            try {
                id = decryptString(id);
            } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
                id = null;
            }
        }
        return id;
    }

    @Override
    public String getTokenString() {
        String token = getSharedPreferences().getString(TOKEN, null);
        if (token != null) {
            try {
                token = decryptString(token);
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                token = null;
            }
        }
        return token;
    }
}

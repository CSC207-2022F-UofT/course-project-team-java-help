package com.javahelp.backend.register;

import java.security.SecureRandom;

/**
 * An abstract controller for both providers and clients
 */
public abstract class UserRegisterViewModel {
    /**
     * Static random generator to generate the stringID of this provider
     */
    private static SecureRandom rand;

    /**
     * Return the randomly encoded stringID of the provider's new account
     * @param username their account name
     * @return the encoded version of the username
     */
    public static String generateStringID(String username){
        byte[] coded = username.getBytes();
        rand.nextBytes(coded);

        StringBuilder stringID = new StringBuilder();
        for (byte code: coded){
            stringID.append(code);
        }
        return stringID.toString();
    }

    /**
     * Hash a given password with a given salt appended at front
     * The reason behind why salt is a parameter instead, so that it could stay consistent
     * between two hashed passwords (original and re-entered)
     *
     * @param salt salt of a password (for enhancing security)
     * @param password an input password (could be the first or re-entered)
     *
     * @return the hashed password with salt appended up front (if match succeeded)
     *         OR only the salt (if match failed)
     */
    public static byte[] hash(byte[] salt, String password){
        // Check for null passwords
        if (password.length() == 0) {
            throw new ArrayIndexOutOfBoundsException("The given password is empty. Please " +
                    "give it a value!");
        }

        // Create a new byte[] and append salt up front
        byte[] bytedPassword = password.getBytes();
        byte[] representation = new byte[salt.length + bytedPassword.length];
        System.arraycopy(salt, 0, representation, 0, salt.length);
        // IHashFactory
        System.arraycopy(bytedPassword, 0, representation, salt.length, password.length());

        return representation;
    }
}

package com.javahelp.model.user;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Singleton implementation of {@link IPasswordHasher} for hashing passwords with SHA.
 */
public class SHAPasswordHasher implements IPasswordHasher {

    private static final int HASH_LENGTH = 64;

    private static final int SALT_LENGTH = 64;

    private static final int REPETITIONS = 10000;

    private static final SHAPasswordHasher INSTANCE = new SHAPasswordHasher();

    /**
     * Private constructor
     */
    private SHAPasswordHasher() {

    }

    /**
     * @return Instance of {@link SHAPasswordHasher}
     */
    public static IPasswordHasher getInstance() {
        return INSTANCE;
    }

    /**
     * Gets a random, cryptographically secure salt
     *
     * @return A byte array containing the salt
     */
    @Override
    public byte[] randomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Hashes a password
     *
     * @param password {@link String} password to hash
     * @param salt     64 byte array containing salt
     * @return the hashed password
     */
    @Override
    public byte[] hash(String password, byte[] salt) {

        if (salt.length != SALT_LENGTH) {
            throw new IllegalArgumentException("Illegal salt length, must be 512 bit");
        }

        byte[] hash = new byte[HASH_LENGTH + salt.length];

        SecretKeyFactory factory = null;

        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error with hashing implementation");
        }

        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, REPETITIONS,
                8 * HASH_LENGTH);

        Key key = null;

        try {
            key = factory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Error with password or salt in key spec");
        }

        byte[] keyBytes = key.getEncoded();
        System.arraycopy(salt, 0, hash, 0, salt.length);
        System.arraycopy(keyBytes, 0, hash, salt.length, keyBytes.length);
        return hash;
    }
}

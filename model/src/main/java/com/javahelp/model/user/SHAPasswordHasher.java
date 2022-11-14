package com.javahelp.model.user;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import jdk.internal.org.objectweb.asm.util.CheckSignatureAdapter;

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
     *
     * @return Instance of {@link SHAPasswordHasher}
     */
    public static SHAPasswordHasher getInstance() {
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
     * @param password {@link String} password to hash
     * @param salt 64 byte array containing salt
     * @return the hashed password
     */
    @Override
    public byte[] hash(String password, byte[] salt) {
        try {
            byte[] hash = new byte[HASH_LENGTH + salt.length];
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, REPETITIONS,
                8 * HASH_LENGTH);
            Key key = factory.generateSecret(keySpec);
            byte[] keyBytes = key.getEncoded();
            System.arraycopy(salt, 0, hash, 0, salt.length);
            System.arraycopy(keyBytes, 0, hash, salt.length, keyBytes.length);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error with hashing implementation");
        } catch (InvalidKeySpecException e) {
            throw new IllegalArgumentException("Error with password or salt in key spec");
        }
    }
}

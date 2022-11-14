package com.javahelp.model.user;

/**
 * Interface for hashing passwords
 *
 * Hashes {@link String} passwords with a salt
 */
public interface IPasswordHasher {

    /**
     *
     * @return a random salt
     */
    byte[] randomSalt();

    /**
     * Hashes the provided {@link String} password with the specified salt
     * @param password {@link String} password to hash
     * @param salt byte array containing salt
     * @return byte array produced
     */
    byte[] hash(String password, byte[] salt);

}

package com.javahelp.model.user;

import java.util.Arrays;
import java.util.Base64;
import java.util.function.Function;

/**
 * A class for the password of a {@link User}
 */
public class UserPassword {

    private byte[] salt;

    private byte[] hash;

    /**
     * Creates an instance an {@link UserPassword}.
     *
     * @param salt the salt for the hashed password.
     * @param hash the hashed password
     */
    public UserPassword(byte[] salt, byte[] hash) {
        this.salt = salt;
        this.hash = hash;
    }

    /**
     * Creates a new {@link UserPassword}
     *
     * @param combinedBase64 {@link Base64} encoded concatenated salt and hash in format:
     *                       Base64(salt + hash)
     * @param saltLength     length of the salt in bytes
     */
    public UserPassword(String combinedBase64, int saltLength) {
        byte[] joined = Base64.getDecoder().decode(combinedBase64);
        this.salt = Arrays.copyOfRange(joined, 0, saltLength);
        this.hash = Arrays.copyOfRange(joined, saltLength, joined.length);
    }

    /**
     * @return The currently hashed password
     */
    public byte[] getHash() {
        return hash;
    }

    /**
     * @return The salt
     */
    public byte[] getSalt() {
        return salt;
    }

    /**
     * @param hash the hashed password
     */
    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    /**
     * Set the salt
     *
     * @param salt the current salt
     */
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    /**
     * Combines salts and hashes together into one array.
     *
     * @param salt byte array containing the salt
     * @param hash byte array containing the password hash
     * @return concatenation of salt and hash arrays
     */
    private static byte[] combineByteArrays(byte[] salt, byte[] hash) {
        if (hash.length == 0) {
            throw new ArrayIndexOutOfBoundsException("The given password is empty. Please " +
                    "give it a value!");
        }
        byte[] representation = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, representation, 0, salt.length);
        System.arraycopy(hash, 0, representation, salt.length, hash.length);
        return representation;
    }

    /**
     * @return a Base64 {@link String} representation of this {@link UserPassword}
     */
    public String getBase64SaltHash() {
        byte[] concatenated = combineByteArrays(salt, hash);
        return Base64.getEncoder().encodeToString(concatenated);
    }
}

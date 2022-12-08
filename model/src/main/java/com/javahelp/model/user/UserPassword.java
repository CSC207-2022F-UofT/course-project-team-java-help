package com.javahelp.model.user;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Base64;

/**
 * A class for the password of a {@link User}
 */
public class UserPassword {

    private byte[] salt;

    private byte[] hash;

    /**
     * Hashes the provided password
     * @param password {@link String} password
     * @param salt byte array with salt to use
     * @param hasher {@link IPasswordHasher} to use
     */
    public UserPassword(String password, byte[] salt, IPasswordHasher hasher) {
        this.salt = salt;
        this.hash = hasher.hash(password, salt);
    }

    /**
     * Hashes the provided password
     * @param password {@link String} password
     * @param hasher {@link IPasswordHasher} to use
     */
    public UserPassword(String password, IPasswordHasher hasher) {
        this.salt = hasher.randomSalt();
        this.hash = hasher.hash(password, this.salt);
    }

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
     *                       Base64(salt length (Int32) + salt + hash)
     */
    public UserPassword(String combinedBase64) {
        byte[] joined = Base64.getDecoder().decode(combinedBase64);
        byte[] saltLengthArray = Arrays.copyOfRange(joined, 0, 4);
        int saltLength = ByteBuffer.wrap(saltLengthArray).getInt();
        this.salt = Arrays.copyOfRange(joined, 4, saltLength + 4);
        this.hash = Arrays.copyOfRange(joined, saltLength + 4, joined.length);
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
     * @return concatenation of salt and hash arrays, with salt length stuck on front
     */
    private static byte[] combineByteArrays(byte[] salt, byte[] hash) {
        if (hash.length == 0) {
            throw new ArrayIndexOutOfBoundsException("The given password is empty. Please " +
                    "give it a value!");
        }
        byte[] representation = new byte[4 + salt.length + hash.length];
        byte[] saltLength = ByteBuffer.allocate(4).putInt(salt.length).array();
        System.arraycopy(saltLength, 0, representation, 0, 4);
        System.arraycopy(salt, 0, representation, 4, salt.length);
        System.arraycopy(hash, 0, representation, salt.length + 4, hash.length);
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

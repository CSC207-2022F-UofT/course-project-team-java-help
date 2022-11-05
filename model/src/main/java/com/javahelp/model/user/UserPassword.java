package com.javahelp.model.user;

import java.util.Base64;

/**
 * A class for the password of a user entity
 */
public class UserPassword {

    /**
     * --- Attributes ---
     * salt - the current salt of a user
     * hash - the current and hashed password of the user
     */
    private byte[] salt;

    private byte[] hash;

    /** The constructor to build an instance of the password of a user account.
     *
     * @param salt the currently used salt for the hashed password.
     * @param hash the already-hashed password
     */
    public UserPassword(byte[] salt, byte[] hash){
        this.salt = salt;
        this.hash = hash;
    }

    /** Get the current hashed password of UserPassword
     *
     * @return The currently hashed password
     */
    public byte[] getHash() {
        return hash;
    }

    /** Get the current salt of UserPassword
     *
     * @return The currently used salt
     */
    public byte[] getSalt() {
        return salt;
    }

    /** Set current hashed password to the user.
     *
     * @param hash the currently hashed password
     */
    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    /** Set the current salt.
     *
     * @param salt the current salt
     */
    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    /** A helper method to combine salt and hashed passwords together into one array.
     *
     * @param salt the current used salt
     * @param hash the current hashed password of this user
     *
     * @return a concatenated array of salt and hashed password, with salt being up front.
     */
    private static byte[] combineByteArrays (byte[] salt, byte[] hash){
        if (hash.length == 0) {
            throw new ArrayIndexOutOfBoundsException("The given password is empty. Please " +
                    "give it a value!");
        }
        byte[] representation = new byte[salt.length + hash.length];
        System.arraycopy(salt, 0, representation, 0, salt.length);
        System.arraycopy(hash, 0, representation, salt.length, hash.length);
        return representation;
    }

    /** A function that takes an array of both salt and hashed password and return a Base64 String
     * representation of that array.
     *
     * @param password a concatenated array of both salt and hashed password
     *
     * @return a Base64 String representation of the given array.
     */
    public static String getBase64SaltHash(UserPassword password){
        byte[] concatenated = UserPassword.combineByteArrays(password.salt, password.hash);
        return Base64.getEncoder().encodeToString(concatenated);
    }
}

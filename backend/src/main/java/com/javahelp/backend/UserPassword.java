package com.javahelp.backend;

import java.util.Base64;

public class UserPassword {
    /**
     * A class for hashing a user's password
     *
     */

    private byte[] salt;

    private byte[] hash;

    private User user;

    /** The constructor to build an instance of the password of a user account.
     *
     * @param salt - The currently used salt for the hashed password.
     * @param hash - The already-hashed password
     * @param user - the current user account
     */

    public UserPassword(User user, byte[] salt, byte[] hash){
        this.user = user;
        this.salt = salt;
        this.hash = hash;
    }

    /** Getters of the current attributes of UserPassword
     *
     * @return Hash - the currently hashed password
     *         Salt - the currently used salt
     *         User - the current user
     */

    public byte[] getHash() {
        return hash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public User getUser(){
        return user;
    }

    /** The setters of the current attributes. Please note that only current salt and password can be set, while the
     * current user always stays the same in this UserPassword entity!
     *
     * @param hash - the currently hashed password
     *        salt - the current salt
     */

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    /** A helper method to combine salt and hashed passwords together into one array.
     *
     * @param salt - the current used salt
     * @param hash - the current hashed password of this user
     *
     * @return A concatenated array of salt and hashed password, with salt being up front.
     */
    public static byte[] combineByteArrays (byte[] salt, byte[] hash){
        byte[] representation = new byte[salt.length + hash.length];
        int position = 0;
        for (byte element: salt){
            representation[position] = element;
            position++;
        }
        for (byte element: hash){
            representation[position] = element;
            position++;
        }
        return representation;
    }

    /** A function that takes an array of both salt and hashed password and return a Base64 String representation of
     * that array.
     *
     * @param password - a concatenated array of both salt and hashed password
     *
     * @return a Base64 String representation of the given array.
     */

    public static String getBase64SaltHash(UserPassword password){
        byte[] concatenated = UserPassword.combineByteArrays(password.salt, password.hash);
        return "The Base 64 representation of "+ password.user + "is: " +
                Base64.getEncoder().encodeToString(concatenated);
    }


}

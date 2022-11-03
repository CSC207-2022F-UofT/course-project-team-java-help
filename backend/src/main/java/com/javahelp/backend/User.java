package com.javahelp.backend;
// package userinfo;

public class User {
    /**
     * A class for a user entity
     */

    private String stringID;
    private String  userInfo;

    /** A constructor for the current User Entity
     *
     * @param stringID - the ID of the account
     * @param userInfo - The account information.
     */

    public User(String stringID, String userInfo){
        this.stringID = stringID;
        this.userInfo = userInfo;
    }

    /** Set the StringID for the user
     *
     * @param stringID - the updated ID of the account
     *        userInfo - the updated information of the user
     */

    public void setStringID(String stringID) {
        this.stringID = stringID;
    }

    public void setUserInfo(String userInfo){
        this.userInfo = userInfo;
    }

    /** Getters of all the instance variables of the current user.
     *
     * @return StringID - the current ID
     *         userInfo - the current information of the user
     *
     */

    public String getStringID() {
        return stringID;
    }

    public String getUserInfo() {
        return userInfo;
    }
}

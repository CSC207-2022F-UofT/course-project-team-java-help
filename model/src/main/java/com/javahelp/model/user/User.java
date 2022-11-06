package com.javahelp.model.user;

/**
 * A class for a user entity
 */
public class User {

    /**
     * The ID of the user account (randomly generated and immutable)
     */
    private final String stringID;

    /**
     * The information of the current user
     */
    private UserInfo userInfo;

    /**
     * The username of the current user
     */
    private String username;

    /**
     * A constructor for the current User Entity
     *
     * @param stringID the ID of the account
     * @param userInfo the account information
     * @param username the username of the account (randomly generated)
     */
    public User(String stringID, UserInfo userInfo, String username) {
        this.stringID = stringID;
        this.userInfo = userInfo;
        this.username = username;
    }

    /**
     * Set the userinfo for the user
     *
     * @param userInfo an updated userinfo of the account
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * Set the username for the user
     *
     * @param username an updated username of the account
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Get the current user entity ID.
     *
     * @return the current ID of the user in form of a String
     */
    public String getStringID() {
        return stringID;
    }

    /**
     * Get the current information of this user entity.
     * @return the user information of the user in form of a UserInfo instance
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * Get the current username of this user.
     *
     * @return the username of this user.
     */
    public String getUsername(){
        return username;
    }
}

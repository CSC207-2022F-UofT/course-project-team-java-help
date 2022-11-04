package com.javahelp.model.userEntityTest;
import com.javahelp.model.user.UserInfo;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.UserType;
import com.javahelp.model.user.ProviderUserInfo;

/**
 * A class for a user entity
 */
public class User {

    /**
     * --- Attributes---
     * stringID - the ID of the user account
     * userInfo - contains all information of the user. It varies based on Client/Provider
     */
    private String stringID;
    private UserInfo userInfo;

    /**
     * A constructor for the current User Entity
     *
     * @param stringID - the ID of the account
     * @param userInfo - The account information.
     */
    public User(String stringID, UserInfo userInfo) {
        this.stringID = stringID;
        this.userInfo = userInfo;
    }

    /**
     * Set the StringID for the user
     *
     * @param stringID - the updated ID of the account
     */
    public void setStringID(String stringID) {
        this.stringID = stringID;
    }

    /**
     * Set the StringID for the user
     *
     * @param userInfo - the updated ID of the account
     */
    public void setUserInfo(UserInfo userInfo) { this.userInfo = userInfo; }

    /**
     * Get the current user entity ID.
     *
     * @return The current ID of the user in form of a String
     */
    public String getStringID() {
        return stringID;
    }

    /**
     * Get the current information of this user entity.
     * @return The user information of the user in form of a UserInfo instance
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }
}

package com.javahelp.model.token;

import com.javahelp.model.user.User;
import org.joda.time.DateTime;
import java.security.SecureRandom;

/**
 * Representation of token entity
 */

public class Token {
    /**
     * Token requested by the user
     */
    private String token;

    /**
     * Unique id of the token.
     */
    private String id;

    /**
     * Expiry date of the token.
     */
    private DateTime expiry;

    /**
     * Issued date of the token.
     */
    private DateTime issued;

    /**
     * User who requested the token.
     */
    private User user;

    /**
     * This holds any additional information about the toekn.
     */
    private String tag;


    /**
     * Creates a new {@link Token}
     * @param tag {@link String} of this {@link Token}
     * @param user {@link User} who requested this {@link Token}
     */
    public Token(String tag, User user){
        // Creating a token using functions from SecureRandom

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        this.token = bytes.toString();

        this.issued = new DateTime(DateTime.now());
        this.expiry = this.issued.plusDays(365/2);
        this.user = user;
        this.tag = tag;
    }

    /**
     *
     * @return unique {@link String} token for this {@link Token}
     */
    public String getToken(){
        return this.token;
    }

    /**
     *
     * @return {@link String} tag for this {@link Token}
     */
    public String getTag(){
        return this.tag;
    }

    /**
     *
     * @return unique {@link String} id for this {@link Token}
     */
    public String getId(){
        return this.id;
    }

    /**
     *
     * @return {@link String} issued date for this {@link Token}
     */
    public DateTime getIssuedDate(){ return this.issued;}

    /**
     *
     * @return {@link String} expiry date for this {@link Token}
     */
    public DateTime getExpiryDate(){
        return this.expiry;
    }

    /**
     * Setting up the token for this {@link Token}
     */
    public void setToken(String t){
        this.token = t;
    }

    /**
     * Setting up the issue date for the token.
     * @param d1 {@link DateTime} of this {@link Token}
     */
    public void setIssuedDate(DateTime d1){
        this.issued = d1;
    }

    /**
     * Settingup the expiry date for the token.
     * @param d2 {@link DateTime} of this {@link Token}
     */
    public void setExpiryDate(DateTime d2){
        this.expiry = d2;
    }

}

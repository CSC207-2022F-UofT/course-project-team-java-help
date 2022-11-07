package com.javahelp.model.token;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import org.joda.time.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.time.format.DateTimeFormatter;

/**
 * Unit tests for {@link Token}
 */
public class TokenTest {

    // Setting up the user and token
    ClientUserInfo info = new ClientUserInfo("user1@example.com", "New York",
            "111","David", "Smith");
    User user1 = new User("123", info, "user1");
    Token token = new Token("This is Test token", user1);

    @Test(timeout = 50)
    public void getToken(){
        token.setToken("[!jkl123");
        String expected_token = "[!jkl123";
        assertEquals(expected_token,token.getToken());
    }

    @Test(timeout = 50)
    public void getTag(){
        assertEquals("This is Test token", token.getTag());
    }

    @Test(timeout = 50)
    public void getIssuedDate(){
        DateTime issuedDate = token.getIssuedDate();
        token.setIssuedDate(issuedDate);
        DateTime expected_date = token.getIssuedDate();
        assertEquals(expected_date, issuedDate);
    }

    @Test(timeout = 50)
    public void getExpiryDate(){
        DateTime expiredDate = token.getExpiryDate();
        token.setExpiryDate(expiredDate);
        DateTime expected_date = token.getExpiryDate();
        assertEquals(expected_date, expiredDate);
    }
}

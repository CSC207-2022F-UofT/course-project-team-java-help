package com.javahelp.model.token;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import java.time.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.time.format.DateTimeFormatter;

/**
 * Unit tests for {@link Token}
 */
public class TokenTest {
    // Setting up the user and token
    LocalDate issued = LocalDate.of(2022, 11, 06);
    LocalDate expiry = LocalDate.of(2022, 11, 20);
    ClientUserInfo info = new ClientUserInfo("user@example.com", "NY",
            "111","David", "Smith");
    User user1 = new User("123", info, "user1");
    Token token = new Token("[!jkl123", issued, expiry, "This is Test token", user1);

    @Test(timeout = 50)
    public void getToken(){
        String expectedToken = "[!jkl123";
        assertEquals(expectedToken,token.getToken());
    }

    @Test(timeout = 50)
    public void getTag(){
        assertEquals("This is Test token", token.getTag());
    }

    @Test(timeout = 50)
    public void getIssuedDate(){
        LocalDate expectedDate = LocalDate.of(2022, 11, 06);
        assertEquals(expectedDate,token.getIssuedDate());
    }

    @Test(timeout = 50)
    public void getExpiryDate(){
        LocalDate expectedDate = LocalDate.of(2022, 11, 20);
        assertEquals(expectedDate, token.getExpiryDate());
    }
}

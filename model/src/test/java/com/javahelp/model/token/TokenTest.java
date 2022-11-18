package com.javahelp.model.token;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.Gender;
import com.javahelp.model.user.User;
import java.time.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Unit tests for {@link Token}
 */
public class TokenTest {
    // Setting up the user and token
    Instant issued = LocalDate.of(2022, 11, 06).atStartOfDay()
            .atZone(TimeZone.getDefault().toZoneId()).toInstant();
    Instant expiry = LocalDate.of(2022, 11, 20).atStartOfDay()
            .atZone(TimeZone.getDefault().toZoneId()).toInstant();
    ClientUserInfo info = new ClientUserInfo("user@example.com", "NY",
            "111","David", "Smith", Gender.MALE);
    User user1 = new User("123", info, "user1");
    Token token = new Token("[!jkl123", issued, expiry, "This is Test token", user1.getStringID());

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
        Instant expectedDate = LocalDate.of(2022, 11, 06).atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant();
        assertEquals(expectedDate,token.getIssuedDate());
    }

    @Test(timeout = 50)
    public void getExpiryDate(){
        Instant expectedDate = LocalDate.of(2022, 11, 20).atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant();
        assertEquals(expectedDate, token.getExpiryDate());
    }
}

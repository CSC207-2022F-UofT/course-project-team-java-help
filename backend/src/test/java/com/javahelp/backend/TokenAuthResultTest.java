package com.javahelp.backend;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.User;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.TimeZone;

public class TokenAuthResultTest {
    User user1, user2, user3, user4;
    Token token1, token2;

    @Before
    public void setUp() {
        Instant issued = LocalDate.of(2022, 11, 15).atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant();
        Instant expiry = LocalDate.of(2022, 11, 16).atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant();

        ClientUserInfo client1 = new ClientUserInfo("email@mail.com",
                "123 Java St", "000-000-0000", "John", "Doe");
        ClientUserInfo client2 = new ClientUserInfo("email@gmail.com",
                "123 Git St", "000-000-0001", "Jane", "Doe");

        ProviderUserInfo provider1 = new ProviderUserInfo("provider@mail.com",
                "123 Address St", "000-000-0002", "Provider#1");
        ProviderUserInfo provider2 = new ProviderUserInfo("provider@gmail.com",
                "234 Address St", "000-000-0003", "Provider#2");

        user1 = new User("testID1", client1, "testUserName1");
        user2 = new User("testID2", client2, "testUserName2");
        user3 = new User("testID3", provider1, "testUserName3");
        user4 = new User("testID4", provider2, "testUserName4");

        token1 = new Token("wasd!@", issued, expiry, "Token Tag 1", user1.getStringID());
        token2 = new Token("asdf!@", issued, expiry, "Token Tag 2" , user3.getStringID());

    }


    @Test(timeout = 50)
    public void testClientAuthenticate() {
        TokenAuthResult auth1 = new TokenAuthResult(user1, token1);
        System.out.println(auth1.getAuthenticated());
        assertEquals(true, auth1.getAuthenticated());


        TokenAuthResult auth2 = new TokenAuthResult(user2, token1);
        assertEquals(false, auth2.getAuthenticated());
    }

    @Test(timeout = 50)
    public void testProviderAuthenticate() {
        TokenAuthResult auth1 = new TokenAuthResult(user3, token2);
        assertEquals(true, auth1.getAuthenticated());

        TokenAuthResult auth2 = new TokenAuthResult(user4, token2);
        assertEquals(false, auth2.getAuthenticated());
    }
}

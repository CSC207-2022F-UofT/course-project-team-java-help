package com.javahelp.backend;

import static org.junit.Assert.assertEquals;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.util.TimeZone;

public class TokenAuthManagerTest {
    User user;
    Token token;
    TokenAuthResult result;
    ITokenStore tokenStore;
    IUserStore userStore;

    @Before
    public void setUp() {
        Instant issued = LocalDate.of(2022, 11, 15).atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant();
        Instant expiry = LocalDate.of(2022, 11, 16).atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant();

        ClientUserInfo client = new ClientUserInfo("email@mail.com",
                "123 Java St", "000-000-0000", "John", "Doe");

        user = new User("testID", client, "testUserName");
        token = new Token("wasd!@", issued, expiry, "Token Tag", user.getStringID());
        result = new TokenAuthResult(user, token);

    }

    @Test(timeout = 50)
    public void testAuthenticate() {
        TokenAuthManager manager = new TokenAuthManager(userStore, tokenStore);
        assertEquals(result.getAuthenticated(), manager.authenticate(user, token).getAuthenticated());
    }

}


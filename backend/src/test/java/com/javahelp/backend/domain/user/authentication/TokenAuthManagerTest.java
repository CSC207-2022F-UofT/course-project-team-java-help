package com.javahelp.backend.domain.user.authentication;

import static org.junit.Assert.assertEquals;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.authentication.TokenAuthManager;
import com.javahelp.backend.domain.user.authentication.TokenAuthResult;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

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
        result = new TokenAuthResult(user.getStringID(), token);

        byte[] salt = {1};
        byte[] password = {'a'};

        tokenStore = ITokenStore.getDefaultImplementation();
        userStore = IUserStore.getDefaultImplementation();
        tokenStore.create(token);
        userStore.create(user, new UserPassword(salt, password));

    }

    @Test(timeout = 100)
    public void testAuthenticate() {
        TokenAuthManager manager = new TokenAuthManager(userStore, tokenStore);
        TokenAuthResult testResult = manager.authenticate("testID", token.getToken());
        assertEquals(result.getAuthenticated(),testResult.getAuthenticated());
    }

}


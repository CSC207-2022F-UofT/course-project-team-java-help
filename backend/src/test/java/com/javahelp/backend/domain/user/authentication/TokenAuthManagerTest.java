package com.javahelp.backend.domain.user.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
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
    ITokenStore tokenStore = ITokenStore.getDefaultImplementation();
    IUserStore userStore = IUserStore.getDefaultImplementation();

    @Test
    public void testAuthenticate() {
        Instant issued = LocalDate.of(2022, 11, 15).atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant();
        Instant expiry = LocalDate.of(2022, 11, 16).atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant();

        ClientUserInfo client = new ClientUserInfo("email@mail.com",
                "123 Java St", "000-000-0000", "John", "Doe");

        User user = new User("testID", client, "testUserName");
        Token token = new Token("wasd!@", issued, expiry, "Token Tag", user.getStringID());

        byte[] salt = {1};
        byte[] password = {'a'};

        userStore.create(user, new UserPassword(salt, password));
        token.setUserID(user.getStringID());
        tokenStore.create(token);

        TokenAuthManager manager = new TokenAuthManager(userStore, tokenStore);
        TokenAuthResult testResult = manager.authenticate(new ITokenAuthInput() {
            @Override
            public String getUserID() {
                return user.getStringID();
            }

            @Override
            public String getToken() {
                return token.getToken();
            }
        });

        assertTrue(testResult.isSuccess());
        assertEquals(user.getStringID(), testResult.getUser().getStringID());
        assertEquals(token.getToken(), testResult.getToken().getToken());

        tokenStore.delete(token.getToken());
        userStore.delete(user.getStringID());
    }

}


package com.javahelp.backend.domain.user.authentication;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class TokenAuthManagerTest {
    ITokenStore tokenStore = ITokenStore.getDefaultImplementation();
    IUserStore userStore = IUserStore.getDefaultImplementation();

    /**
     *
     * @return whether the database is accessible from the current process
     */
    public boolean databaseAccessible() {
        try {
            tokenStore.read("test");
            userStore.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testAuthenticate() {
        assumeTrue(databaseAccessible());

        Instant issued = LocalDate.of(2022, 11, 15).atStartOfDay()
                .atZone(TimeZone.getDefault().toZoneId()).toInstant();
        Instant expiry = Instant.now().plus(5, ChronoUnit.DAYS);

        ClientUserInfo client = new ClientUserInfo("email@mail.com",
                "123 Java St", "000-000-0000", "John", "Doe");

        User user = new User("testID", client, "testUserName");
        Token token = new Token("wasd!@", issued, expiry, "Token Tag", user.getStringID());

        byte[] salt = {1};
        byte[] password = {'a'};

        try {
            userStore.create(user, new UserPassword(salt, password));
            token.setUserID(user.getStringID());
            tokenStore.create(token);

            TokenAuthInteractor manager = new TokenAuthInteractor(userStore, tokenStore);
            TokenAuthResult testResult = manager.authenticate(new ITokenAuthInputBoundary() {
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
        } finally {
            tokenStore.delete(token.getToken());
            userStore.delete(user.getStringID());
        }
    }

}


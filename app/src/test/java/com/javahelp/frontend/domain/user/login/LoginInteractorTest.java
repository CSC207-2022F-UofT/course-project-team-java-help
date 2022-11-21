package com.javahelp.frontend.domain.user.login;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.javahelp.frontend.gateway.LambdaLoginDataAccess;
import com.javahelp.frontend.gateway.LambdaSaltDataAccess;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.IPasswordHasher;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.CompletedFuture;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;


/**
 * Test {@link LoginInteractor}
 */
public class LoginInteractorTest {

    Optional<LoginResult> result = null;
    String errorOutput = "";
    ILoginOutput output;
    ISaltDataAccess mockedSaltDataAccess;
    User user;
    UserPassword password;
    byte[] salt;

    @Before
    public void setup() {

        output = new ILoginOutput() {
            @Override
            public void success(User user, Token token) {
                result = Optional.of(new LoginResult(user, token));
            }

            @Override
            public void failure() {
                result = Optional.of(new LoginResult("Failed to authenticate"));
            }

            @Override
            public void error(String errorMessage) {
                result = Optional.empty();
                errorOutput = errorMessage;
            }

            @Override
            public void abort() {
                result = Optional.empty();
            }
        };

        salt = new byte[64];
        Random r = new Random();
        r.nextBytes(salt);

        password = new UserPassword("password", SHAPasswordHasher.getInstance());

        ClientUserInfo info = new ClientUserInfo("sdjkfhkls", "sdf", "sdf",
                "sdfhjkl", "sdjlfhkslkjd");

        user = new User("asdasd", info, "testing_user_login_123");

        mockedSaltDataAccess = (username, email, id, callback) -> {
            callback.completed(salt);
            return new CompletedFuture<>(salt);
        };
    }

    @Test
    public void testMockedLogin() {
        Token t = new Token(Duration.ofDays(1), "session token", user.getStringID());
        LoginResult loginResult = new LoginResult(user, t);

        ILoginDataAccess loginDataAccess = (username, email, id, password, stayLoggedIn, callback) -> {
            callback.completed(loginResult);
            return new CompletedFuture<>(loginResult);
        };

        LoginInteractor interactor = new LoginInteractor(output, mockedSaltDataAccess, loginDataAccess, SHAPasswordHasher.getInstance());
        interactor.login(user.getStringID(), null, null, "password", false);

        assertTrue(result.isPresent() && result.get().isSuccess());
    }

    @Test
    public void testRealLogin_MockedSalt() throws InterruptedException {

        ILoginDataAccess loginAccess = LambdaLoginDataAccess.getInstance();
        IPasswordHasher hasher = SHAPasswordHasher.getInstance();

        LoginInteractor interactor = new LoginInteractor(output, mockedSaltDataAccess, loginAccess, hasher);
        interactor.login(null, user.getUsername(), null, "password", false);

        // give enough time for response to come back
        Thread.sleep(5000);

        assertTrue(result.isPresent() && !result.get().isSuccess());
    }

    @Test
    public void testReadLogin_RealSalt() throws InterruptedException {

        ISaltDataAccess saltAccess = LambdaSaltDataAccess.getInstance();
        ILoginDataAccess loginAccess = LambdaLoginDataAccess.getInstance();
        IPasswordHasher hasher = SHAPasswordHasher.getInstance();

        LoginInteractor interactor = new LoginInteractor(output, saltAccess, loginAccess, hasher);
        interactor.login(null, user.getUsername(), null, "password", false);

        // give enough time for response to come back
        Thread.sleep(5000);

        assertFalse(result.isPresent());
        assertEquals("User not found", errorOutput);
    }

}

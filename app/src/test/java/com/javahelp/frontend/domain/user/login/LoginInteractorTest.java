package com.javahelp.frontend.domain.user.login;

import static org.junit.Assert.assertEquals;

import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.FutureCallback;
import org.junit.Test;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Test {@link LoginInteractor}
 */
public class LoginInteractorTest {

    @Test
    public void testLogin() {

        StringBuilder outputStringBuilder = new StringBuilder();

        byte[] salt = new byte[32];

        Random r = new Random();
        r.nextBytes(salt);

        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());

        ClientUserInfo info = new ClientUserInfo("sdjkfhkls", "sdf", "sdf",
                "sdfhjkl", "sdjlfhkslkjd");

        User u = new User("", info, "testing_user_login_123");

        Token t = new Token(Duration.ofDays(1), "session token", u.getStringID());

        LoginResult loginResult = new LoginResult(u, t);

        ILoginOutput output = new ILoginOutput() {

            @Override
            public void success(User user, Token token) {
                outputStringBuilder.append("success");
            }

            @Override
            public void failure() {
                outputStringBuilder.append("failure");
            }

            @Override
            public void error(String errorMessage) {
                outputStringBuilder.append("error: " + errorMessage);
            }

            @Override
            public void abort() {
                outputStringBuilder.append("abort");
            }
        };

        ISaltDataAccess saltDataAccess = (username, email, id, callback) -> {

            callback.completed(salt);

            return new Future<byte[]>() {
                @Override
                public boolean cancel(boolean b) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public byte[] get() throws ExecutionException, InterruptedException {
                    return salt;
                }

                @Override
                public byte[] get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                    return salt;
                }
            };
        };

        ILoginDataAccess loginDataAccess = (username, email, id, password, stayLoggedIn, callback) -> {
            callback.completed(loginResult);
            return new Future<LoginResult>() {
                @Override
                public boolean cancel(boolean b) {
                    return false;
                }

                @Override
                public boolean isCancelled() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return true;
                }

                @Override
                public LoginResult get() throws ExecutionException, InterruptedException {
                    return loginResult;
                }

                @Override
                public LoginResult get(long l, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
                    return loginResult;
                }
            };
        };

        LoginInteractor interactor = new LoginInteractor(output, saltDataAccess, loginDataAccess, SHAPasswordHasher.getInstance());
        interactor.login(u.getStringID(), "", "", "password", false);

        assertEquals("success", outputStringBuilder.toString());
    }

}

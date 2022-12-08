package com.javahelp.frontend.domain.user.read;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.javahelp.frontend.domain.user.delete.IDeleteDataAccess;
import com.javahelp.frontend.domain.user.login.ILoginDataAccess;
import com.javahelp.frontend.domain.user.login.ISaltDataAccess;
import com.javahelp.frontend.domain.user.register.IRegisterDataAccess;
import com.javahelp.frontend.domain.user.register.RegisterResult;
import com.javahelp.frontend.gateway.IAuthInformationProvider;
import com.javahelp.frontend.gateway.LambdaDeleteDataAccess;
import com.javahelp.frontend.gateway.LambdaLoginDataAccess;
import com.javahelp.frontend.gateway.LambdaReadDataAccess;
import com.javahelp.frontend.gateway.LambdaRegisterDataAccess;
import com.javahelp.frontend.gateway.LambdaSaltDataAccess;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Tests for {@link ReadInteractor}
 */
public class ReadInteractorTest {

    User readUser;
    User u;
    Token t;

    @Test
    public void testRead() throws ExecutionException, InterruptedException {
        IRegisterDataAccess register = LambdaRegisterDataAccess.getInstance();

        t = null;

        readUser = null;

        u = new User("", new ClientUserInfo("test",
                "test",
                "test",
                "test",
                "test"),
                "test_client_for_read");

        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());

        Object readLock = new Object();

        try {
            Future<RegisterResult> result = register.register(u, p, null);
            RegisterResult registerResult = result.get();
            u = registerResult.getUser();
            t = registerResult.getToken();

            IReadDataAccess access = new LambdaReadDataAccess(new IAuthInformationProvider() {
                @Override
                public String getUserID() {
                    return u.getStringID();
                }

                @Override
                public String getTokenString() {
                    return t.getToken();
                }
            });

            IReadOutput output = new IReadOutput() {
                @Override
                public void success(User user) {
                    readUser = user;
                    synchronized (readLock) {
                        readLock.notifyAll();
                    }
                }

                @Override
                public void error(String errorMessage) {
                    synchronized (readLock) {
                        readLock.notifyAll();
                    }
                }

                @Override
                public void abort() {
                    synchronized (readLock) {
                        readLock.notifyAll();
                    }
                }
            };

            ReadInteractor interactor = new ReadInteractor(output, access);

            interactor.read(u.getStringID());

            synchronized (readLock) {
                readLock.wait();
            }

            assertNotNull(readUser);
            assertEquals(u.getUsername(), readUser.getUsername());
            assertEquals(u.getStringID(), readUser.getStringID());
        } finally {
            if (u != null && t != null) {
                new LambdaDeleteDataAccess(new IAuthInformationProvider() {
                    @Override
                    public String getUserID() {
                        return u.getStringID();
                    }

                    @Override
                    public String getTokenString() {
                        return t.getToken();
                    }
                }).delete(u.getStringID(), null).get();
            }
        }
    }

}

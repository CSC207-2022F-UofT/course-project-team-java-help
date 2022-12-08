package com.javahelp.frontend.domain.user.register;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.javahelp.frontend.gateway.IAuthInformationProvider;
import com.javahelp.frontend.gateway.LambdaDeleteDataAccess;
import com.javahelp.frontend.gateway.LambdaRegisterDataAccess;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

/**
 * Tests for {@link RegisterInteractor}
 */
public class RegisterInteractorTest {

    User created;
    Token t;

    @Test
    public void testRegister() throws InterruptedException, ExecutionException {
        IRegisterDataAccess register = LambdaRegisterDataAccess.getInstance();

        created = null;
        t = null;

        User u = new User("", new ClientUserInfo("test",
                "test",
                "test",
                "test",
                "test"),
                "test_client_for_read");

        Object registerLock = new Object();

        try {
            IRegisterOutput output = new IRegisterOutput() {
                @Override
                public void success(User user, Token t) {
                    created = user;
                    RegisterInteractorTest.this.t = t;
                    synchronized (registerLock) {
                        registerLock.notifyAll();
                    }
                }

                @Override
                public void error(String errorMessage) {
                    synchronized (registerLock) {
                        registerLock.notifyAll();
                    }
                }

                @Override
                public void abort() {
                    synchronized (registerLock) {
                        registerLock.notifyAll();
                    }
                }
            };

            RegisterInteractor interactor = new RegisterInteractor(output, register, SHAPasswordHasher.getInstance());

            interactor.register(u, "password");

            synchronized (registerLock) {
                registerLock.wait();
            }

            assertNotNull(created);
            assertEquals(created.getUsername(), u.getUsername());
        } finally {
            if (created != null && t != null) {
                new LambdaDeleteDataAccess(new IAuthInformationProvider() {
                    @Override
                    public String getUserID() {
                        return created.getStringID();
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

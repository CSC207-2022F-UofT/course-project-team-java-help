package com.javahelp.backend.domain.user.authentication;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

/**
 * Test for {@link SaltInteractor}
 */
public class SaltInteractorTest {

    IUserStore users = IUserStore.getDefaultImplementation();

    @Test
    public void testExistingPassword() {
        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());

        ClientUserInfo info = new ClientUserInfo("sdjkfhkls", "sdf", "sdf",
                "sdfhjkl", "sdjlfhkslkjd");

        User u = new User("", info, "testing_user_login_123");

        users.create(u, p);

        SaltInteractor interactor = new SaltInteractor(users);
        SaltResult result = interactor.get(new ISaltInput() {
            @Override
            public String getUserID() {
                return u.getStringID();
            }

            @Override
            public String getUsername() {
                return null;
            }

            @Override
            public String getEmail() {
                return null;
            }
        });

        assertTrue(result.isSuccess());
        assertArrayEquals(p.getSalt(), result.getSalt());

        users.delete(u.getStringID());
    }

    @Test
    public void testNonExistingPassword() {
        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());

        ClientUserInfo info = new ClientUserInfo("sdjkfhkls", "sdf", "sdf",
                "sdfhjkl", "sdjlfhkslkjd");

        User u = new User("ghfgh", info, "testing_user_login_123");

        SaltInteractor interactor = new SaltInteractor(users);
        SaltResult result = interactor.get(new ISaltInput() {
            @Override
            public String getUserID() {
                return null;
            }

            @Override
            public String getUsername() {
                return u.getUsername();
            }

            @Override
            public String getEmail() {
                return null;
            }
        });

        assertFalse(result.isSuccess());
        assertNull(result.getSalt());

    }

}

package com.javahelp.backend.domain.user.login;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import com.javahelp.backend.data.ITokenStore;
import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

/**
 * Tests for {@link LoginInteractor}
 */
public class LoginInteractorTest {

    /**
     *
     * @return whether the database is accessible from the current process
     */
    public boolean databaseAccessible() {
        try {
            IUserStore.getDefaultImplementation().read("test");
            ITokenStore.getDefaultImplementation().read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testLogin() {
        assumeTrue(databaseAccessible());

        IUserStore db = IUserStore.getDefaultImplementation();
        ITokenStore tokens = ITokenStore.getDefaultImplementation();

        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());

        ClientUserInfo info = new ClientUserInfo("sdjkfhkls", "sdf", "sdf",
                "sdfhjkl", "sdjlfhkslkjd");

        User u = new User("", info, "testing_user_login_123");

        try {
            db.create(u, p);

            db.read(u.getStringID()); // consistent read should block until information propagated
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {

            }

            LoginInteractor interactor = new LoginInteractor(db, tokens);

            LoginResult result = interactor.login(new ILoginInputBoundary() {
                @Override
                public String getUsername() {
                    return u.getUsername();
                }

                @Override
                public String getID() {
                    return null;
                }

                @Override
                public String getEmail() {
                    return u.getUserInfo().getEmailAddress();
                }

                @Override
                public UserPassword getPassword() {
                    return p;
                }

                @Override
                public boolean stayLoggedIn() {
                    return false;
                }
            });

            assertTrue(result.isSuccess());
        } finally {
            db.delete(u.getStringID());
        }
    }

}

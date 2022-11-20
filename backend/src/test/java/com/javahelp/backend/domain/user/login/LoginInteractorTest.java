package com.javahelp.backend.domain.user.login;

import static org.junit.Assert.assertTrue;

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

    @Test
    public void testLogin() {
        IUserStore db = IUserStore.getDefaultImplementation();
        ITokenStore tokens = ITokenStore.getDefaultImplementation();

        UserPassword p = new UserPassword("password", SHAPasswordHasher.getInstance());

        ClientUserInfo info = new ClientUserInfo("sdjkfhkls", "sdf", "sdf",
                "sdfhjkl", "sdjlfhkslkjd");

        User u = new User("", info, "testing_user_login_123");

        db.create(u, p);

        LoginInteractor interactor = new LoginInteractor(db, tokens);

        LoginResult result = interactor.login(new ILoginInput() {
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

        db.delete(u.getStringID());
    }

}

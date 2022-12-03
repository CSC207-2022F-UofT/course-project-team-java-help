package com.javahelp.backend.domain.user.register;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.register.client.ClientRegisterInteractor;
import com.javahelp.backend.domain.user.register.client.IClientRegisterInputBoundary;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

/**
 * Tests {@link ClientRegisterInteractor}
 */
public class ClientRegisterTest {
    IUserStore userStore = IUserStore.getDefaultImplementation();
    ClientRegisterInteractor client = new ClientRegisterInteractor(userStore);

    /**
     *
     * @return whether the database is accessible
     */
    private boolean databaseAccessible() {
        try {
            userStore.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testSuccess() {
        assumeTrue(databaseAccessible());

        IClientRegisterInputBoundary input = new IClientRegisterInputBoundary() {
            @Override
            public String getEmailAddress() {
                return "4Streak@gmail.com";
            }

            @Override
            public String getUsername() {
                return "F0URSTR3AK";
            }

            @Override
            public String getAddress() {
                return "1 Yonge St.";
            }

            @Override
            public String getPhoneNumber() {
                return "123-456-9870";
            }

            @Override
            public String getSaltAndHash(){
                return new UserPassword("1399569893",
                        SHAPasswordHasher.getInstance()).getBase64SaltHash();
            }

            @Override
            public String getFirstName() {
                return "First";
            }

            @Override
            public String getLastName() {
                return "Last";
            }
        };

        UserRegisterResult result = null;

        try {
            result = client.register(input);
            assertTrue(result.isSuccess());
        }
        finally{
            if (result.isSuccess()) {
                userStore.delete(result.getUser().getStringID());
            }
        }
    }

    @Test
    public void testFailure() {
        assumeTrue(databaseAccessible());

        ClientUserInfo info = new ClientUserInfo("4Streak@gmail.com",
                "1 This St.", "123-987-4560", "OneIn",
                "One");

        User u = new User("", info, "F0URSTR3AK");

        UserPassword p = new UserPassword("Client#1", SHAPasswordHasher.getInstance());

        IClientRegisterInputBoundary input = new IClientRegisterInputBoundary() {
            @Override
            public String getEmailAddress() {
                return info.getEmailAddress();
            }

            @Override
            public String getUsername() {
                return u.getUsername();
            }

            @Override
            public String getAddress() {
                return info.getAddress();
            }

            @Override
            public String getPhoneNumber() {
                return info.getPhoneNumber();
            }

            @Override
            public String getSaltAndHash() {
                return p.getBase64SaltHash();
            }

            @Override
            public String getFirstName() {
                return info.getFirstName();
            }

            @Override
            public String getLastName() {
                return info.getLastName();
            }
        };

        UserRegisterResult result = null;

        try {
            userStore.create(u, p);

            result = client.register(input);

            assertEquals("Email and username already taken", result.getErrorMessage());
        } finally {
            userStore.delete(u.getStringID());
            if (result != null && result.isSuccess()) {
                userStore.delete(result.getUser().getStringID());
            }
        }
    }
}


package com.javahelp.backend.domain.user.register;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.backend.domain.user.register.provider.IProviderRegisterInputBoundary;
import com.javahelp.backend.domain.user.register.provider.ProviderRegisterInteractor;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

/**
 * Tests {@link ProviderRegisterInteractor}
 */
public class ProviderRegisterTest {
    IUserStore userStore = IUserStore.getDefaultImplementation();
    ProviderRegisterInteractor provider = new ProviderRegisterInteractor(userStore);

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

        IProviderRegisterInputBoundary input = new IProviderRegisterInputBoundary() {
            @Override
            public String getEmailAddress() {
                return "5Streak@gmail.com";
            }

            @Override
            public String getUsername() {
                return "FIV3STR3AK";
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
                return new UserPassword("Provider#5",
                        SHAPasswordHasher.getInstance()).getBase64SaltHash();
            }

            @Override
            public String getPracticeName() {
                return "Provider Five";
            }

            @Override
            public boolean getCertified() {
                return false;
            }
        };

        UserRegisterResult result = null;

        try {
            result = provider.register(input);
            assertTrue(result.isSuccess());
        } finally {
            if (result.isSuccess()) {
                userStore.delete(result.getUser().getStringID());
            }
        }
    }

    @Test
    public void testFailure() {
        assumeTrue(databaseAccessible());

        ProviderUserInfo info = new ProviderUserInfo("nonmatchingemail@gmail.com",
                "1 This St.", "123-987-4560", "Provider One");

        info.setCertified(true);

        User u = new User("", info, "THR33STR3AK");

        UserPassword p = new UserPassword("test", SHAPasswordHasher.getInstance());

        IProviderRegisterInputBoundary input = new IProviderRegisterInputBoundary() {
            @Override
            public String getEmailAddress() {
                return "1InOh1@gmail.com";
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
            public String getSaltAndHash(){
                return p.getBase64SaltHash();
            }

            @Override
            public String getPracticeName() {
                return info.getPracticeName();
            }

            @Override
            public boolean getCertified() {
                return info.isCertified();
            }
        };

        UserRegisterResult result = null;

        try {
            userStore.create(u, p);
            result = provider.register(input);
            assertEquals("Username already taken", result.getErrorMessage());}
        finally{
            userStore.delete(u.getStringID());
            if (result != null && result.isSuccess()) {
                userStore.delete(result.getUser().getStringID());
            }
        }
    }
}

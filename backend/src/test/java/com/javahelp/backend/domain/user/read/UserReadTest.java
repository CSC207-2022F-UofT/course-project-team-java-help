package com.javahelp.backend.domain.user.read;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assume.assumeTrue;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserPassword;

import org.junit.Test;

/**
 * Tests for {@link UserReadInteractor}
 */
public class UserReadTest {

    IUserStore store = IUserStore.getDefaultImplementation();

    /**
     *
     * @return whether the database is currently accessible
     */
    private boolean databaseAccessible() {
        try {
            store.read("test");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Tests a read when the specified {@link User} exists
     */
    @Test
    public void testReadExists() {
        assumeTrue(databaseAccessible());

        ProviderUserInfo info = new ProviderUserInfo("iamreal@hotmail.pl",
                "221b baker street",
                "112358132134",
                "Testing Users Theraputics");
        info.setCertified(true);

        User u = new User("I haven't slept in so long :(",
                info, "this_user_should_exist");

        UserPassword p = new UserPassword("i'm so glad this password is" +
                " hashed and salted so well so no one will know what it is! even if it" +
                " gets leaked no one will know the password for the rest of my accounts!",
                SHAPasswordHasher.getInstance());

        IUserReadInputBoundary input = u::getStringID;

        UserReadInteractor interactor = new UserReadInteractor(store);

        try {
            store.create(u, p);

            User readUser = interactor.readUser(input);

            assertEquals(u.getStringID(), readUser.getStringID());
            assertEquals(u.getUsername(), readUser.getUsername());
            assertEquals(info.isCertified(),
                    ((ProviderUserInfo)readUser.getUserInfo()).isCertified());
        } finally {
            store.delete(u.getStringID());
        }
    }

    /**
     * Tests a read when the specified {@link User} does not exist
     */
    @Test
    public void testReadDoesNotExist() {
        assumeTrue(databaseAccessible());

        UserReadInteractor interactor = new UserReadInteractor(store);

        User readUser = null;

        try {
            readUser = interactor.readUser(() -> "I do not exist, don't look for me");

            assertNull(readUser);
        } finally {
            if (readUser != null) {
                store.delete(readUser.getStringID());
            }
        }
    }

}

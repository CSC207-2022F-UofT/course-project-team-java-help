package com.javahelp.frontend.domain.user.delete;

import static org.junit.Assert.assertTrue;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;
import com.javahelp.model.user.UserPassword;

import org.apache.hc.core5.concurrent.CompletedFuture;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class DeleteInteractorTest {

    Optional<DeleteResult> result;
    User user;
    UserPassword password;
    UserInfo userInfo;
    IDeleteOutput output;
    String errorOutput;

    @Before
    public void setUp() {
        userInfo = new ClientUserInfo("test@mail.com", "University",
                "123-456-7890", "J", "M");
        password = new UserPassword("password", SHAPasswordHasher.getInstance());

        user = new User("test", userInfo, "username");

        output = new IDeleteOutput() {
            @Override
            public void success(User user) {
                result = Optional.of(new DeleteResult(user));
            }

            @Override
            public void failure() {
                result = Optional.of(new DeleteResult("User does not exist"));
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
    }

    @Test(timeout = 5000)
    public void testDeleteInteractor() {
        DeleteResult deleteResult = new DeleteResult(user);
        IDeleteDataAccess deleteDataAccess = (userID, callback) -> {
            callback.completed(deleteResult);
            return new CompletedFuture<>(deleteResult);
        };

        DeleteInteractor deleteInteractor = new DeleteInteractor(output, deleteDataAccess);
        deleteInteractor.delete(user.getStringID());

        assertTrue(result.isPresent() && result.get().isSuccess());
    }
}

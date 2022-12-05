package com.javahelp.frontend.domain.user.delete;

import static org.junit.Assert.assertTrue;

import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;

import org.apache.hc.core5.concurrent.CompletedFuture;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class DeleteInteractorTest {
    UserInfo userInfo;
    User user;
    IDeleteOutput output;
    Optional<DeleteResult> result = null;
    String errorOutput = "";

    @Before
    public void setUp() {
        userInfo = new ClientUserInfo("test@mail.com", "University",
                "123-456-7890", "J", "M");

        user = new User("test", userInfo, "username");
        output = new IDeleteOutput() {
            @Override
            public void deleteSuccess(User user) {
                result = Optional.of(new DeleteResult(user));
            }

            @Override
            public void deleteFailure(String errorMessage) {
                result = Optional.of(new DeleteResult(errorMessage));
            }

            @Override
            public void deleteError(String errorMessage) {
                result = Optional.empty();
                errorOutput = errorMessage;
            }

            @Override
            public void deleteAbort() {
                result = Optional.empty();
            }
        };
    }

    @Test(timeout = 5000)
    public void testDeleteInteractorSuccess() {
        DeleteResult deleteResult = new DeleteResult(user);
        IDeleteDataAccess dataAccess = (userID, callback) -> {
            callback.completed(deleteResult);
            return new CompletedFuture<>(deleteResult);
        };

        DeleteInteractor interactor = new DeleteInteractor(output, dataAccess);
        interactor.delete(user.getStringID());

        assertTrue(result.isPresent() && result.get().isSuccess());
    }

    @Test(timeout = 5000)
    public void testDeleteInteractorFailure() {
        DeleteResult deleteResult = new DeleteResult("User does not exist");
        IDeleteDataAccess dataAccess = (userID, callback) -> {
            callback.completed(deleteResult);
            return new CompletedFuture<>(deleteResult);
        };

        DeleteInteractor interactor = new DeleteInteractor(output, dataAccess);
        interactor.delete(user.getStringID());

        assertTrue(result.isPresent() && !result.get().isSuccess());
    }
}

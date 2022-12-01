package com.javahelp.backend.domain.user.register;

import com.javahelp.backend.data.IUserStore;
import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;
import com.javahelp.model.user.UserPassword;

/**
 * An abstract interactor to make new {@link User} and {@link UserPassword} a specific type of {@link User}
 * @param <T> an interface supplying registration info for a {@link User}
 * @param <U> type of {@link UserInfo} for {@link User} type to register
 */
public abstract class UserRegisterInteractor <T extends IUserRegisterInputBoundary,
        U extends UserInfo>{

    private final IUserStore userStore;

    /**
     * Creates a new {@link UserRegisterInteractor} instance
     *
     * @param userStore  {@link IUserStore} to use
     */
    public UserRegisterInteractor(IUserStore userStore) {
        this.userStore = userStore;
    }

    /**
     * Checks whether email and username are taken
     * @param boundary {@link IUserRegisterInputBoundary} with input
     *
     * @return a {@link RegisterUserResponse} with error message, or else null
     */
    private RegisterUserResponse areEmailAndUsernameTaken(T boundary) {
        User emailTaken = userStore.readByEmail(boundary.getEmailAddress());
        User usernameTaken = userStore.readByUsername(boundary.getUsername());
        if (emailTaken != null && usernameTaken != null){
            return new RegisterUserResponse("Email and username already taken");
        } else if (emailTaken != null){
            return new RegisterUserResponse("Email already taken");
        } else if (usernameTaken != null){
            return new RegisterUserResponse("Username already taken");
        }

        // none already taken
        return null;
    }

    /**
     * Determines whether the provided input can assemble a valid {@link UserInfo}
     * @param boundary <T> {@link IUserRegisterInputBoundary} to retrieve input
     *
     * @return a {@link RegisterUserResponse} containing the error message
     * for invalid {@link IUserRegisterInputBoundary}, or else null
     */
    protected abstract RegisterUserResponse isValidated(T boundary);

    /**
     * Assembles {@link UserInfo} from input
     *
     * @return a {@link IUserRegisterInputBoundary} with input data
     */
    protected abstract U bundleUserInfo(T boundary);

    /**
     * Creates and registers a {@link User}
     * @param boundary <T> valid {@link IUserRegisterInputBoundary}
     *
     * @return a {@link RegisterUserResponse} with the registration results
     */
    private RegisterUserResponse registerUserToDatabase(T boundary) {
        UserInfo info = bundleUserInfo(boundary);
        User user = new User("", info, boundary.getUsername());

        UserPassword userPassword = new UserPassword(boundary.getSaltAndHash());
        User newUser = userStore.create(user, userPassword);

        return new RegisterUserResponse(newUser, userPassword);
    }

    /**
     * Registers a new {@link User}
     * @param boundary <T> {@link IUserRegisterInputBoundary} providing input
     * @return a new {@link RegisterUserResponse} with created {@link User}, or an error message
     */
    public RegisterUserResponse register(T boundary) {
        // check email and username are not taken
        RegisterUserResponse emailUsernameTaken = areEmailAndUsernameTaken(boundary);
        if (emailUsernameTaken != null) {
            return emailUsernameTaken;
        }

        // Check user input valid
        RegisterUserResponse inputInvalid = isValidated(boundary);
        if (inputInvalid != null) {
            return inputInvalid;
        }

        // register a new user
        return registerUserToDatabase(boundary);
    }
}

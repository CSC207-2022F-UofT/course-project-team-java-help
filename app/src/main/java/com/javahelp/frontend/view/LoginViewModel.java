package com.javahelp.frontend.view;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.javahelp.frontend.domain.user.login.ILoginDataAccess;
import com.javahelp.frontend.domain.user.login.ILoginInput;
import com.javahelp.frontend.domain.user.login.ILoginOutput;
import com.javahelp.frontend.domain.user.login.ISaltDataAccess;
import com.javahelp.frontend.domain.user.login.LoginInteractor;
import com.javahelp.frontend.domain.user.login.LoginResult;
import com.javahelp.frontend.gateway.LambdaLoginDataAccess;
import com.javahelp.frontend.gateway.LambdaSaltDataAccess;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.IPasswordHasher;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link AndroidViewModel} for {@link LoginActivity}
 */
public class LoginViewModel extends AndroidViewModel implements ILoginOutput {

    private final MutableLiveData<String> username = new MutableLiveData<>("");
    private final MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> loggingIn = new MutableLiveData<>(Boolean.FALSE);
    private final MutableLiveData<Boolean> staySignedIn = new MutableLiveData<>(Boolean.FALSE);
    private final MutableLiveData<Optional<LoginResult>> loginResult = new MutableLiveData<>(Optional.empty());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ILoginInput loginInteractor;

    /**
     * Default {@link AndroidViewModel} constructor for {@link LoginViewModel}
     * @param application {@link Application} to use
     */
    public LoginViewModel(@NonNull Application application) {
        super(application);
        ILoginDataAccess login = LambdaLoginDataAccess.getInstance();
        ISaltDataAccess salt = LambdaSaltDataAccess.getInstance();
        IPasswordHasher hasher = SHAPasswordHasher.getInstance();
        loginInteractor = new LoginInteractor(this, salt, login, hasher);
    }

    /**
     * Sets the username for this {@link LoginViewModel}
     * @param username {@link String} username
     */
    public void setUsername(String username) {
        this.username.setValue(username);
    }

    /**
     * Sets the password for this {@link LoginViewModel}
     * @param password {@link String} password to use
     */
    public void setPassword(String password) {
        this.password.setValue(password);
    }

    /**
     * Sets whether this {@link LoginViewModel} represents a {@link View} that
     * is currently trying to log in
     * @param b whether currently trying to log in
     */
    public void setLoggingIn(boolean b) {
        loggingIn.setValue(b);
    }

    /**
     * Sets whether the current login form is specifying to stay logged in long term
     * @param b whether to stay logged in
     */
    public void setStaySignedIn(boolean b) {
        staySignedIn.setValue(b);
    }

    /**
     * Sets the login error
     * @param s {@link String} login error to set
     */
    public void setLoginError(String s) {
        loginResult.setValue(Optional.of(new LoginResult(s)));
    }

    /**
     *
     * @return whether the user should stay signed in
     */
    public MutableLiveData<Boolean> shouldStaySignedIn() {
        return staySignedIn;
    }

    /**
     *
     * @return whether there is currently a login attempt going on
     */
    public MutableLiveData<Boolean> isLoggingIn() {
        return loggingIn;
    }

    /**
     *
     * @return the username
     */
    public MutableLiveData<String> getUsername() {
        return username;
    }

    /**
     *
     * @return the password
     */
    public MutableLiveData<String> getPassword() {
        return password;
    }

    /**
     *
     * @return the {@link LoginResult} of the latest login attempt, if applicable
     */
    public MutableLiveData<Optional<LoginResult>> getLoginResult() {
        return loginResult;
    }

    /**
     * Tries to log in
     */
    public void attemptLogin() {
        setLoggingIn(true);
        executor.execute(() -> {
            try {
                // the delay is present in case login immediately returns an error.
                // in the event that login immediately returns an error, if the user does not
                // see the error message under the login button, they may not realize anything
                // has happened. Instead, it is easier to add a small delay so that the user
                // sees the program 'trying' to log in, sees the progress bar appear, sees it
                // disappear, and understands that the login has failed.
                Thread.sleep(250);
            } catch (InterruptedException ignored) {

            }
            loginInteractor.login(null, username.getValue(), null, password.getValue(), false);
        });
    }

    @Override
    public void success(User user, Token token) {
        loggingIn.postValue(false);
        loginResult.postValue(Optional.of(new LoginResult(user, token)));
    }

    @Override
    public void failure() {
        loggingIn.postValue(false);
        loginResult.postValue(Optional.of(new LoginResult("Incorrect username or password")));
    }

    @Override
    public void error(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "Connection error, try again";
        }
        loggingIn.postValue(false);
        loginResult.postValue(Optional.of(new LoginResult(errorMessage)));
    }

    @Override
    public void abort() {
        loggingIn.postValue(false);
        loginResult.postValue(Optional.empty());
    }
}








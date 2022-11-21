package com.javahelp.frontend;

import android.app.Application;
import android.graphics.Path;

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

import java.security.cert.PKIXRevocationChecker;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginViewModel extends AndroidViewModel implements ILoginOutput {

    private MutableLiveData<String> username = new MutableLiveData<>("");
    private MutableLiveData<String> password = new MutableLiveData<>("");
    private MutableLiveData<Boolean> loggingIn = new MutableLiveData<>(Boolean.FALSE);
    private MutableLiveData<Boolean> staySignedIn = new MutableLiveData<>(Boolean.FALSE);
    private MutableLiveData<Optional<String>> loginError = new MutableLiveData<>(Optional.empty());
    private ILoginInput loginInteractor;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        ILoginDataAccess login = LambdaLoginDataAccess.getInstance();
        ISaltDataAccess salt = LambdaSaltDataAccess.getInstance();
        IPasswordHasher hasher = SHAPasswordHasher.getInstance();
        loginInteractor = new LoginInteractor(this, salt, login, hasher);
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }

    public void setPassword(String password) {
        this.password.setValue(password);
    }

    public void setLoggingIn(boolean b) {
        loggingIn.setValue(b);
    }

    public void setStaySignedIn(boolean b) {
        staySignedIn.setValue(b);
    }

    public MutableLiveData<Optional<String>> getLoginError() {
        return loginError;
    }

    public MutableLiveData<Boolean> shouldStaySignedIn() {
        return staySignedIn;
    }

    public MutableLiveData<Boolean> isLoggingIn() {
        return loggingIn;
    }

    public MutableLiveData<String> getUsername() {
        return username;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public void attemptLogin() {
        setLoggingIn(true);
        new Thread(() -> loginInteractor.login(null, username.getValue(), null, password.getValue(), false)).start();
    }

    @Override
    public void success(User user, Token token) {
        loggingIn.postValue(false);
    }

    @Override
    public void failure() {
        loggingIn.postValue(false);
        loginError.postValue(Optional.of("Incorrect username or password"));
    }

    @Override
    public void error(String errorMessage) {
        loggingIn.postValue(false);
        loginError.postValue(Optional.of(errorMessage));
    }

    @Override
    public void abort() {
        loggingIn.postValue(false);
    }
}








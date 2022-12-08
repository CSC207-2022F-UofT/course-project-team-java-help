package com.javahelp.frontend.view;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.javahelp.frontend.domain.user.delete.DeleteInteractor;
import com.javahelp.frontend.domain.user.delete.DeleteResult;
import com.javahelp.frontend.domain.user.delete.IDeleteDataAccess;
import com.javahelp.frontend.domain.user.delete.IDeleteInput;
import com.javahelp.frontend.domain.user.delete.IDeleteOutput;
import com.javahelp.frontend.domain.user.login.ILoginDataAccess;
import com.javahelp.frontend.domain.user.login.ILoginInput;
import com.javahelp.frontend.domain.user.login.ILoginOutput;
import com.javahelp.frontend.domain.user.login.ISaltDataAccess;
import com.javahelp.frontend.domain.user.login.LoginInteractor;
import com.javahelp.frontend.domain.user.login.LoginResult;
import com.javahelp.frontend.gateway.IAuthInformationProvider;
import com.javahelp.frontend.gateway.LambdaDeleteDataAccess;
import com.javahelp.frontend.gateway.LambdaLoginDataAccess;
import com.javahelp.frontend.gateway.LambdaSaltDataAccess;
import com.javahelp.frontend.util.auth.SharedPreferencesAuthInformationProvider;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.IPasswordHasher;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The {@link AndroidViewModel} for {@link DeleteActivity}.
 */
public class DeleteViewModel extends AndroidViewModel implements IDeleteOutput, ILoginOutput {

    private final ILoginInput loginInteractor;
    private final IDeleteInput deleteInteractor;
    private final IAuthInformationProvider provider;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<String> password = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> loggingIn = new MutableLiveData<>();
    private final MutableLiveData<Optional<LoginResult>> loginResult = new MutableLiveData<>(Optional.empty());

    private final MutableLiveData<Boolean> deleting = new MutableLiveData<>(false);
    private final MutableLiveData<Optional<DeleteResult>> deleteResult = new MutableLiveData<>(Optional.empty());

    /**
     * Constructs a {@link DeleteViewModel}.
     * @param application: the {@link Application} to use.
     */
    public DeleteViewModel(@NonNull Application application) {
        super(application);

        provider = new SharedPreferencesAuthInformationProvider(application.getBaseContext());

        ILoginDataAccess loginDataAccess = LambdaLoginDataAccess.getInstance();
        ISaltDataAccess saltDataAccess = LambdaSaltDataAccess.getInstance();
        IPasswordHasher hasher = SHAPasswordHasher.getInstance();
        loginInteractor = new LoginInteractor(this, saltDataAccess, loginDataAccess, hasher);

        IDeleteDataAccess deleteDataAccess = new LambdaDeleteDataAccess(provider);
        deleteInteractor = new DeleteInteractor(this, deleteDataAccess);
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
     * @return whether there is currently a login attempt going on
     */
    public MutableLiveData<Boolean> isLoggingIn() {
        return loggingIn;
    }

    /**
     * Sets the password for this {@link LoginViewModel}
     * @param password {@link String} password to use
     */
    public void setPassword(String password) {
        this.password.setValue(password);
    }

    /**
     * @return the password
     */
    public MutableLiveData<String> getPassword() {
        return password;
    }

    /**
     * Sets the login error
     * @param s {@link String} login error to set
     */
    public void setLoginError(String s) {
        loginResult.setValue(Optional.of(new LoginResult(s)));
    }

    /**
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
                Thread.sleep(250);
            } catch (InterruptedException ignored) {

            }
            loginInteractor.login(provider.getUserID(), null, null, password.getValue(), false);
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
        loginResult.postValue(Optional.of(new LoginResult("Incorrect password")));
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

    /**
     * Sets an error occurred in the delete process.
     * @param errorMessage: the {@link String} error message to be received.
     */
    public void setDeleteError(String errorMessage) {
        deleteResult.setValue(Optional.of(new DeleteResult(errorMessage)));
    }

    /**
     * @return the {@link DeleteResult} of this deletion.
     */
    public MutableLiveData<Optional<DeleteResult>> getDeleteResult() {
        return deleteResult;
    }

    /**
     * @return whether there is a deletion attempt running.
     */
    public MutableLiveData<Boolean> isDeleting() {
        return deleting;
    }

    /**
     * Sets the status of this deletion.
     * @param b: whether this deletion attempt is running.
     */
    public void setDeleting(boolean b) {
        deleting.setValue(b);
    }

    /**
     * Attempts to delete the {@link User}.
     */
    public void attemptDelete() {
        setDeleting(true);
        executor.execute(() -> {
            try {
                Thread.sleep(250);
            } catch (InterruptedException ignored) {

            }
            deleteInteractor.delete(provider.getUserID());
        });
    }

    @Override
    public void deleteSuccess(User user) {
        deleting.postValue(false);
        deleteResult.postValue(Optional.of(new DeleteResult(user)));
    }

    @Override
    public void deleteFailure(String errorMessage) {
        deleting.postValue(false);
        deleteResult.postValue(Optional.of(new DeleteResult(errorMessage)));
    }

    @Override
    public void deleteError(String errorMessage) {
        if (errorMessage == null) {
            errorMessage = "Connection error, try again";
        }
        deleting.postValue(false);
        deleteResult.postValue(Optional.of(new DeleteResult(errorMessage)));
    }

    @Override
    public void deleteAbort() {
        deleting.postValue(false);
        deleteResult.postValue(Optional.empty());
    }
}

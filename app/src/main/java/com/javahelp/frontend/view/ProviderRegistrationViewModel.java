package com.javahelp.frontend.view;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.javahelp.frontend.domain.user.register.IRegisterDataAccess;
import com.javahelp.frontend.domain.user.register.IRegisterOutput;
import com.javahelp.frontend.domain.user.register.RegisterInteractor;
import com.javahelp.frontend.domain.user.register.RegisterResult;
import com.javahelp.frontend.gateway.LambdaRegisterDataAccess;
import com.javahelp.frontend.util.auth.SharedPreferencesAuthInformationProvider;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.IPasswordHasher;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * View model for registering providers
 */
public class ProviderRegistrationViewModel extends AndroidViewModel implements IRegisterOutput {

    private MutableLiveData<String> username = new MutableLiveData<>("");
    private MutableLiveData<String> password = new MutableLiveData<>("");
    private MutableLiveData<String> email = new MutableLiveData<>("");
    private MutableLiveData<String> practiceName = new MutableLiveData<>("");
    private MutableLiveData<String> address = new MutableLiveData<>("");
    private MutableLiveData<String> phone = new MutableLiveData<>("");
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<Optional<RegisterResult>> registerResult = new MutableLiveData<>(Optional.empty());
    private RegisterInteractor registerInteractor;
    private Context context;

    /**
     * Creates a new {@link ProviderRegistrationViewModel}
     *
     * @param application {@link Application} to use
     */
    public ProviderRegistrationViewModel(@NonNull Application application) {
        super(application);
        context = application.getBaseContext();
        IRegisterDataAccess register = LambdaRegisterDataAccess.getInstance();
        IPasswordHasher hasher = SHAPasswordHasher.getInstance();
        registerInteractor = new RegisterInteractor(this, register, hasher);
    }

    /**
     * Sets the username for this {@link ClientRegistrationViewModel}
     *
     * @param username {@link String} username
     */
    public void setUsername(String username) {
        this.username.postValue(username);
    }

    /**
     * Sets the password for this {@link ClientRegistrationViewModel}
     *
     * @param password {@link String} password1 to use
     */
    public void setPassword(String password) {
        this.password.postValue(password);
    }

    /**
     * Sets the email for this {@link ClientRegistrationViewModel}
     *
     * @param email {@link String} email to use
     */
    public void setEmail(String email) {
        this.email.postValue(email);
    }

    /**
     * Sets the firstname for this {@link ClientRegistrationViewModel}
     *
     * @param firstname {@link String} firstname to use
     */
    public void setPracticeName(String firstname) {
        this.practiceName.postValue(firstname);
    }

    /**
     * Sets the address for this {@link ClientRegistrationViewModel}
     *
     * @param address {@link String} address to use
     */
    public void setAddress(String address) {
        this.address.postValue(address);
    }

    /**
     * Sets the phone for this {@link ClientRegistrationViewModel}
     *
     * @param phone {@link String} phone to use
     */
    public void setPhone(String phone) {
        this.phone.setValue(phone);
    }

    /**
     * Sets the register error
     *
     * @param s {@link String} register error to set
     */
    public void setRegisterError(String s) {
        registerResult.setValue(Optional.of(new RegisterResult(s)));
    }

    /**
     * @return the {@link RegisterResult} of the latest register attempt, if applicable
     */
    public LiveData<Optional<RegisterResult>> getRegisterResult() {
        return registerResult;
    }

    /**
     * Tries to Register in
     */
    public void attemptRegister() {
        executor.execute(() -> {
            ProviderUserInfo info = new ProviderUserInfo(
                    email.getValue(),
                    address.getValue(),
                    phone.getValue(),
                    practiceName.getValue());
            info.setCertified(false);
            User u = new User("", info, username.getValue());
            registerInteractor.register(u, password.getValue());
        });
    }

    @Override
    public void success(User user, Token token) {
        registerResult.postValue(Optional.of(new RegisterResult(user, token)));
        SharedPreferencesAuthInformationProvider provider = new SharedPreferencesAuthInformationProvider(context);
        try {
            provider.setUserID(user.getStringID());
            provider.setTokenString(token.getToken());
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException ignored) {
            Toast.makeText(context, "Register successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void error(String errorMessage) {
        registerResult.postValue(Optional.of(new RegisterResult(errorMessage)));
    }

    @Override
    public void abort() {
        registerResult.postValue(Optional.empty());
    }
}

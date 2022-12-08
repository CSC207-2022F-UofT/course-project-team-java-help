package com.javahelp.frontend.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.javahelp.frontend.domain.user.register.IRegisterDataAccess;
import com.javahelp.frontend.domain.user.register.IRegisterOutput;
import com.javahelp.frontend.domain.user.register.RegisterInteractor;
import com.javahelp.frontend.domain.user.register.RegisterResult;
import com.javahelp.frontend.gateway.LambdaRegisterDataAccess;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.IPasswordHasher;
import com.javahelp.model.user.ProviderUserInfo;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProviderRegistrationViewmodel extends AndroidViewModel implements IRegisterOutput {

    private MutableLiveData<String> username = new MutableLiveData<>("");
    private MutableLiveData<String> password1 = new MutableLiveData<>("");
    private MutableLiveData<String> password2 = new MutableLiveData<>("");
    private MutableLiveData<String> email = new MutableLiveData<>("");
    private MutableLiveData<String> practicename = new MutableLiveData<>("");
    private MutableLiveData<String> address = new MutableLiveData<>("");
    private MutableLiveData<String> phone = new MutableLiveData<>("");
    private MutableLiveData<Boolean> certified = new MutableLiveData<>(Boolean.FALSE);
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<Optional<RegisterResult>> registerResult = new MutableLiveData<>(Optional.empty());
    private ProviderUserInfo providerUserInfo = new ProviderUserInfo("", "", "", "");
    private RegisterInteractor registerInteractor;


    public ProviderRegistrationViewmodel(@NonNull Application application) {
        super(application);
        IRegisterDataAccess register = LambdaRegisterDataAccess.getInstance();
        IPasswordHasher hasher = SHAPasswordHasher.getInstance();
        registerInteractor = new RegisterInteractor(this, register, hasher);
    }

    /**
     * Sets the username for this {@link ProviderRegistrationViewmodel}
     *
     * @param username {@link String} username
     */
    public void setUsername(String username) {
        this.username.setValue(username);
    }

    /**
     * Sets the password for this {@link ProviderRegistrationViewmodel}
     *
     * @param password1 {@link String} password1 to use
     */
    public void setPassword1(String password1) {
        this.password1.setValue(password1);
    }

    /**
     * Sets the confirmed_password for this {@link ProviderRegistrationViewmodel}
     *
     * @param password2 {@link String} password2 to use
     */
    public void setPassword2(String password2) {
        this.password2.setValue(password2);
    }

    /**
     * Sets the email for this {@link ProviderRegistrationViewmodel}
     *
     * @param email {@link String} email to use
     */
    public void setEmail(String email) {
        this.email.setValue(email);
    }

    /**
     * Sets the practicename for this {@link ProviderRegistrationViewmodel}
     *
     * @param practicename {@link String} firstname to use
     */
    public void setPracticename(String practicename) {
        this.practicename.setValue(practicename);
    }

    /**
     * Sets the address for this {@link ProviderRegistrationViewmodel}
     *
     * @param address {@link String} address to use
     */
    public void setAddress(String address) {
        this.address.setValue(address);
    }

    /**
     * Sets the phone for this {@link ProviderRegistrationViewmodel}
     *
     * @param phone {@link String} phone to use
     */
    public void setPhone(String phone) {
        this.phone.setValue(phone);
    }

    /**
     * Sets whether the current provider is certified or not
     *
     * @param c whether certified
     */
    public void setCertified(boolean c) {
        certified.setValue(c);
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
     * Sets the provideruserinfo for this {@link ProviderRegistrationViewmodel}
     *
     * @param email, practicename, phone, address {@link String}
     */
    public void setProviderUserInfo(String email, String practicename, String phone, String address) {
        this.providerUserInfo.setEmailAddress(email);
        this.providerUserInfo.setPracticeName(practicename);
        this.providerUserInfo.setCertified(false);
        this.providerUserInfo.setAddress(address);
        this.providerUserInfo.setPhoneNumber(phone);
    }

    /**
     * @return the {@link RegisterResult} of the latest register attempt, if applicable
     */
    public MutableLiveData<Optional<RegisterResult>> getRegisterResult() {
        return registerResult;
    }

    /**
     * @param password1, password2 {@link String}
     * @return the boolean of the input passwords are matched or not
     */
    public boolean passwordMatch(String password1, String password2) {
        return password1.equals(password2);

    }


    /**
     * Tries to Register in
     */
    public void attemptRegister() {
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
            if (passwordMatch(password1.getValue(), password2.getValue())) {
                ProviderUserInfo info = new ProviderUserInfo(
                        email.getValue(),
                        address.getValue(),
                        phone.getValue(),
                        practicename.getValue());
                info.setCertified(certified.getValue());
                User u = new User("", info, username.getValue());
                registerInteractor.register(u, password1.getValue());
            }

        });
    }


    @Override
    public void success(User user, Token token) {

    }

    @Override
    public void error(String errorMessage) {

    }

    @Override
    public void abort() {

    }
}

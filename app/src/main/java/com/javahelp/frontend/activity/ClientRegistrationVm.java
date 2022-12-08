package com.javahelp.frontend.activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.javahelp.frontend.domain.user.Client_register.IRegisterDataAccess;
import com.javahelp.frontend.domain.user.Client_register.IRegisterOutput;
import com.javahelp.frontend.domain.user.Client_register.RegisterInteractor;
import com.javahelp.frontend.domain.user.Client_register.RegisterResult;
import com.javahelp.frontend.gateway.LambdaCRegisterDataAccess;
import com.javahelp.model.token.Token;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.IPasswordHasher;
import com.javahelp.model.user.SHAPasswordHasher;
import com.javahelp.model.user.User;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientRegistrationVm extends AndroidViewModel implements IRegisterOutput {

    private MutableLiveData<String> username = new MutableLiveData<>("");
    private MutableLiveData<String> password1 = new MutableLiveData<>("");
    private MutableLiveData<String> password2 = new MutableLiveData<>("");
    private MutableLiveData<String> email = new MutableLiveData<>("");
    private MutableLiveData<String> firstname = new MutableLiveData<>("");
    private MutableLiveData<String> lastname = new MutableLiveData<>("");
    private MutableLiveData<String> address = new MutableLiveData<>("");
    private MutableLiveData<String> phone = new MutableLiveData<>("");
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private MutableLiveData<Optional<RegisterResult>> registerResult = new MutableLiveData<>(Optional.empty());
    private ClientUserInfo clientUserInfo = new ClientUserInfo("", "", "", "", "");
    private RegisterInteractor registerInteractor;

    public ClientRegistrationVm(@NonNull Application application) {

        super(application);
        IRegisterDataAccess register = LambdaCRegisterDataAccess.getInstance();
        IPasswordHasher hasher = SHAPasswordHasher.getInstance();
        registerInteractor = new RegisterInteractor(this, register, hasher);
    }

    /**
     * Sets the username for this {@link ClientRegistrationVm}
     *
     * @param username {@link String} username
     */
    public void setUsername(String username) {
        this.username.setValue(username);
    }

    /**
     * Sets the password for this {@link ClientRegistrationVm}
     *
     * @param password1 {@link String} password1 to use
     */
    public void setPassword1(String password1) {
        this.password1.setValue(password1);
    }

    /**
     * Sets the confirmed_password for this {@link ClientRegistrationVm}
     *
     * @param password2 {@link String} password2 to use
     */
    public void setPassword2(String password2) {
        this.password2.setValue(password2);
    }

    /**
     * Sets the email for this {@link ClientRegistrationVm}
     *
     * @param email {@link String} email to use
     */
    public void setEmail(String email) {
        this.email.setValue(email);
    }

    /**
     * Sets the firstname for this {@link ClientRegistrationVm}
     *
     * @param firstname {@link String} firstname to use
     */
    public void setFirstname(String firstname) {
        this.firstname.setValue(firstname);
    }

    /**
     * Sets the lastname for this {@link ClientRegistrationVm}
     *
     * @param lastname {@link String} firstname to use
     */
    public void setLastname(String lastname) {
        this.firstname.setValue(lastname);
    }

    /**
     * Sets the address for this {@link ClientRegistrationVm}
     *
     * @param address {@link String} address to use
     */
    public void setAddress(String address) {
        this.address.setValue(address);
    }

    /**
     * Sets the phone for this {@link ClientRegistrationVm}
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
     * Sets the clientUserInfo for this {@link ClientRegistrationVm}
     *
     * @param email, firstname, lastname, phone, address {@link String}
     */
    public void setClientUserInfo(String email, String address, String phone, String firstname, String lastname) {
        this.clientUserInfo.setEmailAddress(email);
        this.clientUserInfo.setFirstName(firstname);
        this.clientUserInfo.setLastName(lastname);
        this.clientUserInfo.setAddress(address);
        this.clientUserInfo.setPhoneNumber(phone);
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
                Thread.sleep(250);
            } catch (InterruptedException ignored) {

            }
            if (passwordMatch(password1.getValue(), password2.getValue())) {
                registerInteractor.register(username.getValue(), password1.getValue(), clientUserInfo);
            }
        });
    }

    @Override
    public void success(User user, Token token) {

    }

    @Override
    public void failure() {

    }

    @Override
    public void error(String errorMessage) {

    }

    @Override
    public void abort() {

    }
}

package com.javahelp.frontend.view;


import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.javahelp.frontend.domain.user.login.ILoginInput;
import com.javahelp.frontend.domain.user.read.IReadInput;
import com.javahelp.frontend.domain.user.read.IReadOutput;
import com.javahelp.frontend.domain.user.read.ReadInteractor;
import com.javahelp.frontend.gateway.IAuthInformationProvider;
import com.javahelp.frontend.gateway.LambdaReadDataAccess;
import com.javahelp.frontend.util.auth.SharedPreferencesAuthInformationProvider;
import com.javahelp.model.user.ClientUserInfo;
import com.javahelp.model.user.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * View model for an {@link AccountFragment}
 */
public class AccountFragmentViewModel extends AndroidViewModel implements IReadOutput {

    private MutableLiveData<String> fullName = new MutableLiveData<>("Full Name");

    private MutableLiveData<String> address = new MutableLiveData<>("123 Street");

    private MutableLiveData<String> phone = new MutableLiveData<>("+1 (234) 567-8901");

    private MutableLiveData<String> email = new MutableLiveData<>("test@email.com");

    private MutableLiveData<Boolean> registering = new MutableLiveData<>(false);

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private IReadInput readInteractor;

    private IAuthInformationProvider provider;

    private Context context;

    /**
     * Creates a new {@link AccountFragmentViewModel}
     *
     * @param application {@link Application} to use
     */
    public AccountFragmentViewModel(@NonNull Application application) {
        super(application);
        context = application.getBaseContext();
        provider = new SharedPreferencesAuthInformationProvider(context);
        readInteractor = new ReadInteractor(this,
                new LambdaReadDataAccess(provider));
    }

    /**
     *
     * @return {@link LiveData} whether fetching info
     */
    public LiveData<Boolean> getRegistering() {
        return registering;
    }

    /**
     * @return {@link LiveData} full name
     */
    public LiveData<String> getFullName() {
        return fullName;
    }

    /**
     * @return {@link LiveData} address
     */
    public LiveData<String> getAddress() {
        return address;
    }

    /**
     * @return {@link LiveData} phone number
     */
    public LiveData<String> getPhoneNumber() {
        return phone;
    }

    /**
     * @return {@link LiveData} email
     */
    public LiveData<String> getEmail() {
        return email;
    }

    /**
     * Gets the current user's info
     */
    public void getInfo() {
        executor.execute(() -> {
            readInteractor.read(provider.getUserID());
            registering.postValue(true);
        });
    }

    @Override
    public void success(User user) {
        ClientUserInfo info = (ClientUserInfo) user.getUserInfo();
        fullName.postValue(info.getFirstName() + " " + info.getLastName());
        address.postValue(info.getAddress());
        phone.postValue(info.getPhoneNumber());
        email.postValue(info.getEmailAddress());
        registering.postValue(false);
    }

    @Override
    public void error(String errorMessage) {
        registering.postValue(false);
        Toast.makeText(context, "Error fetching user info", Toast.LENGTH_LONG).show();
    }

    @Override
    public void abort() {
        registering.postValue(false);
        Toast.makeText(context, "Aborted fetching user info", Toast.LENGTH_LONG).show();
    }
}

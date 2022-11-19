package com.javahelp.frontend;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends AndroidViewModel {
    private MutableLiveData<String> userName;
    private MutableLiveData<String> passWord;

    public MyViewModel(@NonNull Application application) {
        super(application);
    }


    public void setUserName(String username) {
        userName.setValue(username);

    }

    public MutableLiveData<String> getUserName() {
        if (userName == null) {
            userName = new MutableLiveData<>();
            userName.setValue("");
        }
        return userName;
    }

    public MutableLiveData<String> getPassWord() {
        if (passWord == null) {
            passWord = new MutableLiveData<>();
            passWord.setValue("");
        }
        return passWord;
    }

}








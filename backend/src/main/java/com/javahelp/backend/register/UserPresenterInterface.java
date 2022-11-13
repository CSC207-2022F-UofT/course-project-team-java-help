package com.javahelp.backend.register;

import com.javahelp.model.user.User;
import com.javahelp.model.user.UserInfo;

public interface UserPresenterInterface {
    String successView(UserInfo userInfo);

    String failureView(String error);
}
